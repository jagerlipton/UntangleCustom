package com.jagerlipton.dots_lines

import android.app.Application
import com.jagerlipton.dots_lines.DI.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(listOf(appModule))
            androidLogger(org.koin.core.logger.Level.NONE)
            androidContext(this@App)
        }
    }
}