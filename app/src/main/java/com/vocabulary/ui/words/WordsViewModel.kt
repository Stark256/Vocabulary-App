package com.vocabulary.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.models.LetterModel
import com.vocabulary.models.WordBaseItem
import com.vocabulary.models.WordModel

class WordsViewModel : ViewModel() {

    val words = MutableLiveData<ArrayList<WordBaseItem>?>()
    val isLoading = MutableLiveData<Boolean>()
    val filters = ArrayList<LetterModel>()


    private val dbWords: ArrayList<WordModel> = arrayListOf(
        WordModel(0, "atest1", "aтест1", "test"),
        WordModel(1, "atest2", "aтест2", "test"),
        WordModel(2, "atest3", "aтест3", "test"),
        WordModel(3, "btest4", "bтест4", "test"),
        WordModel(4, "btest5", "bтест5", "test"),
        WordModel(5, "ctest6", "cтест6", "test"),
        WordModel(6, "dtest7", "dтест7", "test"),
        WordModel(7, "dtest8", "dтест8", "test"),
        WordModel(8, "etest9", "eтест9", "test"),
        WordModel(9, "etest10", "eтест10", "test")
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

    fun addEditWord(
        wordModel: WordModel? = null,
        newWord: String? = null,
        newTranslation: String? = null,
        result: (String?) -> Unit) {

        if(wordModel != null && newWord != null && newTranslation != null) {
            // TODO edit word
        } else if(newWord != null && newTranslation != null) {
            // TODO add new word
        }

    }

    fun deleteWord(wordModel: WordModel, result: () -> Unit) {
        // TODO delete word from db
    }

}