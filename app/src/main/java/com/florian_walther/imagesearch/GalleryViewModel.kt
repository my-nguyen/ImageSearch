package com.florian_walther.imagesearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(val repository: Repository): ViewModel() {
    companion object {
        const val DEFAULT_QUERY = "cats"
    }

    val currentQuery = MutableLiveData(DEFAULT_QUERY)
    val photos = currentQuery.switchMap { query ->
        repository.searchPhotos(query).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }
}