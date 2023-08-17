package com.dbucci.chronostasis

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding.imageButtonSimplePomodoro.setOnClickListener { changeTimerValue(25) }
        binding.imageButtonDoublePomodoro.setOnClickListener { changeTimerValue(50) }
        binding.imageButtonShortBreak.setOnClickListener { changeTimerValue(5) }
        binding.imageButtonLongBreak.setOnClickListener { changeTimerValue(15) }

        binding.imageButtonStopwatch.setOnClickListener {}
    }

    private fun changeTimerValue(time: Int) {
        resetTimerHandler()

        timerMin = time
        setupPomodoroTextView()
    }

    private fun pauseAndResumeHandler() {
        isTimerRunning = !isTimerRunning

        val buttonText = if (isTimerRunning) "Stop" else "Resume"
        binding.buttonStopStart.text = buttonText

        if (isTimerRunning) startTimer() else stopTimer()
    }

    private fun resetTimerHandler() {
        timerMin = 25
        timerSec = 0
        binding.buttonStopStart.text = "Start"

        stopTimer()
        setupPomodoroTextView()
    }

    private fun startTimer() {
        timer = fixedRateTimer("pomodoro-timer", false, 0L, 1000) {
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