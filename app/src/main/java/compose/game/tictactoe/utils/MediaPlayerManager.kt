package compose.game.tictactoe.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.first

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null

    suspend fun playSound(context: Context, soundResId: Int) {
        val shouldPlaySound = PreferencesManager.get(context, Constants.KEY_SHOULD_PLAY_SOUND, true).first()
        if (!shouldPlaySound) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
