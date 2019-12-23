package com.vocabulary.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.models.LetterModel
import com.vocabulary.models.WordBaseItem
import com.vocabulary.models.WordModel

class WordsViewModel : ViewModel() {

    val words = MutableLiveData<ArrayList<WordBaseItem>>()
    val filters = ArrayList<LetterModel>()


    private val dbWords: ArrayList<WordModel> = arrayListOf(
        WordModel(0, "atest1", "aтест1"),
        WordModel(1, "atest2", "aтест2"),
        WordModel(2, "atest3", "aтест3"),
        WordModel(3, "btest4", "bтест4"),
        WordModel(4, "btest5", "bтест5"),
        WordModel(5, "ctest6", "cтест6"),
        WordModel(6, "dtest7", "dтест7"),
        WordModel(7, "dtest8", "dтест8"),
        WordModel(0, "etest9", "eтест9"),
        WordModel(0, "etest10", "eтест10")
    )

    fun loadWords() {
        val adapterList = ArrayList<WordBaseItem>()
        var letter = ""

        for(item in dbWords) {
            val wordStart = item.word.first().toString()
            if(wordStart != letter) {
                val letterModel = LetterModel(wordStart)
                adapterList.add(letterModel)
                filters.add(letterModel)
                letter = wordStart
            }

            adapterList.add(item)
        }

        words.value = adapterList
    }

    fun filterWords() {
        val newList = ArrayList<WordBaseItem>()



        for(wordBase in words.value!!) {



        }
    }

    fun setFilters(filters: ArrayList<LetterModel>) {
        if(filters.isNotEmpty()) {
            this.filters.clear()
            this.filters.addAll(filters)
            // TODO reload words
        }
    }

    fun resetFilters() {
        filters.clear()
        // TODO reload words
    }

}