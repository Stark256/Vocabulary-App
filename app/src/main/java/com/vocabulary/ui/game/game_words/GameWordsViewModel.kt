package com.vocabulary.ui.game.game_words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.managers.Injector
import com.vocabulary.models.*
import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.game_words_models.GameWordsListModel
import com.vocabulary.models.word_models.WordModel

class GameWordsViewModel : ViewModel() {

    val game = MutableLiveData<GameWordsListModel>()
    val tips = MutableLiveData<ArrayList<Long>>()
    val screenTitle = MutableLiveData<String>()
    val loadingPercent = MutableLiveData<Int>()
    val viewState = MutableLiveData<GameWordsViewState>()
    val buttonNextState = MutableLiveData<GameWordsButtonNextState>()
    val showFinishDialog = MutableLiveData<ArrayList<GameResult>>()

    var showTipsButton = true
    var finishImmediate = true

    private val exerciseResult : ExerciseResult = ExerciseResult()
    private val gamesList = ArrayList<GameWordsListModel>()
    private var selectedGameWordItem: GameWordItemModel? = null
    private val showedTips = ArrayList<Long>()

    private var wordsCount: Long? = null
    private var itemToGuess: Long? = null
    private var currentGame: Int = -1

    fun setInitExtras(wordsCount: Long, itemToGuess: Long) {
        if(wordsCount != -1L) this.wordsCount = wordsCount
        if(itemToGuess != -1L) this.itemToGuess = itemToGuess
    }

