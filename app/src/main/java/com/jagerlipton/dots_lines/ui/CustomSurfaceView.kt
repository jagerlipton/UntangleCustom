package com.jagerlipton.dots_lines.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.jagerlipton.dots_lines.R
import com.jagerlipton.dots_lines.engine.DrawMachine
import com.jagerlipton.dots_lines.engine.GameThread
import com.jagerlipton.dots_lines.engine.model.Dot
import com.jagerlipton.dots_lines.engine.model.GameColors
import com.jagerlipton.dots_lines.engine.model.Line
import com.jagerlipton.dots_lines.storage.storage.IStorage
import org.koin.java.KoinJavaComponent.inject
import java.lang.Thread.sleep
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class CustomSurfaceView(context: Context, attributes: AttributeSet) :
    SurfaceView(context, attributes),
    SurfaceHolder.Callback {
    private lateinit var thread: GameThread
    private var dotList: MutableList<Dot> = mutableListOf()
    private var linesSet: HashSet<Line> = HashSet()
    private var circleNumber: Int = -1
    private val gameColors: GameColors = getGameColors()
    private val storage by inject<IStorage>(IStorage::class.java)
    private var gameover: Boolean = true
    private val drawMachine: DrawMachine = DrawMachine(gameColors)

    init {
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSLUCENT)
        isFocusable = true
    }

    private fun onLaunchSurface() {
        when (storage.loadGameOverState()) {
            true -> newGame()
            false -> loadGame()
        }
    }

    private fun newGame() {
        createGame(
            storage.loadOptionsDotCount(),
            storage.loadOptionsMaxLinksCount(),
            storage.loadOptionsRadiusDot()
        )
        gameover = false
    }

    private fun loadGame() {
        dotList = storage.loadGameDots() as MutableList<Dot>
        linesSet = storage.loadGameLines() as HashSet<Line>
        gameover = false
    }

    private fun saveGame() {
        when (gameover) {
            true -> {
                storage.saveGameOverState(true)
            }
            false -> {
                storage.saveGameOverState(false)
                storage.saveGameDots(dotList)
                storage.saveGameLines(linesSet)
            }
        }
    }

    private fun createGame(dotCount: Int, maxLinksCount: Int, radius: Int) {
        for (i: Int in 0 until dotCount) {
            dotList.add(Dot(i, 0, 0, radius, HashSet()))
        }
        alignmentDots(dotList)
        generateLines(dotList, maxLinksCount)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        onLaunchSurface()
        thread = GameThread(holder, this)
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        // TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        saveGame()
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            retry = false
        }
    }

    private fun getGameColors(): GameColors {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return GameColors(
                resources.getColor(R.color.game_untouched_circle_stroke, context.theme),
                resources.getColor(R.color.game_untouched_circle_fill, context.theme),
                resources.getColor(R.color.game_touched_circle_stroke, context.theme),
                resources.getColor(R.color.game_touched_circle_fill, context.theme),
                resources.getColor(R.color.game_touched_linked_circle_stroke, context.theme),
                resources.getColor(R.color.game_touched_linked_circle_fill, context.theme),
                resources.getColor(R.color.game_untouched_lines, context.theme),
                resources.getColor(R.color.game_touched_lines, context.theme),
                resources.getColor(R.color.game_bg, context.theme)
            )
        } else return GameColors(
            resources.getColor(R.color.game_untouched_circle_stroke),
            resources.getColor(R.color.game_untouched_circle_fill),
            resources.getColor(R.color.game_touched_circle_stroke),
            resources.getColor(R.color.game_touched_circle_fill),
            resources.getColor(R.color.game_touched_linked_circle_stroke),
            resources.getColor(R.color.game_touched_linked_circle_fill),
            resources.getColor(R.color.game_untouched_lines),
            resources.getColor(R.color.game_touched_lines),
            resources.getColor(R.color.game_bg)
        )
    }

    private fun patternCircle(list: List<Dot>) {
        val stepAngle: Double = (2 * PI / list.size)
        val height = this.height
        val width = this.width
        val radius = if (width < height) {
            ((width - 100) / 2)
        } else {
            ((height - 100) / 2)
        }
        for (i in 1..list.size) {
            list[i - 1].setX((width / 2) + (radius * cos((i * stepAngle).toFloat())).toInt())
            list[i - 1].setY((height / 2) + (radius * sin((i * stepAngle).toFloat())).toInt())
        }
    }

    private fun patternStorm(list: List<Dot>) {
        var phi: Double = 0.0
        var x: Double
        var y: Double
        var centerX: Double
        var centerY: Double
        var r: Int
        val height = this.height
        val width = this.width

        if (height > width) {
            centerX = (width / 2).toDouble()
            centerY = (height / 2).toDouble()
        } else {
            centerX = (height / 2).toDouble()
            centerY = (width / 2).toDouble()
        }
        for (i in list.indices) {
            phi = (i * 1).toDouble();
            r = 10 * i;
            x = r * cos(phi)
            y = r * sin(phi)
            list[i].setX(centerX.toInt() + x.toInt())
            list[i].setY(centerY.toInt() + y.toInt())
        }//
    }

    private fun patternSpiral(list: List<Dot>) {
        var centerX: Double
        var centerY: Double
        val height = this.height
        val width = this.width
        if (height > width) {
            centerX = (width / 2).toDouble()
            centerY = (height / 2).toDouble()
        } else {
            centerX = (height / 2).toDouble()
            centerY = (width / 2).toDouble()
        }
        val interval: Double = 80.0
        val rSafe: Int = (interval / 2).toInt()
        val a: Double = -0.2 * interval
        var x = 0.0
        var y = 0.0
        x += interval
        for (i in list.indices) {
            val r: Double = sqrt(x * x + y * y)
            val tx = a * x + r * y
            val ty = a * y - r * x
            val tLen: Double = sqrt(tx * tx + ty * ty)
            val k = interval / tLen
            x += tx * k
            y += ty * k
            list[i].setX(centerX.toInt() + x.toInt())
            list[i].setY(centerY.toInt() + y.toInt())
        }//
    }


    private fun alignmentDots(list: List<Dot>) {
           patternCircle(list)
        //  patternStorm(list)
        //   patternSpiral(list)
        // when ...etc pattern
    }


    private fun generateLines(list: List<Dot>, maxLinksCount: Int) {
// здесь должен быть генератор плоского графа
        val matrix_h: Int = sqrt(list.size.toDouble()).toInt()
        val matrix_w = if (list.size % matrix_h == 0) list.size / matrix_h
        else (list.size / matrix_h) + 1
        val dotMatrix: Array<Array<Int>> = Array(matrix_h) { Array(matrix_w) { -1 } }
        var indexList = mutableListOf<Int>()
        for (i: Int in list.indices) indexList.add(i)
        indexList = indexList.shuffled() as MutableList<Int>
        // заполнение массива индексами точек
        var index: Int = 0
        for (row: Int in 0 until matrix_h)
            for (col: Int in 0 until matrix_w) {
                if (index < indexList.size) {
                    dotMatrix[row][col] = indexList[index]
                    index += 1
                }
            }

        for (row: Int in 0 until matrix_h)
            for (col: Int in 0 until matrix_w) {
                if (row + 1 < matrix_h && dotMatrix[row + 1][col] != -1) { // линия вниз
                    if (list[dotMatrix[row][col]].getLinksSet().size < maxLinksCount && list[dotMatrix[row + 1][col]].getLinksSet().size < maxLinksCount) {
                        linesSet.add(
                            Line(
                                list[dotMatrix[row][col]].getIndex(),
                                list[dotMatrix[row + 1][col]].getIndex()
                            )
                        )
                        list[dotMatrix[row][col]].addLinkToLinksSet(list[dotMatrix[row + 1][col]].getIndex())
                        list[dotMatrix[row + 1][col]].addLinkToLinksSet(list[dotMatrix[row][col]].getIndex())
                    }
                }
                if (row + 1 < matrix_h && col - 1 >= 0 && dotMatrix[row + 1][col - 1] != -1) { // линия лево вниз
                    if (list[dotMatrix[row][col]].getLinksSet().size < maxLinksCount && list[dotMatrix[row + 1][col - 1]].getLinksSet().size < maxLinksCount) {
                        linesSet.add(
                            Line(
                                list[dotMatrix[row][col]].getIndex(),
                                list[dotMatrix[row + 1][col - 1]].getIndex()
                            )
                        )
                        list[dotMatrix[row][col]].addLinkToLinksSet(list[dotMatrix[row + 1][col - 1]].getIndex())
                        list[dotMatrix[row + 1][col - 1]].addLinkToLinksSet(list[dotMatrix[row][col]].getIndex())
                    }
                }

                if (col + 1 < matrix_w && dotMatrix[row][col + 1] != -1) { // линия вправо
                    if (list[dotMatrix[row][col]].getLinksSet().size < maxLinksCount && list[dotMatrix[row][col + 1]].getLinksSet().size < maxLinksCount) {
                        linesSet.add(
                            Line(
                                list[dotMatrix[row][col]].getIndex(),
                                list[dotMatrix[row][col + 1]].getIndex()
                            )
                        )
                        list[dotMatrix[row][col]].addLinkToLinksSet(list[dotMatrix[row][col + 1]].getIndex())
                        list[dotMatrix[row][col + 1]].addLinkToLinksSet(list[dotMatrix[row][col]].getIndex())
                    }
                }
            }
    }

    private fun pointInsideCircle(circleX: Int, circleY: Int, dotX: Int, dotY: Int, radius: Int): Boolean {
        val FATFINGER = 20
        return ((circleX - dotX) * (circleX - dotX) + (circleY - dotY) * (circleY - dotY) <= (radius + FATFINGER) * (radius + FATFINGER))
    }

    private fun numberCircleOnTouch(
        listDots: List<Dot>,
        x: Int,
        y: Int
    ): Int {  // место испытаний алгоритмов поиска круга по координатам
        for (i in listDots.indices) {
            if (pointInsideCircle(
                    listDots[i].getX(),
                    listDots[i].getY(),
                    x,
                    y,
                    listDots[i].getRadius()
                )
            ) {
                return i
            }
        }
        return -1
    }

    private fun checkIntersection(
        dotA1: Dot,
        dotB1: Dot,
        dotA2: Dot,
        dotB2: Dot
    ): Boolean { //проверяет пересечение двух отрезков
        var ax: Int = dotA1.getX()
        var ay: Int = dotA1.getY()
        var bx: Int = dotB1.getX()
        var by: Int = dotB1.getY()
        var cx: Int = dotA2.getX()
        var cy: Int = dotA2.getY()
        var dx: Int = dotB2.getX()
        var dy: Int = dotB2.getY()

        if (ax == bx && ay == by || cx == dx && cy == dy) return false //  проверка на нулевую длину отрезка
        if (ax == cx && ay == cy || bx == cx && by == cy || ax == dx && ay == dy || bx == dx && by == dy) return false  // проверка на общую конечную точку

        bx -= ax  //перемещение системы координат , чтобы точка А была в начале системы
        by -= ay
        cx -= ax
        cy -= ay
        dx -= ax
        dy -= ay

        val lengthAB: Double = sqrt((bx * bx + by * by).toDouble())  //длина отрезка AB (line1)
        val theCos: Double =
            bx / lengthAB                           //поворот системы координат, чтобы точка В находилась на положительной оси Х
        val theSin: Double = by / lengthAB
        var newX: Double = cx * theCos + cy * theSin
        cy = (cy * theCos - cx * theSin).toInt()
        cx = newX.toInt()
        newX = dx * theCos + dy * theSin
        dy = (dy * theCos - dx * theSin).toInt()
        dx = newX.toInt()
        if (cy < 0 && dy < 0 || cy >= 0 && dy >= 0) return false  //если CD не пересекает совсем
        val positionAB: Double =
            (dx + (cx - dx) * dy / (dy - cy)).toDouble() // точка пересечения вдоль линии AB
        if (positionAB < 0 || positionAB > lengthAB) return false  // если пересекает, но за пределами отрезка
        return true // в остальных случаях пересечение есть
    }

    private fun checkLineList(dotlist: List<Dot>, linelist: List<Line>): Boolean {
        for (i in linelist.indices) {  // прямой перебор списка с линиями на пересечения
            for (y in linelist.indices) {
                if (y != i) if (checkIntersection(
                        dotlist[linelist[i].dot1],
                        dotlist[linelist[i].dot2],
                        dotlist[linelist[y].dot1],
                        dotlist[linelist[y].dot2]
                    )
                ) {
                    return true
                }
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            circleNumber = numberCircleOnTouch(dotList, event.x.toInt(), event.y.toInt())
            if (circleNumber >= 0) {
                dotList[circleNumber].setTouchStatus(true)
                dotList[circleNumber].getLinksSet().forEach() { dotList[it].setTouchStatus(true) }
            }
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            if (circleNumber >= 0) {
                if (event.x.toInt() >= dotList[circleNumber].getRadius() && event.x.toInt() < this.width - dotList[circleNumber].getRadius()) dotList[circleNumber].setX(
                    event.x.toInt()
                )
                if (event.y.toInt() >= dotList[circleNumber].getRadius() && event.y.toInt() < this.height - dotList[circleNumber].getRadius()) dotList[circleNumber].setY(
                    event.y.toInt()
                )
            }
        }
        if (event.action == MotionEvent.ACTION_UP) {
            if (circleNumber >= 0) {
                dotList[circleNumber].setTouchStatus(false)
                dotList[circleNumber].getLinksSet().forEach() { dotList[it].setTouchStatus(false) }
                circleNumber = -1
                if (!checkLineList(dotList, linesSet.toList())) victory()
            }
        }
        return true
    }

    private fun victory() {
        victoryAnimateFlush()
        gameover = true
    }

    private fun victoryAnimateFlush() {
        val baseX: Int = this.width / 2 // x координата центра экрана
        val baseY: Int = this.height / 2 // y координата центра экрана
        var i: Int = 0
        linesSet.clear()

        while (dotList.size > 0) {
            val x1: Int = dotList[i].getX() - baseX
            val y1: Int = dotList[i].getY() - baseY
            val c: Double = (sqrt((x1 * x1 + y1 * y1).toDouble()))
            val step: Double = 2.0
            val y2: Int = ((y1 * (c - step)) / c).toInt()
            val x2: Int = ((x1 * (c - step)) / c).toInt()
            dotList[i].setX(baseX + x2)
            dotList[i].setY(baseY + y2)

            if (dotList[i].getX() - baseX < 10 && dotList[i].getY() - baseY < 10) {
                if (dotList[i].getRadius() > 0) {
                    dotList[i].decreaseRadius(1)
                    if (dotList[i].getRadius() <= 0) {
                        dotList.removeAt(i)
                        i -= 1
                    }
                }
            }
            sleep(0, 2)
            i += 1
            if (i >= dotList.size) i = 0
        }
    }

    fun update() {

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        when (gameover) {
            false -> drawMachine.draw(canvas, dotList, linesSet)
            true -> drawMachine.drawText(canvas, "WIN")
        }
    }
}
