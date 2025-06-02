package compose.game.tictactoe.utils

import android.content.Context
import android.media.MediaPlayer

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(context: Context, soundResId: Int) {
        val shouldPlaySound = PreferencesManager.get(context, "should_play_sound", true)
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
