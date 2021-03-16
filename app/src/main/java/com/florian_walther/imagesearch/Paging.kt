package com.florian_walther.imagesearch

import androidx.paging.PagingSource
import com.florian_walther.mvvmimagesearch.Result
import retrofit2.HttpException
import java.io.IOException

class Paging(val service: UnsplashService, val query: String): PagingSource<Int, Result>() {
    companion object {
        const val STARTING_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
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
}