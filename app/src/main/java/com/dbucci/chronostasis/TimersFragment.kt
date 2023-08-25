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

class TimersFragment : Fragment() {
    private lateinit var binding: FragmentTimersBinding
    private var myTimer: MyTimer? = null
    private var isTimerRunning = false

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

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
    }

    private fun setupClickListeners() {
        binding.buttonStopStart.setOnClickListener { stopStartResumeHandler() }
        binding.buttonReset.setOnClickListener { resetTimersHandler() }

        myTimer = MyTimer(
            "pomodoro",
            binding.textViewTagName.text.toString(),
            androidx.constraintlayout.widget.R.color.abc_color_highlight_material
        )

        val simplePomodoroBtn = binding.imageButtonSimplePomodoro
        val doublePomodoroBtn = binding.imageButtonDoublePomodoro
        val shortBreakBtn = binding.imageButtonShortBreak
        val longBreakBtn = binding.imageButtonLongBreak
        val stopwatchBtn = binding.imageButtonStopwatch

        simplePomodoroBtn.setOnClickListener {
            changeTimerValuesHandler(
                "pomodoro",
                simplePomodoroBtn
            )
        }
        doublePomodoroBtn.setOnClickListener {
            changeTimerValuesHandler(
                "double-pomodoro",
                doublePomodoroBtn
            )
        }
        shortBreakBtn.setOnClickListener {
            changeTimerValuesHandler(
                "short-break",
                shortBreakBtn
            )
        }
        longBreakBtn.setOnClickListener {
            changeTimerValuesHandler(
                "long-break",
                longBreakBtn
            )
        }
        stopwatchBtn.setOnClickListener {
            changeTimerValuesHandler(
                "stopwatch",
                stopwatchBtn
            )
        }
    }

    private fun changeTimerValuesHandler(
        type: String,
        button: ImageButton,
    ) {
        myTimer = MyTimer(
            type,
            binding.textViewTagName.text.toString(),
            androidx.constraintlayout.widget.R.color.abc_color_highlight_material
        )

        resetTimersHandler()
        changeImageButtonColors(button)
        setupTimeTextView()
    }

    private fun stopStartResumeHandler() {
        isTimerRunning = !isTimerRunning

        val buttonText = if (isTimerRunning) "Stop" else "Resume"
        binding.buttonStopStart.text = buttonText

        if (isTimerRunning) myTimer?.startTimer(requireActivity(), ::setupTimeTextView) else myTimer?.stopTimer()
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
        isTimerRunning = false
        myTimer?.resetTimer()
        binding.buttonStopStart.text = "Start"
        setupTimeTextView()
    }


    private fun setupTimeTextView() {
        binding.textViewTimerValue.text = myTimer?.getTime()
    }
}