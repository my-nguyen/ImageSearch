package com.florian_walther.imagesearch

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(val service: UnsplashService) {
    /*fun searchPhotos(query: String) {
        // pageSize: the number of items to fetch and load into the RecyclerView each time.
        //           this will be passed as params.loadSize in service.searchPhotos() inside Paging.load().
        // maxSize: the max number of items the RecyclerView can contain
        // enablePlaceholders: whether the RecyclerView displays placeholders for items still loading
        val config = PagingConfig(pageSize=20, maxSize=100, enablePlaceholders=false)
        val pagingSourceFactory = { Paging(service, query) }
        Pager(config=config, pagingSourceFactory=pagingSourceFactory).liveData
    }*/
    fun searchPhotos(query: String) =
        Pager(config=PagingConfig(pageSize=20, maxSize=100, enablePlaceholders=false),
            pagingSourceFactory={ Paging(service, query) }
        ).liveData
}