package com.jagerlipton.dots_lines.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jagerlipton.dots_lines.storage.storage.IStorage

class HomeViewModel(private val storage: IStorage) : ViewModel() {
    private val gameOver = MutableLiveData(storage.loadGameOverState())
    fun isGameOver(): LiveData<Boolean> {
        return gameOver
    }

    fun loadPrefs() {
        gameOver.value = storage.loadGameOverState()
    }
}