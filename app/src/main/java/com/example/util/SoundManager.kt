package com.example.util

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SoundManager(private val vibrator: Vibrator? = null) {
    private var toneGenerator: ToneGenerator? = null
    private var isMuted = false

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    fun toggleMute(): Boolean {
        isMuted = !isMuted
        return isMuted
    }

    fun playTick() {
        if (isMuted) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 40)
            } catch (_: Exception) {}
        }
    }

    fun playSuccess() {
        if (isMuted) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
            } catch (_: Exception) {}
        }
        vibrate(60)
    }

    fun playBuzzer() {
        if (isMuted) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 300)
            } catch (_: Exception) {}
        }
        vibrate(200)
    }

    fun playShutter() {
        if (isMuted) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 100)
            } catch (_: Exception) {}
        }
        vibrate(80)
    }

    fun playPop() {
        if (isMuted) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 60)
            } catch (_: Exception) {}
        }
        vibrate(30)
    }

    fun vibrate(milliseconds: Long = 50) {
        try {
            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(milliseconds)
                }
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            toneGenerator?.release()
        } catch (_: Exception) {}
    }
}
