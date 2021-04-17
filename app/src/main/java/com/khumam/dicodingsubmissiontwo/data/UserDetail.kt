package com.khumam.dicodingsubmissiontwo.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    var username: String?,
    var name: String?,
    var location: String?,
    var repository: String?,
    var company: String?,
    var followers: String?,
    var following: String?,
    var avatar: String?,
) : Parcelable