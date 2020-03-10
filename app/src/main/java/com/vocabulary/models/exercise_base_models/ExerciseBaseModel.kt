package com.vocabulary.models.exercise_base_models

import java.io.Serializable

class ExerciseBaseModel (
    val game_type: ExerciseType,
    val game_name_res: Int
) : Serializable {
    lateinit var words_count_arr: Array<String>
    lateinit var option_1_title: String
    lateinit var option_2_title: String
    lateinit var option_3_title: String
    var option_2_is_enable: Boolean = false
    var option_3_is_enable: Boolean = false
}

enum class ExerciseType : Serializable {
    GAME_WORDS,
    GAME_LETTERS
}
