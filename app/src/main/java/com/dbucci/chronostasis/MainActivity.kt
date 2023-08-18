package com.dbucci.chronostasis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.dbucci.chronostasis.databinding.ActivityMainBinding
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var timer: Timer? = null

    private var isPomodoro = true
    private var isTimerRunning = false

    private var timerHr = 0
    private var timerMin = 25
    private var timerSec = 0

    private var selectedTimerMin = 25

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonStopStart.setOnClickListener { stopStartResumeHandler() }
        binding.buttonReset.setOnClickListener { resetTimersHandler() }

        val simplePomodoroBtn = binding.imageButtonSimplePomodoro
        val doublePomodoroBtn = binding.imageButtonDoublePomodoro
        val shortBreakBtn = binding.imageButtonShortBreak
        val longBreakBtn = binding.imageButtonLongBreak
        val stopwatchBtn = binding.imageButtonStopwatch

        simplePomodoroBtn.setOnClickListener { changeTimerValuesHandler(25, simplePomodoroBtn) }
        doublePomodoroBtn.setOnClickListener { changeTimerValuesHandler(50, doublePomodoroBtn) }
        shortBreakBtn.setOnClickListener { changeTimerValuesHandler(5, shortBreakBtn) }
        longBreakBtn.setOnClickListener { changeTimerValuesHandler(15, longBreakBtn) }
        stopwatchBtn.setOnClickListener { changeTimerValuesHandler(0, stopwatchBtn, false) }
    }

    private fun changeTimerValuesHandler(
        time: Int,
        button: ImageButton,
        isPomodoroTimer: Boolean = true
    ) {
        isPomodoro = isPomodoroTimer
        timerMin = time
        selectedTimerMin = time

        resetTimersHandler()
        changeImageButtonColors(button)
        setupTimeTextView()
    }

    private fun stopStartResumeHandler() {
        isTimerRunning = !isTimerRunning

        val buttonText = if (isTimerRunning) "Stop" else "Resume"
        binding.buttonStopStart.text = buttonText

        if (isTimerRunning) startTimerHandler() else stopTimer()
    }

    private fun changeImageButtonColors(button: ImageButton) {
        val inactiveColor = ContextCompat.getColorStateList(
            this,
            R.color.light_grey
        )
        val activeColor = ContextCompat.getColorStateList(
            this,
            com.google.android.material.R.color.design_default_color_primary
        )

        binding.imageButtonStopwatch.imageTintList = inactiveColor
        binding.imageButtonSimplePomodoro.imageTintList = inactiveColor
        binding.imageButtonDoublePomodoro.imageTintList = inactiveColor
        binding.imageButtonShortBreak.imageTintList = inactiveColor
        binding.imageButtonLongBreak.imageTintList = inactiveColor

        button.imageTintList = activeColor
    }

    private fun resetTimersHandler() {
        timerHr = 0
        timerMin = if (isPomodoro) selectedTimerMin else 0
        timerSec = 0

        binding.buttonStopStart.text = "Start"

        stopTimer()
        setupTimeTextView()
    }

    private fun startTimerHandler() {
        timer = fixedRateTimer("timer", false, 0L, 1000) {
            this@MainActivity.runOnUiThread {
                if (isPomodoro) startPomodoro()
                else startStopwatch()

                setupTimeTextView()
            }
        }
    }

    private fun startPomodoro() {
        if (timerMin > 0 && timerSec == 0) {
            timerMin--
            timerSec = 60
        }

        if (timerSec > 0) timerSec--
    }

    private fun startStopwatch() {
        if (timerHr >= 99 && timerMin >= 59 && timerSec >= 59) {
            stopTimer()
        } else {
            if (timerSec == 59 && timerMin != 59) {
                timerMin++
                timerSec = 0
            } else if (timerMin == 59 && timerSec == 59 ) {
                timerHr++
                timerMin = 0
                timerSec = 0
            } else {
                timerSec++
            }
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    private fun setupTimeTextView() {
        val hoursStr = String.format("%02d", timerHr)
        val minutesStr = String.format("%02d", timerMin)
        val secondsStr = String.format("%02d", timerSec)

        val finalStr = "${if (!isPomodoro) "$hoursStr:" else ""}${minutesStr}:${secondsStr}"

        binding.textViewTimerValue.text = finalStr
    }
}