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
        val paintFillUntouched = Paint()
        paintFillUntouched.style = Paint.Style.FILL
        paintFillUntouched.color = gameColors.game_untouched_circle_fill

        val paintFillTouched = Paint()
        paintFillTouched.style = Paint.Style.FILL
        paintFillTouched.color = gameColors.game_touched_circle_fill

        val paintStrokeUntouched = Paint()
        paintStrokeUntouched.style = Paint.Style.STROKE
        paintStrokeUntouched.strokeWidth = 2F
        paintStrokeUntouched.color = gameColors.game_untouched_circle_stroke

        val paintStrokeTouched = Paint()
        paintStrokeTouched.style = Paint.Style.STROKE
        paintStrokeTouched.strokeWidth = 2F
        paintStrokeTouched.color = gameColors.game_touched_circle_stroke

        dotList.forEach() {
            if (!it.isTouched())
            {
                canvas.drawCircle(
                    it.getX().toFloat(),
                    it.getY().toFloat(),
                    it.getRadius().toFloat(),
                    paintFillUntouched
                )
                canvas.drawCircle(
                    it.getX().toFloat(),
                    it.getY().toFloat(),
                    it.getRadius().toFloat(),
                    paintStrokeUntouched
                )
            }
            else {
                canvas.drawCircle(
                    it.getX().toFloat(),
                    it.getY().toFloat(),
                    it.getRadius().toFloat(),
                    paintFillTouched
                )
                canvas.drawCircle(
                    it.getX().toFloat(),
                    it.getY().toFloat(),
                    it.getRadius().toFloat(),
                    paintStrokeTouched
                )
            }
        }
    }

    private fun drawLines(canvas: Canvas, gameColors: GameColors, dotList: List<Dot>, linesSet: Set<Line> ) {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4F
        paint.color = gameColors.game_untouched_lines

        linesSet.forEach() {
            canvas.drawLine(
                dotList[it.dot1].getX().toFloat(),
                dotList[it.dot1].getY().toFloat(),
                dotList[it.dot2].getX().toFloat(),
                dotList[it.dot2].getY().toFloat(),
                paint
            )
        }
    }

    fun drawText(canvas: Canvas,textSize:Float, text:String){
        val paint = Paint()
        paint.color = gameColors.game_untouched_circle_fill
        paint.style = Paint.Style.FILL
        paint.textSize = textSize
        val typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        paint.typeface = typeface
        paint.setShadowLayer(10.0f, 0.0f, 0.0f, Color.BLACK)
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