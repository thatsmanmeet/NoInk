package com.thatsmanmeet.myapplication.helpers

import android.content.Context
import android.media.MediaPlayer
import com.thatsmanmeet.myapplication.R

open class MusicHelper(private val currentContext:Context) {

    fun deleteSound() {
        val mp = MediaPlayer.create(currentContext, R.raw.delete)
        mp.start()
        mp.setOnCompletionListener {
            it.stop()
            it.reset()
            it.release()
        }
    }
    fun playSound() {
        val mp = MediaPlayer.create(currentContext, R.raw.done)
        mp.start()
        mp.setOnCompletionListener{
            it.stop()
            it.reset()
            it.release()
        }
    }
}