package com.dbucci.chronostasis

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dbucci.chronostasis.databinding.ActivityMainBinding
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var timerMin = 1
    var timerSec = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStopStart.setOnClickListener {
            startTimer()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        fixedRateTimer("timer", false, 0L, 1000) {
            this@MainActivity.runOnUiThread {
                if (timerMin > 0 && timerSec == 0) {
                    timerMin--
                    timerSec = 12
                }

                if (timerSec > 0) {
                    timerSec--
                }

                val minutesStr = String.format("%02d", timerMin)
                val secondsStr = String.format("%02d", timerSec)

                binding.textViewTimerValue.text = "${minutesStr}:${secondsStr}"
            }
        }
    }
}