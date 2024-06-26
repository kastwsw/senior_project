package io.r3chain

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.r3chain.core.data.LoggerService
import javax.inject.Inject

@HiltAndroidApp
class R3App : Application() {

    @Inject
    lateinit var loggerService: LoggerService

    override fun onTerminate() {
        super.onTerminate()
        loggerService.cancel()
    }
}