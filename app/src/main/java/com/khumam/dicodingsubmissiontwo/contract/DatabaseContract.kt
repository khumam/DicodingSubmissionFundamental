package com.khumam.dicodingsubmissiontwo.contract

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    companion object {
        const val AUTHORITY = "com.khumam.dicodingsubmissiontwo.githubuserapp"
        const val SCHEME = "content"
    }

    internal class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorites"
            const val _ID = "id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val DATE = "date"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}