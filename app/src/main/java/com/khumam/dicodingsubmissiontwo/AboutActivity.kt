package com.khumam.dicodingsubmissiontwo

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat


class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val aboutImg: ImageView = findViewById(R.id.about_img)
        val aboutName: TextView = findViewById(R.id.about_name)
        val aboutEmail: TextView = findViewById(R.id.about_email)
        val img: Drawable? =  ResourcesCompat.getDrawable(resources, R.drawable.my, null)
        aboutImg.setImageDrawable(img)

        aboutName.text = getString(R.string.author)
        aboutEmail.text = getString(R.string.email)
    }
}