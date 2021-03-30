package com.khumam.dicodingsubmissiontwo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val DATAUSER = "datauser"
        private val TAB_TITLES = intArrayOf(
            R.string.follow_fragment,
            R.string.following_fragment
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_detail)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val dataUsername: TextView     = findViewById(R.id.id_username)
        val dataName: TextView         = findViewById(R.id.id_name)
        val dataLocation: TextView     = findViewById(R.id.id_location)
        val dataAvatar: ImageView      = findViewById(R.id.id_avatar)

        val data = intent.getParcelableExtra<User>(DATAUSER) as User

        dataUsername.text      = data.username.toString()
        dataName.text          = data.name.toString()
        dataLocation.text      = data.location.toString()
        Glide.with(this)
                .load(data.avatar)
                .into(dataAvatar)

    }

    override fun onClick(v: View) {
    }
}