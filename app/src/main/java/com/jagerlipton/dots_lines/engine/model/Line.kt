package com.jagerlipton.dots_lines.engine.model

class Line(val dot1: Int, val dot2: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Line

        if (dot1 != other.dot1) return false
        if (dot2 != other.dot2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dot1
        result = 31 * result + dot2
        return result
    }
}