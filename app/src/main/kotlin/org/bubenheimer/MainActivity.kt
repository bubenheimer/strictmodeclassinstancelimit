package org.bubenheimer

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.StrictMode
import android.os.SystemClock
import android.os.strictmode.Violation
import android.util.Log
import java.util.concurrent.Executors

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val executor = Executors.newSingleThreadExecutor()

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .setClassInstanceLimit(Application::class.java, 0)
                .penaltyListener(executor, OnVmViolationListener)
                .build()
        )

        Thread {
            SystemClock.sleep(3_000L)

            Runtime.getRuntime().gc()
        }.start()
    }
}

private object OnVmViolationListener : StrictMode.OnVmViolationListener {
    override fun onVmViolation(violation: Violation) {
        Log.e("MainActivity", "Violation: $violation")
    }
}
