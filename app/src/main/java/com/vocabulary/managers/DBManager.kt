package com.vocabulary.managers

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import androidx.core.content.ContextCompat
import com.vocabulary.R
import com.vocabulary.db.DBHelper
import com.vocabulary.db.DBUtil
import com.vocabulary.models.LanguageModel
import com.vocabulary.models.TestFailModel
import com.vocabulary.models.TestsModel
import com.vocabulary.models.WordModel
import org.intellij.lang.annotations.Language
import java.lang.Exception
import android.widget.Toast
import java.util.regex.Pattern


class DBManager(private val context: Context) {

    private val TEXT_REGEX = "^[a-zA-Z ]+$"
    private var db: DBHelper
//    private var context: Context

    init {
//        this.context = Injector.application!!
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

//val id = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE)

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
                    tableTestFails = cursor.getString(cursor.getColumnIndex(LanguageModel.key_table_test_fails)))
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
                    tableTestFails = cursor.getString(cursor.getColumnIndex(LanguageModel.key_table_test_fails)))
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
                tableTestFails = DBUtil.generateUniqueTableName())

            db.writableDatabase.insert(LanguageModel.TABLE_NAME, null, language.getContentValues())
            createTableWords(language.tableWords)
//            createTableTests()
            db.close()
            // TODO add language in table
            // TODO create words table
            // TODO create tests table
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
        // TODO delete test table
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

    fun deleteWord(wordModel: WordModel, result: () -> Unit) {
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
                    tableName = cursor.getString(cursor.getColumnIndex(WordModel.key_table_name)))
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



//        return if(!newWord.isNullOrEmpty() && !newTranslation.isNullOrEmpty())
//            return if(Pattern.compile(TEXT_REGEX).matcher(newWord).matches())
//                return if(Pattern.compile(TEXT_REGEX).matcher(newTranslation).matches())
//                    return if(isWordAlreadyExist(tableWords, newWord))
//                        context.getString(com.vocabulary.R.string.message_word_exist)
//                    else null
//                 else // TODO return string error
//            else      // TODO return string error
//         else context.getString(com.vocabulary.R.string.message_empty_field)


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
                    tableName = cursor.getString(cursor.getColumnIndex(WordModel.key_table_name)))
                words.add(word)

            } while(cursor.moveToNext())
        }
        db.close()
        result.invoke(words)

    }
    // ----------------------------------------


    // ----------------------------------------
    // TESTS
    fun createTableTests() {
        db.writableDatabase.execSQL(DBUtil.createTableString(context, TestsModel.TABLE_NAME, TestsModel.TABLE_FIELDS))
        db.close()
    }

    fun deleteTableTests() {
        db.writableDatabase.execSQL(String.format(context.getString(com.vocabulary.R.string.query_drop_table), TestsModel.TABLE_NAME))
        db.close()
    }

    fun addTest() {
        //TODO
    }

    fun updateTest() {
        //TODO
    }

    fun deleteTest() {
        //TODO
    }
    // ----------------------------------------

    // ----------------------------------------
    // TEST Fails
//    fun createTableTestFails(tableName: String) {
//        db.writableDatabase.execSQL(DBUtil.createTableString(context, TestFailModel.TABLE_NAME, TestFailModel.TABLE_FIELDS))
//        db.close()
//    }
//
//    fun deleteTableTestFails(tableName: String) {
//        db.writableDatabase.execSQL(String.format(context.getString(R.string.query_drop_table), TestFailModel.TABLE_NAME))
//        db.close()
//    }

    fun addFails() {
        //TODO
    }

    fun deleteFails() {
        //TODO
    }
    // ----------------------------------------




}
