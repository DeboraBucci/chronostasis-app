package com.dbucci.chronostasis

import androidx.fragment.app.FragmentActivity
import java.util.Timer
import java.util.TimerTask

class MyTimer(
    private var type: String,
    private var tag: String? = null,
    private var color: Int = com.google.android.material.R.color.design_default_color_primary,
) {
    private var timer: Timer? = null
    private var isTimerRunning = false
    private var icon: Int = 0

    private var hs: Int = 0
    private var min: Int = 0
    private var secs: Int = 0

    init {
        initHandler()
    }

    private fun initHandler() {
        hs = 0
        secs = 0

        when (type) {
            "pomodoro" -> {
                min = 25
                icon = R.drawable.ic_simple_pomodoro_24
            }

            "double-pomodoro" -> {
                min = 50
                icon = R.drawable.ic_double_pomodoro_24
            }

            "short-break" -> {
                min = 5
                icon = R.drawable.ic_short_break_24
            }

            "long-break" -> {
                min = 15
                icon = R.drawable.ic_long_break_24
            }

            "stopwatch" -> icon = R.drawable.ic_stopwatch_24
        }
    }

    fun getTime(): String {
        val hoursStr = String.format("%02d", hs)
        val minutesStr = String.format("%02d", min)
        val secondsStr = String.format("%02d", secs)

        return "${if (type == "stopwatch") "$hoursStr:" else ""}${minutesStr}:${secondsStr}"
    }

    fun startTimer(activity: FragmentActivity, setupTimeTextView: () -> Unit) {
        isTimerRunning = true
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity.runOnUiThread {
                    if (type.equals("stopwatch", true)) startStopwatch()
                    else startPomodoro()

                    setupTimeTextView()
                }
            }
        }, 0L, 1000)
    }

    private fun startPomodoro() {
        if (min > 0 && secs == 0) {
            min--
            secs = 60
        }

        if (secs > 0) secs--
    }

    private fun startStopwatch() {
        if (hs >= 99 && min >= 59 && secs >= 59) {
            stopTimer()
        } else {
            if (secs == 59 && min != 59) {
                min++
                secs = 0
            } else if (min == 59 && secs == 59) {
                hs++
                min = 0
                secs = 0
            } else {
                secs++
            }
        }
    }

    fun resetTimer() {
        initHandler()
        stopTimer()
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    fun getType(): String {
        return this.type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getIcon(): Int {
        return this.icon
    }

    fun setIcon(icon: Int) {
        this.icon = icon
    }

    fun getTag(): String? {
        return this.tag
    }

    fun setTag(tag: String) {
        this.tag = tag
    }

    fun getColor(): Int {
        return this.color
    }

    fun setColor(color: Int) {
        this.color = color
    }
}