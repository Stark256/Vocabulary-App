package com.vocabulary.managers

import android.content.Context
import com.vocabulary.R
import com.vocabulary.db.DBHelper
import com.vocabulary.db.DBUtil
import com.vocabulary.models.LanguageModel
import com.vocabulary.models.TestFailModel
import com.vocabulary.models.TestsModel
import com.vocabulary.models.WordModel

class DBManager(private val context: Context) {

    private var db: DBHelper
//    private var context: Context

    init {
//        this.context = Injector.application!!
        this.db = DBHelper(context)
    }


//val id = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE)

    // ----------------------------------------
    // WORDS
    fun addLanguage(language: LanguageModel) {
        db.writableDatabase.insert(LanguageModel.TABLE_NAME, null, language.getContentValues())
    }

    fun updateLanguage() {

    }

    fun deleteLanguage() {

    }


    // ----------------------------------------



    // ----------------------------------------
    // WORDS
    fun createTableWords() {
        db.writableDatabase.execSQL(DBUtil.createTableString(context, WordModel.TABLE_NAME, WordModel.TABLE_FIELDS))
    }

    fun deleteTableWords() {
        db.writableDatabase.execSQL(String.format(context.getString(R.string.query_drop_table), WordModel.TABLE_NAME))
    }

    fun addWord(word: WordModel) {
        db.writableDatabase.insert(WordModel.TABLE_NAME, null, word.getContentValues())

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
    }

    fun deleteTableTests() {
        db.writableDatabase.execSQL(String.format(context.getString(R.string.query_drop_table), TestsModel.TABLE_NAME))
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
    fun createTableTestFails() {
        db.writableDatabase.execSQL(DBUtil.createTableString(context, TestFailModel.TABLE_NAME, TestFailModel.TABLE_FIELDS))
    }

    fun deleteTableTestFails() {
        db.writableDatabase.execSQL(String.format(context.getString(R.string.query_drop_table), TestFailModel.TABLE_NAME))
    }

    fun addFails() {
        //TODO
    }

    fun deleteFails() {
        //TODO
    }
    // ----------------------------------------




}
