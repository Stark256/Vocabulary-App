package com.vocabulary.ui.tests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is tests Fragment"
    }
    val text: LiveData<String> = _text
}