package com.jagerlipton.dots_lines.ui.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jagerlipton.dots_lines.storage.storage.IStorage

class OptionsViewModel(private val storage: IStorage) : ViewModel() {

    private val dotCount = MutableLiveData(storage.loadOptionsDotCount())
    fun getDotCount(): LiveData<Int> {
        return dotCount
    }

    fun setDotCount(count: Int) {
        dotCount.value = count
    }

    private val maxLinksCount = MutableLiveData(storage.loadOptionsMaxLinksCount())
    fun getMaxLinksCount(): LiveData<Int> {
        return maxLinksCount
    }

    fun setMaxLinksCount(count: Int) {
        maxLinksCount.value = count
    }

    private val radiusDot = MutableLiveData(storage.loadOptionsRadiusDot())
    fun getRadius(): LiveData<Int> {
        return radiusDot
    }

    fun setRadius(radius: Int) {
        radiusDot.value = radius
    }

    fun savePrefs() {
        if (dotCount.value != null) storage.saveOptionsDotCount(dotCount.value!!)
        if (maxLinksCount.value != null) storage.saveOptionsMaxLinksCount(maxLinksCount.value!!)
        if (radiusDot.value != null) storage.saveOptionsRadiusDot(radiusDot.value!!)
    }

    fun newCustomGame() {
        storage.saveGameOverState(true)
    }

}