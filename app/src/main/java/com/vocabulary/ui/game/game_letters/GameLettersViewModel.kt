package com.vocabulary.ui.game.game_letters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vocabulary.managers.Injector
import com.vocabulary.models.ExerciseResult
import com.vocabulary.models.GameResult
import com.vocabulary.models.game_letters_models.GameLetterItemModel
import com.vocabulary.models.game_letters_models.GameLetterItemModelState
import com.vocabulary.models.game_letters_models.GameLettersModel
import com.vocabulary.models.safeLet
import com.vocabulary.models.word_models.WordModel

class GameLettersViewModel : ViewModel() {

    val game = MutableLiveData<GameLettersModel>()
    val tips = MutableLiveData<ArrayList<Long>>()
    val screenTitle = MutableLiveData<String>()
    val loadingPercent = MutableLiveData<Int>()
    val viewState = MutableLiveData<GameLetterViewState>()
    val buttonNextState = MutableLiveData<GameLettersButtonNextState>()
    val showFinishDialog = MutableLiveData<ArrayList<GameResult>>()

    var showTipsButton = true
    var finishImmediate = true

    private val gamesList = ArrayList<GameLettersModel>()
    private val exerciseResult : ExerciseResult = ExerciseResult()

    private val showedTips = ArrayList<Long>()

    private var wordsCount: Long? = null
    private var itemToGuess: Long? = null
    private var currentGame: Int = -1

    fun getCurrentGame() : GameLettersModel {
        return gamesList[currentGame]
    }

    fun setInitExtras(wordsCount: Long, itemToGuess: Long) {
        if(wordsCount != -1L) this.wordsCount = wordsCount
        if(itemToGuess != -1L) this.itemToGuess = itemToGuess
    }

    fun loadGames() {
        gamesList.clear()
        viewState.value = GameLetterViewState.STATE_LOADING

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

                        generateGamesList(ArrayList(), wordsList)
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

                                generateGamesList(failsWords, wordList)
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

                                generateGamesList(failsWords, wordList)
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
        wordsArr: ArrayList<WordModel>) {
        val jointGameList = ArrayList<WordModel>()
        if(failsArr.isNotEmpty()) jointGameList.addAll(failsArr)
        if(wordsArr.isNotEmpty()) jointGameList.addAll(wordsArr)

        for(word in jointGameList) {

            val correctLettersList = getCorrectLettersArr(word.word)
            val guessLettersList = getGuessLettersArr(word.word, jointGameList, correctLettersList)

            gamesList.add(
                GameLettersModel(
                    word,
                    word.translation,
                    correctLettersList,
                    guessLettersList
                    )
            )

            loadingPercent.value = ((100 / jointGameList.size) * gamesList.size)
        }
        gamesList.shuffle()
        viewState.value = GameLetterViewState.STATE_READY
    }

    private fun getGuessLettersArr(
        correctWord: String,
        wordArr: ArrayList<WordModel>,
        correctLetterArr: ArrayList<GameLetterItemModel>)
            : ArrayList<GameLetterItemModel> {
        val arrGuess = ArrayList<GameLetterItemModel>()
        var wordString = ""
//        wordString += correctWord
        arrGuess.addAll(correctLetterArr)
//        val lastGuesC: Int = (itemToGuess?.toInt() ?: correctWord.length) - correctWord.length
        val lastGuesC: Int = (itemToGuess?.toInt() ?: correctWord.length)

        for(index in 0..lastGuesC-1) {
            wordString += wordArr.random().word.toCharArray().random()
        }

        val stringArr = wordString.toCharArray()
        for(index in 0..stringArr.size -1) {
            arrGuess.add(GameLetterItemModel(
                GameLetterItemModelState.GS_LETTER, index.toLong() + correctLetterArr.size,
                stringArr[index].toString(), false))
        }
        arrGuess.shuffle()
        return arrGuess
    }

    private fun getCorrectLettersArr(correctWord: String) : ArrayList<GameLetterItemModel> {
        val resultArr = ArrayList<GameLetterItemModel>()
        val stringArr = correctWord.toCharArray()
        for(index in 0..stringArr.size -1) {
            resultArr.add(GameLetterItemModel(
                GameLetterItemModelState.GS_LETTER, index.toLong(),
                stringArr[index].toString(), true))
        }
        return resultArr
    }

    fun startPressed() {
        viewState.value = GameLetterViewState.STATE_STARTED
        nextPressed()
    }

    fun checkPressed() {
        buttonNextState.value =
            if(wordsCount != null && wordsCount!!.toInt() == currentGame+1)
                GameLettersButtonNextState.BS_FINISH
            else GameLettersButtonNextState.BS_NEXT
    }

    fun showCheck(isFull: Boolean) {
        buttonNextState.value =
             if(isFull) GameLettersButtonNextState.BS_ENABLED_CHECK
             else  GameLettersButtonNextState.BS_NOT_ENABLED_CHECK
    }

    fun onGameLetterSelected(resultString: String?) {
        exerciseResult.setGameLettersResult(
            gamesList[currentGame].correctWord,
            resultString)
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
        buttonNextState.value = GameLettersButtonNextState.BS_NOT_ENABLED_CHECK
        currentGame++
        game.value = gamesList[currentGame]
        screenTitle.value = "${currentGame+1} / ${gamesList.size}"
    }

    fun tipsPressed() {
        val gameItem = gamesList[currentGame]
        val newShuffledList = gameItem.lettersToGuess.shuffled()
        var addedCount = 0
        for(shuffledItem in newShuffledList) {
            var shouldAdd = true
            if(!shuffledItem.isCorrect) {
                for(tipsItem in showedTips) {
                    if(tipsItem == shuffledItem.uniqueID) {
                        shouldAdd = false
                        break
                    }
                }
            } else { shouldAdd = false }

            if(shouldAdd) {
                showedTips.add(shuffledItem.uniqueID)
                addedCount++
            }
            if(addedCount == 2) { break }
        }
        showTipsButton = !( gameItem.lettersToGuess.size - gameItem.correctLetters.size == (itemToGuess ?: 0L).toInt())

        tips.value = showedTips

    }

    enum class GameLettersButtonNextState {
        BS_NEXT,
        BS_FINISH,
        BS_ENABLED_CHECK,
        BS_NOT_ENABLED_CHECK,
    }
}