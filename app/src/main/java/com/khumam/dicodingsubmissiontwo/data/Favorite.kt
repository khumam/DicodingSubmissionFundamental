package com.khumam.dicodingsubmissiontwo.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite(
        var id: Int?,
        var username: String?,
        var name: String?,
        var avatar: String?,
        var date: String?
) : Parcelable