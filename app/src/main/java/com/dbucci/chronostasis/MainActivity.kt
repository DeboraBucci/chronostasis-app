package com.dbucci.chronostasis

import android.annotation.SuppressLint
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
    private var isTimerRunning = false
    private var timerMin = 25
    private var timerSec = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonStopStart.setOnClickListener { pauseAndResumeHandler() }
        binding.buttonReset.setOnClickListener { resetTimerHandler() }

        val simplePomodoroBtn = binding.imageButtonSimplePomodoro
        val doublePomodoroBtn = binding.imageButtonDoublePomodoro
        val shortBreakBtn = binding.imageButtonShortBreak
        val longBreakBtn = binding.imageButtonLongBreak
        val stopwatchBtn = binding.imageButtonStopwatch

        simplePomodoroBtn.setOnClickListener { changeTimerValue(25, simplePomodoroBtn) }
        doublePomodoroBtn.setOnClickListener { changeTimerValue(50, doublePomodoroBtn) }
        shortBreakBtn.setOnClickListener { changeTimerValue(5, shortBreakBtn) }
        longBreakBtn.setOnClickListener { changeTimerValue(15, longBreakBtn) }

        binding.imageButtonStopwatch.setOnClickListener {}
    }

    private fun changeTimerValue(time: Int, button: ImageButton) {
        resetTimerHandler()
        changeImageButtonColors(button)

        timerMin = time
        setupPomodoroTextView()
    }

    private fun pauseAndResumeHandler() {
        isTimerRunning = !isTimerRunning

        val buttonText = if (isTimerRunning) "Stop" else "Resume"
        binding.buttonStopStart.text = buttonText

        if (isTimerRunning) startTimer() else stopTimer()
    }

    private fun changeImageButtonColors(button: ImageButton) {
        val inactiveColor = ContextCompat.getColorStateList(this, R.color.light_grey)
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

    private fun resetTimerHandler() {
        timerMin = 25
        timerSec = 0
        binding.buttonStopStart.text = "Start"

        stopTimer()
        setupPomodoroTextView()
    }

    private fun startTimer() {
        timer = fixedRateTimer("timer", false, 0L, 1000) {
            this@MainActivity.runOnUiThread {
                if (timerMin > 0 && timerSec == 0) {
                    timerMin--
                    timerSec = 60
                }

                if (timerSec > 0) timerSec--

                setupPomodoroTextView()
            }
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    @SuppressLint("SetTextI18n")
    private fun setupPomodoroTextView() {
        val minutesStr = String.format("%02d", timerMin)
        val secondsStr = String.format("%02d", timerSec)

        binding.textViewTimerValue.text = "${minutesStr}:${secondsStr}"
    }
}