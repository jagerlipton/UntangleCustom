package com.jagerlipton.dots_lines.storage.storage

import android.content.Context
import android.content.SharedPreferences
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jagerlipton.dots_lines.engine.model.Dot
import com.jagerlipton.dots_lines.engine.model.Line
import com.jagerlipton.dots_lines.storage.storage.model.StorageDot
import com.jagerlipton.dots_lines.storage.storage.model.StorageLine
import java.io.IOException


class Storage(private val context: Context) : IStorage {
    private val APP_PREFERENCES = "gameSettings"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    private val APP_PREFERENCES_DOTS = "APP_PREFERENCES_DOTS"
    private val APP_PREFERENCES_LINES = "APP_PREFERENCES_LINES"
    private val APP_PREFERENCES_DOTS_COUNT = "APP_PREFERENCES_DOTS_COUNT"
    private val APP_PREFERENCES_MAX_LINKS_COUNT = "APP_PREFERENCES_MAX_LINKS_COUNT"
    private val APP_PREFERENCES_RADIUS_DOT = "APP_PREFERENCES_RADIUS_DOT"
    private val APP_PREFERENCES_GAMEOVER = "APP_PREFERENCES_GAMEOVER"

    private fun StorageDot.toDot() = Dot(
        index = index,
        x = x,
        y = y,
        radius = radius,
        linksSet = links.toHashSet()
    )

    override fun loadGameDots(): List<Dot> {
        val mapper = jacksonObjectMapper()
        var storageList: MutableList<StorageDot> = mutableListOf()
        val jsonString = sharedPreferences.getString(APP_PREFERENCES_DOTS, "")
        try {

            if (jsonString != null) storageList = mapper.readValue(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val resultList: MutableList<Dot> = mutableListOf()
        storageList.forEach() {
            resultList.add(it.toDot())
        }
        return resultList
    }

    private fun Dot.toStorageDot() = StorageDot(
        index = getIndex(),
        x = getX(),
        y = getY(),
        radius = getRadius(),
        links = getLinksSet()
    )

    override fun saveGameDots(list: List<Dot>) {
        val storageList: MutableList<StorageDot> = mutableListOf()
        list.forEach() {
            storageList.add(it.toStorageDot())
        }
        val mapper = jacksonObjectMapper()
        try {
            val jsonString = mapper.writeValueAsString(storageList)
            if (jsonString != null) sharedPreferences.edit()
                .putString(APP_PREFERENCES_DOTS, jsonString).apply()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun StorageLine.toLine() = Line(
        dot1 = dot1,
        dot2 = dot2
    )

    override fun loadGameLines(): Set<Line> {
        val mapper = jacksonObjectMapper()
        var storageSet: HashSet<StorageLine> = hashSetOf()
        val jsonString = sharedPreferences.getString(APP_PREFERENCES_LINES, "")
        try {
            if (jsonString != null) storageSet = mapper.readValue(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val resultSet: HashSet<Line> = hashSetOf()
        storageSet.forEach() {
            resultSet.add(it.toLine())
        }
        return resultSet
    }

    private fun Line.toStorageLine() = StorageLine(
        dot1 = dot1,
        dot2 = dot2
    )

    override fun saveGameLines(set: Set<Line>) {
        val storageSet: HashSet<StorageLine> = hashSetOf()
        set.forEach() {
            storageSet.add(it.toStorageLine())
        }
        val mapper = jacksonObjectMapper()
        try {
            val jsonString = mapper.writeValueAsString(set)
            if (jsonString != null) sharedPreferences.edit()
                .putString(APP_PREFERENCES_LINES, jsonString).apply()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun loadOptionsDotCount(): Int {
        return sharedPreferences.getInt(APP_PREFERENCES_DOTS_COUNT, 10)
    }

    override fun saveOptionsDotCount(dotCount: Int) {
        sharedPreferences.edit().putInt(APP_PREFERENCES_DOTS_COUNT, dotCount).apply()
    }

    override fun loadOptionsMaxLinksCount(): Int {
        return sharedPreferences.getInt(APP_PREFERENCES_MAX_LINKS_COUNT, 4)
    }

    override fun saveOptionsMaxLinksCount(maxLinksCount: Int) {
        sharedPreferences.edit().putInt(APP_PREFERENCES_MAX_LINKS_COUNT, maxLinksCount).apply()
    }

    override fun loadOptionsRadiusDot(): Int {
        return sharedPreferences.getInt(APP_PREFERENCES_RADIUS_DOT, 25)
    }

    override fun saveOptionsRadiusDot(radius: Int) {
        sharedPreferences.edit().putInt(APP_PREFERENCES_RADIUS_DOT, radius).apply()
    }

    override fun loadGameOverState(): Boolean {
        return sharedPreferences.getBoolean(APP_PREFERENCES_GAMEOVER, true)
    }

    override fun saveGameOverState(flag: Boolean) {
        sharedPreferences.edit().putBoolean(APP_PREFERENCES_GAMEOVER, flag).apply()
    }
}