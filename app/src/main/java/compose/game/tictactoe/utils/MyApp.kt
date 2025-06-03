package compose.game.tictactoe.utils

import android.app.Application
import android.content.Context

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-wide resources here
        instance = this
    }

    companion object {
        private var instance: MyApp? = null

        val context: Context
            get() = instance?.applicationContext!!
    }
}