package com.memoryproject.app

import android.app.Application
import com.memoryproject.app.di.appModule
import com.memoryproject.app.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MemoryProjectApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MemoryProjectApp)
            modules(appModule, dataModule)
        }
    }
}
