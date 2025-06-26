package com.example.inventory.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.inventory.databinding.ActivitySplashBinding
import com.example.inventory.tools.SharedPrefHelper
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var circularProgress: ProgressBar
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        SharedPrefHelper.init(this)
        auth = FirebaseAuth.getInstance()

        val isNightMode = SharedPrefHelper.isNightMode()
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        loading()
    }

    private fun loading() {
        circularProgress = binding.circularProgress

        val duration = 2000L // 2 secondes
        val interval = 50L

        object : CountDownTimer(duration, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val progress = ((duration - millisUntilFinished) * 100 / duration).toInt()
                circularProgress.progress = progress
            }

            override fun onFinish() {
                circularProgress.progress = 100

                val currentUser = auth.currentUser
                val intent = if (currentUser != null) {
                    Intent(context, MainActivity::class.java)
                } else {
                    Intent(context, LoginActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }.start()
    }
}
