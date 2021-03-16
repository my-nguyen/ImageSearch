package com.florian_walther.imagesearch

import com.florian_walther.mvvmimagesearch.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashService {

    companion object {
        const val ACCESS_KEY = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    @GET("search/photos")
    suspend fun searchPhotos(@Query("query") query: String,
                             @Query("page") page: Int,
                             @Query("per_page") perPage: Int): Response
}