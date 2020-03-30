package com.vocabulary.managers

import android.content.Context
import android.database.DatabaseUtils
import com.vocabulary.R
import com.vocabulary.db.DBHelper
import com.vocabulary.db.DBUtil
import com.vocabulary.models.LanguageModel
import com.vocabulary.models.ExerciseFailModel
import com.vocabulary.models.JsonWord
import com.vocabulary.models.word_models.WordModel
import java.lang.Exception


class DBManager(private val context: Context) {

    private var db: DBHelper

    init {
        this.db = DBHelper(context)
    }


    fun getAllTables(result: (String) -> Unit) {
        val arrTblNames = ArrayList<String>()
        val c = db.readableDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("name")))
                c.moveToNext()
            }
        }
        c.close()

        var string = "\n"

        for(item in arrTblNames) {
            string = string + "${item}, \n"
        }

        result.invoke(string)
    }

    // ----------------------------------------
    // LANGUAGES
    fun getLanguageByID(languageID: Long, result: (LanguageModel?) -> Unit) {
        val languages = ArrayList<LanguageModel>()
        val cursor = db.readableDatabase.
            rawQuery("SELECT * FROM ${LanguageModel.TABLE_NAME} WHERE ${LanguageModel.key_id}=${languageID};", null)
        if(cursor.moveToFirst()) {
            do {
                val lang = LanguageModel(
                    id = cursor.getLong(cursor.getColumnIndex(LanguageModel.key_id)),
                    name = cursor.getString(cursor.getColumnIndex(LanguageModel.key_name)),
                    tableWords = cursor.getString(cursor.getColumnIndex(LanguageModel.key_table_words)),
                    tableExerciseFails = cursor.getString(cursor.getColumnIndex(LanguageModel.key_table_exercise_fails)))
                lang.wordsCount = DatabaseUtils.queryNumEntries(db.readableDatabase, lang.tableWords)
                languages.add(lang)
            } while(cursor.moveToNext())
        }
        db.close()
        result.invoke(languages.firstOrNull())
    }

    fun getLanguages(result: (ArrayList<LanguageModel>) -> Unit) {
        val languages = ArrayList<LanguageModel>()
        val cursor = db.readableDatabase.rawQuery("SELECT * FROM ${LanguageModel.TABLE_NAME}", null)
        if(cursor.moveToFirst()) {
            do {
                val lang = LanguageModel(
                    id = cursor.getLong(cursor.getColumnIndex(LanguageModel.key_id)),
                    name = cursor.getString(cursor.getColumnIndex(LanguageModel.key_name)),
                    tableWords = cursor.getString(cursor.getColumnIndex(LanguageModel.key_table_words)),
                    tableExerciseFails = cursor.getString(cursor.getColumnIndex(LanguageModel.key_table_exercise_fails)))
                lang.wordsCount = DatabaseUtils.queryNumEntries(db.readableDatabase, lang.tableWords)

                languages.add(lang)
            } while(cursor.moveToNext())
        }
        db.close()
        result.invoke(languages)
    }

    fun addLanguage(newLanguage: String?, result: (String?) -> Unit) {
        val isValid = validateLanguage(newLanguage)

        if(isValid == null) {

            val language = LanguageModel(
                name = newLanguage!!,
                tableWords = DBUtil.generateUniqueTableName(),
                tableExerciseFails = DBUtil.generateUniqueTableName())

            db.writableDatabase.insert(LanguageModel.TABLE_NAME, null, language.getContentValues())
            createTableWords(language.tableWords)
            createTableExerciseFails(language.tableExerciseFails)
            db.close()
            result.invoke(isValid)
        } else {
            result.invoke(isValid)
        }
    }

    fun updateLanguageName(languageModel: LanguageModel, newName: String?, result: (String?) -> Unit) {
        val isValid = validateLanguage(newName)

        if(isValid == null) {
            languageModel.name = newName!!
            db.writableDatabase.update(
                LanguageModel.TABLE_NAME,
                languageModel.getContentValuesUpdated(),
                "${LanguageModel.key_id} = ?",
                arrayOf(languageModel.id.toString()))
            db.close()
            result.invoke(isValid)
        } else {
            result.invoke(isValid)
        }
    }

    fun deleteLanguage(languageModel: LanguageModel, result: () -> Unit) {
        deleteTableWords(languageModel.tableWords)
        deleteTableExerciseFails(languageModel.tableExerciseFails)
        db.writableDatabase.delete(
            LanguageModel.TABLE_NAME,
            "${LanguageModel.key_id} = ?",
            arrayOf(languageModel.id.toString()))
        db.close()
        result.invoke()
    }

    private fun isLanguageAlreadyExist(language: String) : Boolean {
        try {
            val cursor = db.readableDatabase.rawQuery(
                DBUtil.selectWhereString(context,
                    LanguageModel.TABLE_NAME,
                    LanguageModel.key_name,
                    language), null)
            if(cursor.moveToFirst()) {
                db.close()
                cursor.close()
                return true
            }
            db.close()
            cursor.close()
        } catch (e: Exception) { }
        return false
    }


    private fun validateLanguage(newLanguage: String?) : String? {
        return if(!newLanguage.isNullOrEmpty())
            return if(isLanguageAlreadyExist(newLanguage))
                context.getString(com.vocabulary.R.string.message_language_exist)
            else null
            else context.getString(com.vocabulary.R.string.message_empty_field)
    }

    // ----------------------------------------



    // ----------------------------------------
    // WORDS
    fun getWordsCount(tableName: String, result: (Long) -> Unit) {
        val rdb = db.readableDatabase
        val count = DatabaseUtils.queryNumEntries(rdb, tableName)
        rdb.close()
        result.invoke(count)
    }

    fun createTableWords(tableName: String) {
        db.writableDatabase.execSQL(DBUtil.createTableString(context, tableName, WordModel.TABLE_FIELDS))
//        db.close()
    }

    fun deleteTableWords(tableName: String) {
        db.writableDatabase.execSQL(String.format(context.getString(com.vocabulary.R.string.query_drop_table), tableName))
//        db.close()
    }

    fun addJsonWords(wordTable: String, jsonArr: ArrayList<JsonWord>, result: () -> Unit) {
        for(item in jsonArr) {
            val isValid = validateWord(wordTable, item.word, item.translation)

            if(isValid == null) {
                val wordModel = WordModel(
                    word = item.word,
                    translation = item.translation,
                    tableName = wordTable
                )
                db.writableDatabase.insert(wordModel.tableName, null, wordModel.getContentValues())
            }
        }
        db.close()
        result.invoke()
    }

    fun addWord(wordTable : String, word: String?, translation: String?, result: (String?) -> Unit) {
        val isValid = validateWord(wordTable, word, translation)

        if(isValid == null) {
            val wordModel = WordModel(
                word = word!!,
                translation = translation!!,
                tableName = wordTable
            )

            db.writableDatabase.insert(wordModel.tableName, null, wordModel.getContentValues())
            db.close()
            result.invoke(isValid)
        } else {
            result.invoke(isValid)
        }
    }

    fun updateWord(wordModel: WordModel, newWord: String?, newTranslation: String?, result: (String?) -> Unit) {
        val isValid = validateWord(wordModel.tableName, newWord, newTranslation)

        if(isValid == null) {
            wordModel.word = newWord!!
            wordModel.translation = newTranslation!!
            db.writableDatabase.update(
                wordModel.tableName,
                wordModel.getContentValuesUpdated(),
                "${WordModel.key_id} = ?",
                arrayOf(wordModel.id.toString()))
            db.close()
            result.invoke(isValid)
        } else {
            result.invoke(isValid)
        }
    }

    fun deleteWord(tableFails: String, wordModel: WordModel, result: () -> Unit) {
        deleteExerciseFailListByWordIDs(tableFails,  arrayListOf(wordModel.id)) {}

        db.writableDatabase.delete(
            wordModel.tableName,
            "${WordModel.key_id} = ?",
            arrayOf(wordModel.id.toString()))
        db.close()
        result.invoke()
    }

    fun getAllWords(currentLanguage: LanguageModel, result: (ArrayList<WordModel>) -> Unit) {
        val words = ArrayList<WordModel>()
        val cursor = db.readableDatabase.rawQuery(
            "SELECT * FROM ${currentLanguage.tableWords} ORDER BY ${WordModel.key_word}", null)
        if(cursor.moveToFirst()) {
            do {
                val word = WordModel(
                    id = cursor.getLong(cursor.getColumnIndex(WordModel.key_id)),
                    word = cursor.getString(cursor.getColumnIndex(WordModel.key_word)),
                    translation = cursor.getString(cursor.getColumnIndex(WordModel.key_translate)),
                    tableName = cursor.getString(cursor.getColumnIndex(WordModel.key_table_name))
                )
                words.add(word)

            } while(cursor.moveToNext())
        }
        db.close()
        result.invoke(words)
    }

    fun getRandomLimitedWords(wordsTable: String,
                              limit: String,
                              whereNotEqual: String?,
                              result: (ArrayList<WordModel>) -> Unit) {
        val selectQuary = "SELECT * FROM ${wordsTable} ${
        if(whereNotEqual != null) "WHERE ${whereNotEqual}" else ""
        } ORDER BY RANDOM() LIMIT ${limit}"


        val words = ArrayList<WordModel>()
        val cursor = db.readableDatabase.rawQuery(
            selectQuary, null)


        if(cursor.moveToFirst()) {
            do {
                val word = WordModel(
                    id = cursor.getLong(cursor.getColumnIndex(WordModel.key_id)),
                    word = cursor.getString(cursor.getColumnIndex(WordModel.key_word)),
                    translation = cursor.getString(cursor.getColumnIndex(WordModel.key_translate)),
                    tableName = cursor.getString(cursor.getColumnIndex(WordModel.key_table_name))
                )
                words.add(word)

            } while(cursor.moveToNext())
        }
        db.close()
        result.invoke(words)
    }

    private fun isWordAlreadyExist(tableWords: String, word: String) : Boolean {
        try {
            val cursor = db.readableDatabase.rawQuery(
                DBUtil.selectWhereString(context,
                    tableWords,
                    WordModel.key_word,
                    word), null)
            if(cursor.moveToFirst()) {
                db.close()
                cursor.close()
                return true
            }
            db.close()
            cursor.close()
        } catch (e: Exception) { }
        return false
    }

    private fun validateWord(tableWords: String, newWord: String?, newTranslation: String?) : String? {
        return if(!newWord.isNullOrEmpty() && !newTranslation.isNullOrEmpty())
            return if(isWordAlreadyExist(tableWords, newWord))
                context.getString(com.vocabulary.R.string.message_word_exist)
            else null
        else context.getString(com.vocabulary.R.string.message_empty_field)
    }


    fun searchWords(wordTable: String, searchText: String, result: (ArrayList<WordModel>) -> Unit) {
        val selectQuery = String.format(
            context.getString(R.string.query_search_words),
            wordTable,
            WordModel.key_word,
            searchText,
            WordModel.key_translate,
            searchText,
            WordModel.key_word)

        val words = ArrayList<WordModel>()
        val cursor = db.readableDatabase.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()) {
            do {
                val word = WordModel(
                    id = cursor.getLong(cursor.getColumnIndex(WordModel.key_id)),
                    word = cursor.getString(cursor.getColumnIndex(WordModel.key_word)),
                    translation = cursor.getString(cursor.getColumnIndex(WordModel.key_translate)),
                    tableName = cursor.getString(cursor.getColumnIndex(WordModel.key_table_name))
                )
                words.add(word)

            } while(cursor.moveToNext())
        }
        db.close()
        result.invoke(words)

    }
    // ----------------------------------------


    // ----------------------------------------
    // Exercise Fails
    fun getExerciseFailCount(tableName: String, result: (Long) -> Unit) {
        val rdb = db.readableDatabase
        val count = DatabaseUtils.queryNumEntries(rdb, tableName)
        rdb.close()
        result.invoke(count)
    }

    fun createTableExerciseFails(tableName: String) {
        db.writableDatabase.execSQL(DBUtil.createTableString(context, tableName, ExerciseFailModel.TABLE_FIELDS))
//        db.close()
    }

    fun deleteTableExerciseFails(tableName: String) {
        db.writableDatabase.execSQL(String.format(context.getString(com.vocabulary.R.string.query_drop_table), tableName))
//        db.close()
    }

    fun getExerciseFailWords(tableWords: String,
                             tableFails: String,
                             count: String,
                             result: (ArrayList<WordModel>) -> Unit) {


//        SQLiteException: RIGHT and FULL OUTER JOINs are not currently supported (code 1 SQLITE_ERROR)

        // RIGHT JOINT
//        "SELECT * FROM (SELECT * FROM %s LIMIT %s) RIGHT JOIN %s ON %s = %s"
//        val selectQuery = String.format(
//            context.getString(R.string.query_select_fail_words_right_join),
//            tableFails,
//            count,
//            tableWords,
//            ExerciseFailModel.key_word_id,
//            WordModel.key_id)

        // LEFT JOIN
//        "SELECT * FROM (SELECT * FROM %s ) AS A LEFT JOIN %s AS B ON A.%s = B.%s LIMIT %s"
//        "SELECT * FROM (SELECT * FROM %s ) LEFT JOIN %s ON %s = %s LIMIT %s"
        val selectQuery = String.format(
            context.getString(R.string.query_select_fail_words_left_join),
            tableWords,
            tableFails,
            WordModel.key_id,
            ExerciseFailModel.key_word_id,
            count)




        val words = ArrayList<WordModel>()
        val cursor = db.readableDatabase.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()) {
            do {
                val word = WordModel(
                    id = cursor.getLong(cursor.getColumnIndex(WordModel.key_id)),
                    word = cursor.getString(cursor.getColumnIndex(WordModel.key_word)),
                    translation = cursor.getString(cursor.getColumnIndex(WordModel.key_translate)),
                    tableName = cursor.getString(cursor.getColumnIndex(WordModel.key_table_name))
                )
                words.add(word)

            } while(cursor.moveToNext())
        }
        db.close()
        result.invoke(words)
    }

    fun addExerciseFailList(tableFails: String,
                        wordIDArr: ArrayList<Long>,
                        result: () -> Unit) {
        for(wordID in wordIDArr) {

            val isExist = isWordFailAlreadyExist(tableFails, wordID.toString())

            if (!isExist) {
                val exerciseFailModel = ExerciseFailModel(wordID = wordID)
                db.writableDatabase.insert(tableFails, null, exerciseFailModel.getContentValues())
            }
        }
        db.close()
        result.invoke()
    }

    fun deleteExerciseFailListByWordIDs(tableFails: String,
                           exerciseIDArr: ArrayList<Long>,
                           result: () -> Unit) {
        for(exerciseItem in exerciseIDArr) {
            val isExist = isWordFailAlreadyExist(tableFails, exerciseItem.toString())
            if(isExist) {
                db.writableDatabase.delete(
                    tableFails,
                    "${ExerciseFailModel.key_word_id} = ?",
                    arrayOf(exerciseItem.toString())
                )
            }
        }

        db.close()
        result.invoke()
    }

    private fun isWordFailAlreadyExist(tableFails: String, wordID: String) : Boolean {
        try {
            val cursor = db.readableDatabase.rawQuery(
                DBUtil.selectWhereString(context,
                    tableFails,
                    ExerciseFailModel.key_word_id,
                    wordID), null)
            if(cursor.moveToFirst()) {
                db.close()
                cursor.close()
                return true
            }
            db.close()
            cursor.close()
        } catch (e: Exception) { }
        return false
    }

}
