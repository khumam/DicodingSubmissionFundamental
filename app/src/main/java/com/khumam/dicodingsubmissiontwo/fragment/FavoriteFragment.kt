package com.khumam.dicodingsubmissiontwo.fragment

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khumam.dicodingsubmissiontwo.contract.DatabaseContract
import com.khumam.dicodingsubmissiontwo.helper.FavoriteHelper
import com.khumam.dicodingsubmissiontwo.R
import com.khumam.dicodingsubmissiontwo.ViewBindingHolder
import com.khumam.dicodingsubmissiontwo.ViewBindingHolderImpl
import com.khumam.dicodingsubmissiontwo.activity.DetailUserActivity
import com.khumam.dicodingsubmissiontwo.activity.MainActivity
import com.khumam.dicodingsubmissiontwo.adapter.FavoriteAdapter
import com.khumam.dicodingsubmissiontwo.adapter.userAdapter
import com.khumam.dicodingsubmissiontwo.data.Favorite
import com.khumam.dicodingsubmissiontwo.data.User
import com.khumam.dicodingsubmissiontwo.databinding.FragmentFavoriteBinding

class FavoritFragment : Fragment(), ViewBindingHolder<FragmentFavoriteBinding> by ViewBindingHolderImpl() {

    private val listFavorite: ArrayList<Favorite> = ArrayList()
    private lateinit var rvFavotire: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var position: Int = 0
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = initBinding(FragmentFavoriteBinding.inflate(inflater), this) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvFavorite?.setHasFixedSize(true)

        getListFavorite()
    }

    private fun getListFavorite() {
        binding?.progressBarFavorite?.visibility = View.VISIBLE
        favoriteHelper = FavoriteHelper.getInstance((activity as MainActivity).applicationContext)
        favoriteHelper.open()

        val cursor = favoriteHelper.queryAll()
        val defferedFavorites = cursorToArraylist(cursor)
        favoriteHelper.close()

        binding?.progressBarFavorite?.visibility = View.INVISIBLE
        val favorites = defferedFavorites
        if (favorites.size > 0) {
            binding?.rvFavorite?.layoutManager = LinearLayoutManager(activity)
            val favoriteAdapter = FavoriteAdapter(favorites)
            binding?.rvFavorite?.adapter = favoriteAdapter

            favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Favorite) {
                    showSelectedUser(data)
                }
            })
        }
    }

    private fun cursorToArraylist(favoriteCursor: Cursor): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val id          = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val username    = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val name        = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val avatar      = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val date        = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DATE))

                favoriteList.add(Favorite(id, username, name, avatar, date))
            }
        }

        return favoriteList
    }

    private fun showSelectedUser(favorite: Favorite) {
        val userdetail = Intent(activity, DetailUserActivity::class.java)
        userdetail.putExtra(DetailUserActivity.USERNAME, favorite.username)
        userdetail.putExtra(DetailUserActivity.NAME, favorite.name)
        userdetail.putExtra(DetailUserActivity.AVATAR, favorite.avatar)
        startActivity(userdetail)
    }
}