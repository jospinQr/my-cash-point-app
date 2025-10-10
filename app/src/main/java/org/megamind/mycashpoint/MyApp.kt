package org.megamind.mycashpoint

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.megamind.mycashpoint.di.appModule
import org.megamind.mycashpoint.di.roomModule

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidLogger()
            androidContext(this@MyApp)
            modules(appModule, roomModule)

        }

    }

}


