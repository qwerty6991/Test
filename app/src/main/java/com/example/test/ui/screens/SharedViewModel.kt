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
class SharedViewModel @Inject constructor(
    private val gifsRepository: GifsRepository
) : ViewModel() {

    val gifsBySearchFlow: Flow<PagingData<Gif>>

    private val searchLiveData = MutableLiveData("")
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
}