package com.vocabulary.models

import com.vocabulary.db.DBField
import com.vocabulary.db.FieldType

class TestsModel (
    val id: Int,
    val languageID: Int,
    val type: Int,
    val words_size: Int
) {
    companion object {

        val TABLE_NAME = "TABLE_ALL_TESTS"

        val key_id = "id"
        val key_languageID = "languageID"
        val key_type = "type"
        val key_words_size = "words_size"

        val type_id = FieldType.PRIMARY_KEY
        val type_languageID = FieldType.NUMBER
        val type_type = FieldType.NUMBER
        val type_words_size = FieldType.NUMBER


        val TABLE_FIELDS: ArrayList<DBField> = arrayListOf(
            DBField(key_id, type_id),
            DBField(key_languageID, type_languageID),
            DBField(key_type, type_type),
            DBField(key_words_size, type_words_size)
        )
    }
}
