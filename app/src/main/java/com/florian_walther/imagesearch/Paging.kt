package com.florian_walther.imagesearch

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.florian_walther.mvvmimagesearch.Photo
import retrofit2.HttpException
import java.io.IOException

class Paging(val service: UnsplashService, val query: String): PagingSource<Int, Photo>() {
    companion object {
        const val STARTING_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_PAGE
        return try {
            val response = service.searchPhotos(query, position, params.loadSize)
            val data = response.results
            val prevPage = if (position == STARTING_PAGE) null else position - 1
            val nextPage = if (data.isEmpty()) null else position + 1
            LoadResult.Page(data, prevPage, nextPage)
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        TODO("Not yet implemented")
    }
}