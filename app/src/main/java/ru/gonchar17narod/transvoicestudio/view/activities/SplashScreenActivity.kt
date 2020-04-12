package ru.gonchar17narod.transvoicestudio.view.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.gonchar17narod.transvoicestudio.R

class SplashScreenActivity : AppCompatActivity() {

    private val PERMISSION_MICRO_CODE = 119

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_MICRO_CODE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    proceedToNextDestination()
                } else {
                    finish()
                }
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            proceedToNextDestination()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                PERMISSION_MICRO_CODE
            )
        }
    }

    private fun proceedToNextDestination() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
        finish()
    }
}
