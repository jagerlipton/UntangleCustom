package com.jagerlipton.dots_lines.ui.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jagerlipton.dots_lines.storage.storage.IStorage

class OptionsViewModel(private val storage: IStorage) : ViewModel() {

    private val _dotCount = MutableLiveData(storage.loadOptionsDotCount())
    val dotCount: LiveData<Int> get() = _dotCount

    fun setDotCount(count: Int) {
        _dotCount.value = count
    }

    private val _maxLinksCount = MutableLiveData(storage.loadOptionsMaxLinksCount())
    val maxLinksCount: LiveData<Int> get() = _maxLinksCount

    fun setMaxLinksCount(count: Int) {
        _maxLinksCount.value = count
    }

    private val _radiusDot = MutableLiveData(storage.loadOptionsRadiusDot())
    val radiusDot: LiveData<Int> get() = _radiusDot

    fun setRadius(radius: Int) {
        _radiusDot.value = radius
    }

    fun savePrefs() {
        storage.saveOptionsDotCount(dotCount.value ?: 10)
        storage.saveOptionsMaxLinksCount(maxLinksCount.value ?: 3)
        storage.saveOptionsRadiusDot(radiusDot.value ?: 30)
    }

    fun newCustomGame() {
        storage.saveGameOverState(true)
    }

}