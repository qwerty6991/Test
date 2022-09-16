package com.example.test.ui.screens

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.test.domain.Gif
import com.example.test.domain.GifsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RootViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gifsRepository: GifsRepository
) : ViewModel() {

    val gifsBySearchFlow: Flow<PagingData<Gif>>

    private val searchLiveData =
        savedStateHandle.getLiveData(KEY_YEAR, "")
    var search: String?
        get() = searchLiveData.value
        set(value) {
            searchLiveData.value = value
        }

    init {
        gifsBySearchFlow = searchLiveData.asFlow()
            .debounce(500)
            .flatMapLatest {
                gifsRepository.getGifs(it)
            }
            .cachedIn(viewModelScope)
    }

    private companion object {
        const val KEY_YEAR = "KEY_SEARCH"
    }
}