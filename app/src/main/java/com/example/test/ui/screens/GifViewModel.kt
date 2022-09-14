package com.example.test.ui.screens

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.test.domain.Gif
import com.example.test.domain.GifsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class GifViewModel @Inject constructor(
    private val gifsRepository: GifsRepository
) : ViewModel() {

    val gifsFlow: Flow<PagingData<Gif>>

    var searchName = MutableLiveData("")

    init {
        gifsFlow = searchName.asFlow()
            .debounce(500)
            .flatMapLatest {
                gifsRepository.getGifs(it)
            }
            .cachedIn(viewModelScope)
    }

    fun setSearchName(value: String?) {
        if (searchName.value == value) return
        searchName.value = value
    }

}