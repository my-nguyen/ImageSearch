package com.florian_walther.mvvmimagesearch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(val results: List<Result>): Parcelable

@Parcelize
data class Result(val id: String, val description: String?, val user: User, val urls: Urls): Parcelable

@Parcelize
data class User(val name: String, val username: String): Parcelable {
    val attributionUrl
        get() = "https://unsplash.com/$username?utm_source=MVVMImageSearch&utm_medium=referral"
}

@Parcelize
data class Urls(val raw: String, val full: String, val regular: String, val small: String, val thumb: String): Parcelable
