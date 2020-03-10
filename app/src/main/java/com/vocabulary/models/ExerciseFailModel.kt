package com.vocabulary.models

import android.content.ContentValues
import com.vocabulary.db.DBField
import com.vocabulary.db.FieldType

class ExerciseFailModel (
    val id: Long = 0,
    val wordID: Long
) {

    fun getContentValues() : ContentValues {
        return ContentValues().apply {
            put(key_word_id, wordID)
        }
    }

    companion object {

//        val TABLE_NAME = "_GUESS"

        val key_id = "id"
        val key_word_id = "wordID"

        val type_id = FieldType.PRIMARY_KEY
        val type_word_id = FieldType.NUMBER


        val TABLE_FIELDS: ArrayList<DBField> = arrayListOf(
            DBField(
                key_id,
                type_id
            ),
            DBField(
                key_word_id,
                type_word_id
            )
        )
    }
    fun adsad() {

        "SELECT * FROM %s (SELECT * FROM %s LIMIT %s) RIGHT JOIN %s ON %s = %s"


        "SELECT Orders.OrderID, Employees.LastName, " +
                "Employees.FirstName FROM Orders RIGHT JOIN Employees ON Orders.EmployeeID = Employees.EmployeeIDORDER " +
                "BY Orders.OrderID"






    }


}
