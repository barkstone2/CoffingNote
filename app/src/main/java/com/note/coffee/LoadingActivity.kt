package com.note.coffee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalView

class LoadingActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = application as MainApplication

        Log.d("LoadingActivity", "start")

        setContent {
            val content = LocalView.current
            content.viewTreeObserver.addOnPreDrawListener { false }
        }

        application.loadAd(
            onAdLoaded = { startMainActivity() },
            onAdLoadFailed = { startMainActivity() }
        )

    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}