package com.jagerlipton.dots_lines.storage.storage

import com.jagerlipton.dots_lines.engine.model.Dot
import com.jagerlipton.dots_lines.engine.model.Line

interface IStorage {
    fun loadGameDots():List<Dot>
    fun saveGameDots(list :List<Dot>)
    fun loadGameLines():Set<Line>
    fun saveGameLines(set: Set<Line>)
    fun loadOptionsDotCount(): Int
    fun saveOptionsDotCount(dotCount: Int)
    fun loadOptionsMaxLinksCount(): Int
    fun saveOptionsMaxLinksCount(maxLinksCount: Int)
    fun loadOptionsRadiusDot():Int
    fun saveOptionsRadiusDot(radius: Int)
    fun loadGameOverState():Boolean
    fun saveGameOverState(flag: Boolean)
}