package com.khumam.githubuserconsumer.helper

import android.database.Cursor
import com.khumam.githubuserconsumer.contract.DatabaseContract
import com.khumam.githubuserconsumer.data.Favorite

class MappingHelper {

    public fun cursorToArraylist(favoriteCursor: Cursor): ArrayList<Favorite> {
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
}