    fun loadGames() {
        gamesList.clear()
        viewState.value = GameWordsViewState.STATE_LOADING

        val currentLanguage = Injector.languageManager.getCurrentLanguageIfSelected()

        safeLet(currentLanguage, wordsCount, itemToGuess) {
                languageModel, countW, countG ->
            screenTitle.value = "${currentGame+1} / ${countW}"
            Injector.dbManager.getExerciseFailCount(languageModel.tableExerciseFails)
            { countF ->

                if(countF == 0L) {
                    Injector.dbManager.getRandomLimitedWords(
                        languageModel.tableWords,
                        countW.toString(),
                        null) { wordsList ->

                        generateGamesList(ArrayList(), wordsList, countG)
                    }

                } else if(countW / 2 > countF) {

                    Injector.dbManager.getExerciseFailWords(
                        languageModel.tableWords,
                        languageModel.tableExerciseFails,
                        countF.toString()) {
                        val failsWords = it

                        generateWhereNotEqual(failsWords) { wheneNotEqual ->
                            Injector.dbManager.getRandomLimitedWords(
                                languageModel.tableWords,
                                countW.minus(countF).toString(),
                                wheneNotEqual) { wordList ->

                                generateGamesList(failsWords, wordList, countG)
                            }
                        }
                    }

                } else if(countW / 2 < countF || countW / 2 == countF) {

                    Injector.dbManager.getExerciseFailWords(
                        languageModel.tableWords,
                        languageModel.tableExerciseFails,
                        (countW / 2L).toString()) {
                        val failsWords = it

                        generateWhereNotEqual(failsWords) { wheneNotEqual ->
                            Injector.dbManager.getRandomLimitedWords(
                                languageModel.tableWords,
                                (countW / 2L).toString(),
                                wheneNotEqual) { wordList ->

                                generateGamesList(failsWords, wordList, countG)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun generateWhereNotEqual(arr: ArrayList<WordModel>, result: (String) -> Unit) {
        var resultString: String = ""

        for(index in 0..arr.lastIndex) {
            val item = arr[index]
            resultString += "${WordModel.key_id} != ${item.id}"

            if(index != arr.lastIndex) {
                resultString += " AND "
            }
        }
        result.invoke(resultString)
    }

    private fun generateGamesList(
        failsArr: ArrayList<WordModel>,
        wordsArr: ArrayList<WordModel>,
        itemToGuess: Long) {
        val jointGameList = ArrayList<WordModel>()
        if(failsArr.isNotEmpty()) jointGameList.addAll(failsArr)
        if(wordsArr.isNotEmpty()) jointGameList.addAll(wordsArr)
//        gamesList.clear()

        if(jointGameList.size == itemToGuess.toInt()) {

            for (word in jointGameList) {
                jointGameList.getAllExcept(word.id)
                { wordsForGuess ->
                    val arrGameWordItemModel = ArrayList<GameWordItemModel>()
                    arrGameWordItemModel.add(GameWordItemModel(word.id, true, word.word))

                    for (wordItemGuess in wordsForGuess) {
                        arrGameWordItemModel.add(
                            GameWordItemModel(
                                wordItemGuess.id,
                                false,
                                wordItemGuess.word
                            )
                        )
                    }

                    arrGameWordItemModel.shuffle()
                    gamesList.add(
                        GameWordsListModel(
                            word,
                            word.translation,
                            arrGameWordItemModel
                        )
                    )

                    loadingPercent.value = ((100 / jointGameList.size) * gamesList.size)
                }
            }
        } else {
            for (word in jointGameList) {
                jointGameList.randomListExcept(
                    word.id, itemToGuess.toInt() - 1)
                { wordsForGuess ->

                    val arrGameWordItemModel = ArrayList<GameWordItemModel>()
                    arrGameWordItemModel.add(GameWordItemModel(word.id, true, word.word))

                    for (wordItemGuess in wordsForGuess) {

                        arrGameWordItemModel.add(
                            GameWordItemModel(
                                wordItemGuess.id,
                                false,
                                wordItemGuess.word
                            )
                        )
                    }

                    arrGameWordItemModel.shuffle()
                    gamesList.add(
                        GameWordsListModel(
                            word,
                            word.translation,
                            arrGameWordItemModel
                        )
                    )
                    loadingPercent.value = ((100 / jointGameList.size) * gamesList.size)
                }
            }
        }
        gamesList.shuffle()
        viewState.value = GameWordsViewState.STATE_READY
    }

    fun startPressed() {
        viewState.value = GameWordsViewState.STATE_STARTED
        nextPressed()
    }

    fun onGameWordSelected(gameWordItemModel: GameWordItemModel?) {
        if(gameWordItemModel != null) {
            selectedGameWordItem = gameWordItemModel
            buttonNextState.value = GameWordsButtonNextState.BS_ENABLED_CHECK

        } else {
            selectedGameWordItem = null
            checkPressed()
        }
    }

    fun checkPressed() {
        buttonNextState.value =
            if(wordsCount != null && wordsCount!!.toInt() == currentGame+1)
            GameWordsButtonNextState.BS_FINISH
            else GameWordsButtonNextState.BS_NEXT

        exerciseResult.setGameWordsResult(gamesList[currentGame].correctWord, selectedGameWordItem)
    }

    fun finishPressed() {
        Injector.languageManager.getCurrentLanguageIfSelected()?.let {
            currentLanguage ->

            Injector.dbManager.deleteExerciseFailListByWordIDs(
                currentLanguage.tableExerciseFails,
                exerciseResult.getCorrectResultWordIDs()) {

                Injector.dbManager.addExerciseFailList(
                    currentLanguage.tableExerciseFails,
                    exerciseResult.failsModelList) {

                    showFinishDialog.value = exerciseResult.resultList
                }
            }
        }
    }

    fun nextPressed() {

        if(finishImmediate) {
            finishImmediate = false
        }
        showedTips.clear()
        buttonNextState.value = GameWordsButtonNextState.BS_NOT_ENABLED_CHECK
        selectedGameWordItem = null
        currentGame++
        game.value = gamesList[currentGame]
        screenTitle.value = "${currentGame+1} / ${gamesList.size}"
    }

    fun tipsPressed() {
        val newShuffledList = gamesList[currentGame].arrWordItemModels.shuffled()
        var addedCount = 0

        for(shuffledItem in newShuffledList) {
            var shouldAdd = true
            if(!shuffledItem.isTrue) {
                for(tipItem in showedTips) {
                    if(tipItem == shuffledItem.modelID) {
                        shouldAdd = false
                        break
                    }
                }
            } else { shouldAdd = false }
            if(shouldAdd) {
                showedTips.add(shuffledItem.modelID)
                addedCount++
            }
            if(addedCount == 2) { break }
        }
        showTipsButton = !((itemToGuess ?: 0L).toInt() - showedTips.size == 2)
        tips.value = showedTips
    }

    enum class GameWordsButtonNextState {
        BS_NEXT,
        BS_FINISH,
        BS_ENABLED_CHECK,
        BS_NOT_ENABLED_CHECK,
    }
}
