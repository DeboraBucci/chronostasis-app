package com.dbucci.chronostasis

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        binding.buttonStopStart.setOnClickListener {
            isTimerRunning = !isTimerRunning

            val buttonText = if (isTimerRunning) "Stop" else "Resume"
            binding.buttonStopStart.text = buttonText

            if (isTimerRunning) startTimer() else stopTimer()
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        if (isTimerRunning) {
            timer = fixedRateTimer("timer", false, 0L, 1000) {
                this@MainActivity.runOnUiThread {
                    if (timerMin > 0 && timerSec == 0) {
                        timerMin--
                        timerSec = 60
                    }

                    if (timerSec > 0) timerSec--

                    val minutesStr = String.format("%02d", timerMin)
                    val secondsStr = String.format("%02d", timerSec)

                    binding.textViewTimerValue.text = "${minutesStr}:${secondsStr}"
                }
            }
        }
    }
}