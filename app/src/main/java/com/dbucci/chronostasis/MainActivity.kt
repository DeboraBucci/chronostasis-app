package com.dbucci.chronostasis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import com.dbucci.chronostasis.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.nav_timers -> onTimersClicked()
        R.id.nav_timers_count -> onTimersCountClicked()
        else -> false
    }

    private fun onTimersClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.frame_content, TimersFragment())
        }

        return true
    }

    private fun onTimersCountClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.frame_content, CountFragment())
        }

        return true
    }
}