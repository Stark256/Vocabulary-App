package com.vocabulary.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.vocabulary.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_TIME = 1000L
    }

    private val handler = Handler()
    private val splashRunnable = Runnable { startMainActivity() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDelayedStartActivity()
    }

    private fun performDelayedStartActivity() {
        handler.postDelayed(splashRunnable, SPLASH_TIME)
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(splashRunnable)
    }
}
