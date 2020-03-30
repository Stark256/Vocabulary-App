package com.vocabulary.ui.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.customViews.sort_sett_view.SortSettView
import com.vocabulary.managers.Injector
import com.vocabulary.models.word_models.LetterModel
import com.vocabulary.models.word_models.WordBaseItem
import com.vocabulary.models.word_models.WordModel
import java.util.regex.Pattern

class WordsViewModel : ViewModel() {

    val viewState = MutableLiveData<WordInitType>()
    var selectedSortSett: SortSettView.SORT_SETT = SortSettView.SORT_SETT.SORT_A_Z
    val words = MutableLiveData<ArrayList<WordBaseItem>>()
    val showBadge = MutableLiveData<Boolean>()
    val filters = ArrayList<LetterModel>()
    private var needToUpdateAfterSearch = false


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
                    val letterModel =
                        LetterModel(wordStart)

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
            viewState.value = WordInitType.FILTER_EMPTY
        } else {
            viewState.value = WordInitType.WORDS_NOT_EMPTY
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

    fun searchWords(searchText: String) {
        viewState.value = WordInitType.WORDS_LOADING
        this.needToUpdateAfterSearch = true
        val currentLanguage = Injector.languageManager.getCurrentLanguageIfSelected()

        if(currentLanguage != null) {
            Injector.dbManager.searchWords(
                currentLanguage.tableWords,
                replaceWithPattern(searchText)) { res ->

                if(res.isNotEmpty()) {
                    viewState.value = WordInitType.SEARCH_NOT_EMPTY
                    this.words.value = generateWordsListWithLetters(res)
                } else {
                    viewState.value = WordInitType.SEARCH_EMPTY
                }
            }
        }
    }

    fun updateAfterSearch() {
        if(needToUpdateAfterSearch) {
            needToUpdateAfterSearch = false
            filterWords(true)
        }
    }

    private fun initWordsList(arr: ArrayList<WordModel>) {
        this.allWords.clear()
        this.allWords.addAll(arr)

        filterWords(true)
    }

    fun getWords() {
        viewState.value = WordInitType.WORDS_LOADING
        val currentLanguage =
            Injector.languageManager.getCurrentLanguageIfSelected()
        if(currentLanguage != null) {
            Injector.dbManager.getAllWords(currentLanguage) {
                if(it.isNotEmpty()) {
                    initWordsList(it)
                } else {
                    filters.clear()
                    viewState.value = WordInitType.WORDS_EMPTY
                    words.value = ArrayList()
                }
            }
        } else {
            viewState.value = WordInitType.LANGUAGE_NOT_SELECTED
            words.value = ArrayList()
        }
    }

    private fun generateWordsListWithLetters(words: ArrayList<WordModel>) : ArrayList<WordBaseItem> {
        val searchArr = ArrayList<WordBaseItem>()
        var letter = ""

        for(item in words) {
            val wordStart = item.word.first().toString()
            if(wordStart != letter) {
                val letterModel =
                    LetterModel(wordStart)
                searchArr.add(letterModel)
                filters.add(letterModel)
                letter = wordStart
            }
            searchArr.add(item)
        }

        return searchArr
    }

    fun setFilterData(sortSett: SortSettView.SORT_SETT, filters: ArrayList<LetterModel>) {
        viewState.value = WordInitType.WORDS_LOADING
        if(allWords.isNotEmpty()) {
            this.selectedSortSett = sortSett
            if(filters.isNotEmpty()) {
                this.filters.clear()
                this.filters.addAll(filters)
            }
            filterWords(false)
        } else {
            viewState.value = WordInitType.WORDS_EMPTY
        }
    }

    fun resetFilters() {
        viewState.value = WordInitType.WORDS_LOADING
        if(allWords.isNotEmpty()) {
            for (filter in filters) {
                filter.isSelected = true
            }
            this.selectedSortSett = SortSettView.SORT_SETT.SORT_A_Z
            filterWords(false)
        } else {
            viewState.value = WordInitType.WORDS_EMPTY
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
        Injector.languageManager.getCurrentLanguageIfSelected()?.let {
            Injector.dbManager.deleteWord(it.tableExerciseFails, wordModel) {
                result.invoke()
                getWords()
            }
        }
    }

    enum class WordInitType {
        WORDS_EMPTY,
        WORDS_NOT_EMPTY,
        WORDS_LOADING,
        LANGUAGE_NOT_SELECTED,
        FILTER_EMPTY,
        SEARCH_NOT_EMPTY,
        SEARCH_EMPTY
    }

    private fun replaceWithPattern(string: String) : String {
        val ptn = Pattern.compile("\\s+")
        val mtch = ptn.matcher(string.trim().toLowerCase())
        return mtch.replaceAll(" ")
    }

}