package compose.game.tictactoe.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesManager {
    // Save value generically
    suspend fun <T : Any> save(context: Context, key: String, value: T) {
        context.dataStore.edit { preferences ->
            when (value) {
                is Boolean -> preferences[booleanPreferencesKey(key)] = value
                is Int -> preferences[intPreferencesKey(key)] = value
                is String -> preferences[stringPreferencesKey(key)] = value
                is Float -> preferences[floatPreferencesKey(key)] = value
                is Long -> preferences[longPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    // Get value as Flow<T> so it's observable
    fun <T : Any> get(context: Context, key: String, default: T): Flow<T> {
        val dataStoreKey = keyOf(default, key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: default
        }
    }

    // Key generator based on type
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> keyOf(value: T, key: String): Preferences.Key<T> {
        return when (value) {
            is Boolean -> booleanPreferencesKey(key)
            is Int -> intPreferencesKey(key)
            is String -> stringPreferencesKey(key)
            is Float -> floatPreferencesKey(key)
            is Long -> longPreferencesKey(key)
            else -> throw IllegalArgumentException("Unsupported type")
        } as Preferences.Key<T>
    }
}
