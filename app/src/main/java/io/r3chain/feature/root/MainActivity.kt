package io.r3chain.feature.root

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.r3chain.R
import io.r3chain.feature.root.ui.App
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var backPressedJob: Job? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (backPressedJob?.isActive == true) {
                // default actions
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            } else {
                // first back button press, show the Toast message
                showExitMessage()
                // start a coroutine to reset the state after delay
                backPressedJob = lifecycleScope.launch {
                    delay(3500L)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setContent {
            App()
        }
    }

    override fun onResume() {
        onBackPressedCallback.isEnabled = true
        super.onResume()
    }

    private fun showExitMessage() {
        Toast
            .makeText(this, getString(R.string.on_back_press_message), Toast.LENGTH_SHORT)
            .show()
    }

}
