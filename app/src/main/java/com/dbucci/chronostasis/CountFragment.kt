package com.dbucci.chronostasis

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbucci.chronostasis.databinding.FragmentCountBinding

class CountFragment : Fragment() {
    private lateinit var binding: FragmentCountBinding
    private lateinit var newRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyTimersList: Array<Timer> = arrayOf(
            Timer(
                0,
                25,
                0,
                "simple pomodoro",
                R.drawable.ic_simple_pomodoro_24,
                "Study",
                parseColor("#B524BF")
            ),
            Timer(
                1,
                2,
                34,
                "stopwatch",
                R.drawable.ic_stopwatch_24,
                "Exercise",
                parseColor("#fcba03")
            ),
            Timer(
                0,
                5,
                0,
                "double pomodoro",
                R.drawable.ic_double_pomodoro_24,
                "Reading",
                parseColor("#B524BF")
            )
        )

        newRecyclerView = binding.recyclerViewCountList

        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.adapter = CountListAdapter(dummyTimersList)
    }

    private fun parseColor(color: String): Int {
        return Color.parseColor(color)
    }
}