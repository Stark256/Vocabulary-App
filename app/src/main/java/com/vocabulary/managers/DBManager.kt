package com.vocabulary.managers

import android.content.Context
import com.vocabulary.R
import com.vocabulary.db.DBField
import com.vocabulary.db.DBHelper
import com.vocabulary.db.DBUtil
import com.vocabulary.db.FieldType
import com.vocabulary.db.models.TestFailModel
import com.vocabulary.db.models.TestsModel
import com.vocabulary.db.models.WordModel

class DBManager() {

    private lateinit var db: DBHelper
    private lateinit var context: Context
    init {
        this.context = Injector.application!!
        this.db = DBHelper(context)
    }


//val id = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE)

    // ----------------------------------------
    // WORDS
    fun createTableWords() {
        db.writableDatabase.execSQL(DBUtil.createTableString(context, WordModel.TABLE_NAME, WordModel.TABLE_FIELDS))
    }

    fun deleteTableWords() {
        db.writableDatabase.execSQL(String.format(context.getString(R.string.query_drop_table), WordModel.TABLE_NAME))
    }

    fun addWord() {
        //TODO
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
