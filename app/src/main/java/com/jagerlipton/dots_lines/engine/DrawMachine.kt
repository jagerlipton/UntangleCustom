package com.jagerlipton.dots_lines.engine

import android.graphics.*
import com.jagerlipton.dots_lines.engine.model.Dot
import com.jagerlipton.dots_lines.engine.model.GameColors
import com.jagerlipton.dots_lines.engine.model.Line


class DrawMachine(private val gameColors: GameColors) {

    fun draw(canvas: Canvas, dotList: List<Dot>, linesSet: Set<Line>) {
        drawBG(canvas, gameColors)
        drawLines(canvas, gameColors, dotList, linesSet)
        drawDots(canvas, gameColors, dotList)
    }

    private fun drawBG(canvas: Canvas, gameColors: GameColors) {
        val paint = Paint()
        paint.color = gameColors.game_bg
        paint.style = Paint.Style.FILL
        canvas.drawRect(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat(), paint)
    }

    private fun drawDots(canvas: Canvas, gameColors: GameColors, dotList: List<Dot>) {
        dotList.forEach() {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.isDither = true
            paint.style = Paint.Style.FILL_AND_STROKE
            if (!it.isTouched()) paint.color = gameColors.game_untouched_circle_fill
            else paint.color = gameColors.game_touched_circle_fill
            canvas.drawCircle(
                it.getX().toFloat(),
                it.getY().toFloat(),
                it.getRadius().toFloat(),
                paint
            )
        }
    }

    private fun drawLines(canvas: Canvas, gameColors: GameColors, dotList: List<Dot>, linesSet: Set<Line> ) {
        linesSet.forEach() {
            val paint = Paint()
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 4F
            paint.color = gameColors.game_untouched_lines
            canvas.drawLine(
                dotList[it.dot1].getX().toFloat(),
                dotList[it.dot1].getY().toFloat(),
                dotList[it.dot2].getX().toFloat(),
                dotList[it.dot2].getY().toFloat(),
                paint
            )
        }
    }

    fun drawText(canvas: Canvas, text:String){
        val paint = Paint()
        paint.color = gameColors.game_bg
        paint.style = Paint.Style.FILL
        canvas.drawRect(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat(), paint)
        paint.color = gameColors.game_touched_circle_stroke
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.textSize = 200F
        val typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paint.typeface = typeface
        paint.setShadowLayer(20.0f, 0.0f, 0.0f, Color.BLACK)

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val textWidth: Int = paint.measureText(text).toInt()
        val textHeight: Int = bounds.height()
        canvas.drawText(text,
            ((canvas.width/2) - (textWidth / 2)).toFloat(),
            ((canvas.height/2) + (textHeight /2)).toFloat(),
            paint
        )
    }


}