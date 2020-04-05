package ru.gonchar17narod.selferificator.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gonchar17narod.selferificator.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()

        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
        finish()
    }
}
