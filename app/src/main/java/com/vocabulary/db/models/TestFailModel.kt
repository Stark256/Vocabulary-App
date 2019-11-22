package com.vocabulary.db.models

import com.vocabulary.db.DBField
import com.vocabulary.db.FieldType

class TestFailModel(
    val id: Int,
    val word: String,
    val translate: String,
    val wrongTranslate1: String,
    val wrongTranslate2: String,
    val wrongTranslate3: String,
    var isFailed: Boolean = false
) {
    companion object {

        val TABLE_NAME = "TABLE_TEST_FAILS_"

        val key_id = "id"
        val key_word = "word"
        val key_translate = "translate"

        val type_id = FieldType.PRIMARY_KEY
        val type_word = FieldType.TEXT
        val type_table_translate = FieldType.TEXT


        val TABLE_FIELDS: ArrayList<DBField> = arrayListOf(
            DBField(key_id, type_id),
            DBField(key_word, type_word),
            DBField(key_translate, type_table_translate)
        )
    }
}
