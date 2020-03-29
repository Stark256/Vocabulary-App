package com.vocabulary.models

import com.vocabulary.models.game_letters_models.GameLetterItemModel
import com.vocabulary.models.game_words_models.GameWordItemModel
import com.vocabulary.models.word_models.LetterModel
import com.vocabulary.models.word_models.WordModel


// Safe Let

fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
fun <T1: Any, T2: Any, T3: Any, T4: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}
fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}

fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, T6: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, p6: T6?, block: (T1, T2, T3, T4, T5, T6)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null && p6 != null) block(p1, p2, p3, p4, p5, p6) else null
}

fun ArrayList<LetterModel>.cloneList() : ArrayList<LetterModel> {
    val newArr = ArrayList<LetterModel>()
    for(item in this) {
        val letter = LetterModel(item.letter)
        letter.isSelected = item.isSelected
        newArr.add(letter)
    }
    return newArr
}

fun ArrayList<GameWordItemModel>.getByModelID(modelID: Long) : GameWordItemModel {
    return this.first { gameWordItemModel -> gameWordItemModel.modelID == modelID  }
}

fun ArrayList<GameLetterItemModel>.getByModelID(uniqueID: Long) : GameLetterItemModel {
    return this.first { gameWordItemModel -> gameWordItemModel.uniqueID == uniqueID  }
}

fun ArrayList<WordModel>.getAllExcept(
    exceptWordID: Long,
    result: (ArrayList<WordModel>) -> Unit
) {
    val resultList = ArrayList<WordModel>()
    for(item in this) {
        if(item.id != exceptWordID) {
            resultList.add(item)
        }
    }
    result.invoke(resultList)
}

fun ArrayList<WordModel>.randomListExcept(
    exceptWordID: Long, listSize: Int,
    result: (ArrayList<WordModel>) -> Unit)  {

    val resultList = ArrayList<WordModel>()
    val exceptArr = ArrayList<Long>()
    exceptArr.add(exceptWordID)


    for(index in 0..listSize-1) {
        var randomAgain = true

        do {
            val randItem = this.random()
            var isAdd = true
            for(exceptItem in exceptArr) {
                if(randItem.id == exceptItem) {
                    isAdd = false
                    break
                }
            }
            if(isAdd) {
                randomAgain = false
                resultList.add(randItem)
                exceptArr.add(randItem.id)
            }

        } while (randomAgain)

    }
    result.invoke(resultList)
}
