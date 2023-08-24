package com.dbucci.chronostasis

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.dbucci.chronostasis.databinding.FragmentTimersBinding
import java.util.Timer
import java.util.TimerTask

class TimersFragment : Fragment() {
    private lateinit var binding: FragmentTimersBinding

    private var timer: Timer? = null

    private var isPomodoro = true
    private var isTimerRunning = false

    private var timerHr = 0
    private var timerMin = 25
    private var timerSec = 0

    private var selectedTimerMin = 25

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        binding.linearLayoutTag.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_layout)

            dialog.show()

            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
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
            requireContext(),
            R.color.light_grey
        )
        val activeColor = ContextCompat.getColorStateList(
            requireContext(),
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
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    if (isPomodoro) startPomodoro()
                    else startStopwatch()

                    setupTimeTextView()
                }
            }
        }, 0L, 1000)
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
            } else if (timerMin == 59 && timerSec == 59) {
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