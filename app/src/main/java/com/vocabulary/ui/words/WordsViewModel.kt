package com.vocabulary.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WordsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is words Fragment"
    }
    val text: LiveData<String> = _text
}