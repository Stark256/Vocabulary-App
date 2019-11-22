package com.vocabulary.db.models

import com.vocabulary.db.DBField
import com.vocabulary.db.FieldType

class LanguageModel(
    val id: Int,
    val name: String,
    val tableWords: String,
    val tableTestFails: String
) {
    companion object {

        val TABLE_NAME = "TABLE_ALL_LANGUAGES"

        val key_id = "id"
        val key_name = "name"
        val key_table_words = "table_words"
        val key_table_test_fails = "table_test_fails"

        val type_id = FieldType.PRIMARY_KEY
        val type_name = FieldType.TEXT
        val type_table_words = FieldType.TEXT
        val type_table_test_fails = FieldType.TEXT


        val TABLE_FIELDS: ArrayList<DBField> = arrayListOf(
            DBField(key_id, type_id),
            DBField(key_name, type_name),
            DBField(key_table_words, type_table_words),
            DBField(key_table_test_fails, type_table_test_fails)
        )
    }
}


