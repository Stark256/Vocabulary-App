package com.vocabulary.models

import android.content.ContentValues
import com.vocabulary.db.DBField
import com.vocabulary.db.FieldType

class WordModel(
    val id: Int,
    val word: String,
    val translate: String
) : WordBaseItem() {

    fun getContentValues() : ContentValues {
        val values = ContentValues()
//        values.put(key_id, id)
        values.put(key_word, word)
        values.put(key_translate, translate)
        return values
    }

    override fun getType(): WordItemType = WordItemType.TYPE_WORD
    companion object {

        val TABLE_NAME = "TABLE_LANGUAGE_"

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
