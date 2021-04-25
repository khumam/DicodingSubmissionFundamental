package com.khumam.dicodingsubmissiontwo.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.khumam.dicodingsubmissiontwo.contract.DatabaseContract
import com.khumam.dicodingsubmissiontwo.contract.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.khumam.dicodingsubmissiontwo.helper.FavoriteHelper

class MyFavoriteProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        val deleteFavorite: Int = when (FAVORITE_USERNAME) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleteFavorite
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val addFavorite: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$addFavorite")
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> favoriteHelper.queryAll()
            FAVORITE_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            FAVORITE_USERNAME -> favoriteHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return 0
    }

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private const val FAVORITE_USERNAME = -1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.FavoriteColumns.TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(DatabaseContract.AUTHORITY, "${DatabaseContract.FavoriteColumns.TABLE_NAME}/#", FAVORITE_ID)
        }
    }
}