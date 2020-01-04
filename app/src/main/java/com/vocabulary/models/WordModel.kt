package com.vocabulary.models

import android.content.ContentValues
import com.vocabulary.db.DBField
import com.vocabulary.db.FieldType
import java.io.Serializable

class WordModel(
    var id: Int = 0,
    var word: String,
    var translate: String,
    var tableName: String
) : WordBaseItem(), Serializable {

    fun getContentValues() : ContentValues {
       return ContentValues().apply {
        put(key_word, word)
        put(key_translate, translate)
       }
    }

    override fun getType(): WordItemType = WordItemType.TYPE_WORD
    companion object {

        val key_id = "id"
        val key_word = "word"
        val key_translate = "translate"
        val key_table_name = "table_name"

        val type_id = FieldType.PRIMARY_KEY
        val type_word = FieldType.TEXT
        val type_translate = FieldType.TEXT
        val type_table_name = FieldType.TEXT


        val TABLE_FIELDS: ArrayList<DBField> = arrayListOf(
            DBField(key_id, type_id),
            DBField(key_word, type_word),
            DBField(key_translate, type_translate),
            DBField(key_table_name, type_table_name)
        )


    }
}
