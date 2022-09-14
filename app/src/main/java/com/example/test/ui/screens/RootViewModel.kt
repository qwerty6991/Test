package com.example.test.ui.screens

import androidx.annotation.StringRes
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.test.domain.Gif
import com.example.test.domain.GifsRepository
import com.example.test.ui.base.MutableLiveEvent
import com.example.test.ui.base.publishEvent
import com.example.test.ui.base.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RootViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gifsRepository: GifsRepository,
) : ViewModel() {

    val gifsBySearchFlow: Flow<PagingData<Gif>>

    private val _scrollEvents = MutableLiveEvent<Unit>()
    val scrollEvents = _scrollEvents.share()

    private val _errorEvents = MutableLiveEvent<Int>()
    val errorEvents = _errorEvents.share()

    private val _toastEvent = MutableLiveEvent<Int>()
    val toastEvent = _toastEvent.share()

    private var _invalidateEvents = MutableLiveEvent<Unit>()
    val invalidateEvents = _invalidateEvents.share()

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

    private fun scrollListToTop() {
        _scrollEvents.publishEvent(Unit)
    }


    private fun invalidateList() {
        _invalidateEvents.publishEvent(Unit)
    }

    private fun showError(@StringRes errorMessage: Int) {
        _errorEvents.publishEvent(errorMessage)
    }

    private companion object {
        const val KEY_YEAR = "KEY_SEARCH"
    }
}