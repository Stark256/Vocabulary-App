package com.vocabulary.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vocabulary.R
import com.vocabulary.models.LanguageModel
import com.vocabulary.models.TestsModel

class DBHelper(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "VOCABULARY_DATABASE"
        private val DATABASE_VERSION = 1
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DBUtil.createTableString(context, LanguageModel.TABLE_NAME, LanguageModel.TABLE_FIELDS))
        db.execSQL(DBUtil.createTableString(context, TestsModel.TABLE_NAME, TestsModel.TABLE_FIELDS))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(String.format(context.getString(R.string.query_drop_table), LanguageModel.TABLE_NAME))
        db.execSQL(String.format(context.getString(R.string.query_drop_table), TestsModel.TABLE_NAME))
    }
}
