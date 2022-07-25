package com.jagerlipton.dots_lines.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jagerlipton.dots_lines.storage.storage.IStorage

class HomeViewModel(private val storage: IStorage) : ViewModel() {
    private val _gameOver = MutableLiveData(storage.loadGameOverState())
    val gameOver: LiveData<Boolean> get() = _gameOver

    fun loadPrefs() {
        _gameOver.value = storage.loadGameOverState()
    }
}