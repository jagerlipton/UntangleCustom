package com.jagerlipton.dots_lines.engine.model

class Dot(private val index:Int, private var x: Int, private var y: Int, private  var radius: Int, private var linksSet: HashSet<Int>) {

    private var touched:Boolean = false
    private var visible = false

    fun getIndex():Int{
        return index
    }

    fun isVisible():Boolean{
        return visible
    }

    fun setVisible(flag:Boolean) {
        this.visible=flag
    }

    fun setRadius(radius:Int) {
        this.radius = radius
    }

    fun getRadius():Int {
        return radius
    }

    fun decreaseRadius(value: Int){
        radius -=value
    }

    fun addLinkToLinksSet(index:Int) {
        linksSet.add(index)
    }

    fun getLinksSet():Set<Int>{
        return linksSet
    }

    fun setX(x: Int) {
        this.x = x
    }

    fun setY(y: Int) {
        this.y = y
    }

    fun getX():Int{
        return x
    }

    fun getY():Int{
        return y
    }

    fun isTouched():Boolean {
        return touched
    }

    fun setTouchStatus(isTouched:Boolean){
        touched=isTouched
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Dot

        if (index != other.index) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (radius != other.radius) return false
        if (linksSet != other.linksSet) return false
        if (touched != other.touched) return false
        if (visible != other.visible) return false

        return true
    }

    override fun hashCode(): Int {
        var result = index
        result = 31 * result + x
        result = 31 * result + y
        result = 31 * result + radius
        result = 31 * result + linksSet.hashCode()
        result = 31 * result + touched.hashCode()
        result = 31 * result + visible.hashCode()
        return result
    }


}