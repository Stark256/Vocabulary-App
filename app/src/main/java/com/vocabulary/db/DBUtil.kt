package com.vocabulary.db

import android.content.Context
import androidx.core.content.ContextCompat
import com.vocabulary.R

object DBUtil {

    fun createTableString(context: Context, tableName: String, fields: ArrayList<DBField>) : String {
        var fieldsString = ""

        for(i in 0 until fields.lastIndex) {
            val field = fields[i]
            fieldsString += "${field.key} ${field.type.value}"
            if(i != fields.lastIndex) {
                fieldsString += ","
            }
        }


//        String.format(resources.getString(R.string.general_sort_param),text)
        return String.format(context.getString(R.string.query_create_table), tableName, fieldsString)
    }


}
