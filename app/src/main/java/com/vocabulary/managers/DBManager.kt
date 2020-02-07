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



class DBManager(private val context: Context) {

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

    fun updateLanguageName(languageModel: LanguageModel, newName: String, result: (String?) -> Unit) {
        val isValid = validateLanguage(newName)

        if(isValid == null) {
            languageModel.name = newName
            db.writableDatabase.update(
                LanguageModel.TABLE_NAME,
                languageModel.getContentValueName(),
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

    fun addWord(word: WordModel) {
        db.writableDatabase.insert(word.tableName, null, word.getContentValues())
        db.close()
    }

    fun updateWord() {
        //TODO
    }

    fun deleteWord() {
        //TODO
    }

    fun getAllWords() {
        //TODO
    }

    fun searchWords() {
        //TODO
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
