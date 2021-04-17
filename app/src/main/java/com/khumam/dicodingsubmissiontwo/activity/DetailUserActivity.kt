package com.khumam.dicodingsubmissiontwo.activity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.khumam.dicodingsubmissiontwo.*
import com.khumam.dicodingsubmissiontwo.contract.DatabaseContract
import com.khumam.dicodingsubmissiontwo.adapter.SectionsPagerAdapter
import com.khumam.dicodingsubmissiontwo.data.Favorite
import com.khumam.dicodingsubmissiontwo.data.User
import com.khumam.dicodingsubmissiontwo.data.UserDetail
import com.khumam.dicodingsubmissiontwo.databinding.UserDetailBinding
import com.khumam.dicodingsubmissiontwo.helper.FavoriteHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private var favorite: Favorite? = null
    private var position: Int = 0
    private lateinit var favoriteHelper: FavoriteHelper
    private var token: String = "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"

    companion object {
        const val USERNAME = "username"
        const val NAME = "name"
        const val AVATAR = "avatar"
        private val TAB_TITLES = intArrayOf(
                R.string.detailinfo_fragment,
                R.string.follow_fragment,
                R.string.following_fragment
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_detail)
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val sectionsPagerAdapter  = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter         = sectionsPagerAdapter
        val tabs: TabLayout       = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val dataUsername: TextView     = findViewById(R.id.id_username)
        val dataName: TextView         = findViewById(R.id.id_name)
        val dataAvatar: ImageView      = findViewById(R.id.id_avatar)
        val imgFavorite: ImageView     = findViewById(R.id.id_favorite)

        val usernameFromIntent = intent.getStringExtra(USERNAME)
        val nameFromIntent = intent.getStringExtra(NAME)
        val avatarFromIntent = intent.getStringExtra(AVATAR)

        dataUsername.text      = usernameFromIntent
        dataName.text          = nameFromIntent
        Glide.with(this)
                .load(avatarFromIntent)
                .into(dataAvatar)
//
        val checkFavorite = favoriteHelper.queryByUsername(usernameFromIntent.toString())
        if (checkFavorite != null && checkFavorite.getCount() > 0) {
            imgFavorite.setColorFilter(resources.getColor(R.color.red))
        } else {
            imgFavorite.setColorFilter(resources.getColor(R.color.white))
        }

        val favoritBtn: FloatingActionButton = findViewById(R.id.id_favorite)
        favoritBtn.setOnClickListener {
            val usernameData    = usernameFromIntent.toString().trim()
            val nameData        = nameFromIntent.toString().trim()
            val avatarData      = avatarFromIntent.toString().trim()
            val dateData        = getCurrentDate()

            favorite?.username      = usernameData
            favorite?.name          = nameData
            favorite?.avatar        = avatarData
            favorite?.date    = dateData

            val values = ContentValues()
            values.put(DatabaseContract.FavoriteColumns.USERNAME, usernameData)
            values.put(DatabaseContract.FavoriteColumns.NAME, nameData)
            values.put(DatabaseContract.FavoriteColumns.AVATAR, avatarData)
            values.put(DatabaseContract.FavoriteColumns.DATE, dateData)

            val checkUsername = favoriteHelper.queryByUsername(usernameData)
            if (checkUsername != null && checkUsername.getCount() > 0) {
                val result = favoriteHelper.deleteByUsername(usernameData)
                if (result > 0) {
                    Toast.makeText(this, resources.getString(R.string.success_delete), Toast.LENGTH_LONG).show()
                    imgFavorite.setColorFilter(resources.getColor(R.color.white))
                } else {
                    Toast.makeText(this, resources.getString(R.string.error_delete), Toast.LENGTH_SHORT).show()
                }
            } else {
                val result =  favoriteHelper.insert(values)
                if (result > 0) {
                    Toast.makeText(this, resources.getString(R.string.success_added), Toast.LENGTH_LONG).show()
                    imgFavorite.setColorFilter(resources.getColor(R.color.red))
                } else {
                    Toast.makeText(this, resources.getString(R.string.error_added), Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onClick(v: View) {}
}