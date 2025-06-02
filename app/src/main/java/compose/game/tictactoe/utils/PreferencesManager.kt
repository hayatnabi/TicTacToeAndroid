package compose.game.tictactoe.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREF_NAME = "app_preferences"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun save(context: Context, key: String, value: Any) {
        val editor = getPrefs(context).edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String  -> editor.putString(key, value)
            is Int     -> editor.putInt(key, value)
            is Float   -> editor.putFloat(key, value)
            is Long    -> editor.putLong(key, value)
        }
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(context: Context, key: String, default: T): T {
        val prefs = getPrefs(context)
        return when (default) {
            is Boolean -> prefs.getBoolean(key, default)
            is String  -> prefs.getString(key, default)
            is Int     -> prefs.getInt(key, default)
            is Float   -> prefs.getFloat(key, default)
            is Long    -> prefs.getLong(key, default)
            else -> default
        } as T
    }
}
