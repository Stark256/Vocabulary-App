package com.vocabulary.utils

import android.content.Context
import com.vocabulary.models.JsonWord
import org.json.JSONObject
import java.lang.Exception

object JsonUtils {

    fun loadJson(context: Context) : String {
        var resultJson: String = ""
        try {
            val inputS = context.assets.open("words_to_add.json")
            val buffer = ByteArray(inputS.available())
            inputS.read(buffer)
            inputS.close()
            resultJson = String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultJson
    }

    fun parseJson(json: String) : ArrayList<JsonWord> {
        val resultArr = ArrayList<JsonWord>()
        try {
            val jsonObj = JSONObject(json)
            val jsonArr = jsonObj.getJSONArray("words_to_add")

            for(index in 0 until jsonArr.length()) {
                val item = jsonArr.getJSONObject(index)
                val itemWord = item.getString("word")
                val itemTranslation = item.getString("translation")

                if(itemWord.isNotBlank() && itemTranslation.isNotBlank()) {
                    resultArr.add(JsonWord(itemWord, itemTranslation))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultArr
    }
}