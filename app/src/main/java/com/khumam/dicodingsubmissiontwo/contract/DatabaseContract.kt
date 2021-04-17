package com.khumam.dicodingsubmissiontwo.contract

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorites"
            const val _ID = "id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val DATE = "date"
        }
    }
}