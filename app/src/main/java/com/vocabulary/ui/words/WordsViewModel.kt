package com.vocabulary.ui.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.customViews.sort_sett_view.SortSettView
import com.vocabulary.managers.Injector
import com.vocabulary.models.LetterModel
import com.vocabulary.models.WordBaseItem
import com.vocabulary.models.WordModel

class WordsViewModel : ViewModel() {

    val initializingView = MutableLiveData<WordInitType>()
    var selectedSortSett: SortSettView.SORT_SETT = SortSettView.SORT_SETT.SORT_A_Z
    val words = MutableLiveData<ArrayList<WordBaseItem>>()
    val showBadge = MutableLiveData<Boolean>()
    val filters = ArrayList<LetterModel>()

    private val allWords: ArrayList<WordModel> = ArrayList()


    private fun filterWords(isInit: Boolean) {
        if(isInit) {
            var oldFilters : ArrayList<LetterModel>? = null
            if(this.filters.isNotEmpty()) {
                oldFilters = ArrayList()
                oldFilters.addAll(this.filters)
            }
            this.filters.clear()

            var letter = ""
            for (item in allWords) {
                val wordStart = item.word.first().toString()
                if (wordStart != letter) {
                    val letterModel = LetterModel(wordStart)

                    if(oldFilters != null) {
                        val oldFilter : LetterModel? =
                            oldFilters.find { it.letter.first() == letterModel.letter.first() }
                        if(oldFilter != null) {
                            letterModel.isSelected = oldFilter.isSelected
                        }
                    }

                    filters.add(letterModel)
                    letter = wordStart
                }
            }
        }
        var isDefaultFilter = true

        if(selectedSortSett == SortSettView.SORT_SETT.SORT_A_Z) {
            allWords.sortBy { it.getSortString() }
            filters.sortBy { it.getSortString() }
        } else {
            allWords.sortByDescending { it.getSortString() }
            filters.sortByDescending { it.getSortString() }
            isDefaultFilter = false
        }

        val filteredWords  = ArrayList<WordBaseItem>()

        for(filter in filters) {
            if(filter.isSelected) {
                filteredWords.add(filter)
                val wordsFilter = getSortedWordsByLetter(filter.letter)
                filteredWords.addAll(wordsFilter)
            } else {
                if(isDefaultFilter) {
                    isDefaultFilter = false
                }
            }
        }

        this.showBadge.value = !isDefaultFilter

        this.words.value = filteredWords
        if(filteredWords.isEmpty()) {
            initializingView.value = WordInitType.FILTER_EMPTY
        } else {
            initializingView.value = WordInitType.WORDS_NOT_EMPTY
        }


    }

    private fun getSortedWordsByLetter(letter: String) : List<WordModel> {

        val words = allWords.filter { it.getSortString().first() == letter.first() }

        if(selectedSortSett == SortSettView.SORT_SETT.SORT_A_Z) {
             words.sortedBy { it.word }
        } else {
            words.sortedByDescending { it.word }
        }
        return words
    }

    fun updateIfNeeded() {
        if(Injector.languageManager.isNeedUpdate) {
            Injector.languageManager.resetNeedUpdate()
            getWords()
        }
    }

    private fun initWordsList(arr: ArrayList<WordModel>) {
        this.allWords.clear()
        this.allWords.addAll(arr)

//        val adapterList = ArrayList<WordBaseItem>()
//        var letter = ""
//
//        for(item in arr) {
//            val wordStart = item.word.first().toString()
//            if(wordStart != letter) {
//                val letterModel = LetterModel(wordStart)
//                adapterList.add(letterModel)
//                filters.add(letterModel)
//                letter = wordStart
//            }
//            adapterList.add(item)
//        }

        //this.allWords.addAll(adapterList)
        filterWords(true)
    }

    fun getWords() {
        initializingView.value = WordInitType.WORDS_LOADING
        val currentLanguage =
            Injector.languageManager.getCurrentLanguageIfSelected()
        if(currentLanguage != null) {
            Injector.dbManager.getAllWords(currentLanguage) {
                if(it.isNotEmpty()) {
                    initWordsList(it)
                } else {
                    filters.clear()
                    initializingView.value = WordInitType.WORDS_EMPTY
                    words.value = ArrayList()
                }
            }
        } else {
            initializingView.value = WordInitType.LANGUAGE_NOT_SELECTED
            words.value = ArrayList()
        }
    }

    fun setFilterData(sortSett: SortSettView.SORT_SETT, filters: ArrayList<LetterModel>) {
        initializingView.value = WordInitType.WORDS_LOADING
        if(allWords.isNotEmpty()) {
            this.selectedSortSett = sortSett
            if(filters.isNotEmpty()) {
                this.filters.clear()
                this.filters.addAll(filters)
            }
            filterWords(false)
        } else {
            initializingView.value = WordInitType.WORDS_EMPTY
        }
    }

    fun resetFilters() {
        initializingView.value = WordInitType.WORDS_LOADING
        if(allWords.isNotEmpty()) {
            for (filter in filters) {
                filter.isSelected = true
            }
            this.selectedSortSett = SortSettView.SORT_SETT.SORT_A_Z
            filterWords(false)
        } else {
            initializingView.value = WordInitType.WORDS_EMPTY
        }
    }

    fun addEditWord(
        wordModel: WordModel? = null,
        newWord: String? = null,
        newTranslation: String? = null,
        result: (String?) -> Unit) {

        if(wordModel != null && newWord != null && newTranslation != null) {
            editWord(wordModel, newWord, newTranslation, result)
        } else if(newWord != null && newTranslation != null) {
            addWord(newWord, newTranslation, result)
        }
    }

    private fun addWord(
        newWord: String?,
        newTranslation: String?,
        result: (String?) -> Unit) {

        val currentLanguage = Injector.languageManager.getCurrentLanguageIfSelected()

        if(currentLanguage != null) {
            Injector.dbManager.addWord(
                currentLanguage.tableWords,
                newWord, newTranslation) { res ->

                result.invoke(res)
                if(res == null) {
                    getWords()
                }
            }
        }
    }

    private fun editWord(
        wordModel: WordModel,
        newWord: String?,
        newTranslation: String?,
        result: (String?) -> Unit) {
        Injector.dbManager.updateWord(
            wordModel,
            newWord,
            newTranslation) { res ->

            result.invoke(res)
            if(res == null) {
                getWords()
            }

        }
    }

    fun deleteWord(wordModel: WordModel, result: () -> Unit) {
        Injector.dbManager.deleteWord(wordModel) {
            result.invoke()
            getWords()
        }
    }

    enum class WordInitType {
        WORDS_EMPTY,
        WORDS_NOT_EMPTY,
        WORDS_LOADING,
        LANGUAGE_NOT_SELECTED,
        FILTER_EMPTY
    }

}