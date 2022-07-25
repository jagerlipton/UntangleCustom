package com.jagerlipton.dots_lines.engine

import android.graphics.Canvas
import android.view.SurfaceHolder
import com.jagerlipton.dots_lines.ui.CustomSurfaceView

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val customSurfaceView: CustomSurfaceView
) : Thread() {
    private var running: Boolean = false
    private val targetFPS = 60

    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()
        while (running) {
            startTime = System.nanoTime()
            canvas = null
            try {
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                   if(canvas!=null)  this.customSurfaceView.update()
                   if(canvas!=null) this.customSurfaceView.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis
            try {
                if (waitTime>0) sleep(waitTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var canvas: Canvas? = null
    }

}