package com.vocabulary.db

import android.content.Context
import androidx.core.content.ContextCompat
import com.vocabulary.R
import java.util.*
import kotlin.collections.ArrayList

object DBUtil {

    fun generateUniqueTableName() : String {
        return "t" + UUID.randomUUID().toString().replace("-","")
    }

    fun createTableString(context: Context, tableName: String, fields: ArrayList<DBField>) : String {
        var fieldsString = ""

        for(i in 0..fields.lastIndex) {
            val field = fields[i]
            fieldsString += "${field.key} ${field.type.value}"
            if(i != fields.lastIndex) {
                fieldsString += ","
            }
        }


//        String.format(resources.getString(R.string.general_sort_param),text)
        return String.format(context.getString(R.string.query_create_table), tableName, fieldsString)
    }

    fun selectWhereString(context: Context, tableName: String, columnWhere: String, whereValue: String) : String {
        return String.format(context.getString(R.string.query_select_where), tableName, columnWhere, whereValue)
        //"SELECT * FROM ${LanguageModel.TABLE_NAME} WHERE ${LanguageModel.key_name}='${language}'"
    }
}
