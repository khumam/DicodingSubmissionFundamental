package com.khumam.githubuserconsumer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.khumam.githubuserconsumer.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val img: ImageView = findViewById(R.id.splash_img)
        img.alpha = 0f
        img.animate().setDuration(1500).alpha(1f).withEndAction {
            val moveToMain = Intent(this, MainActivity::class.java)
            startActivity(moveToMain)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}