package com.example.test.ui.screens

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.test.data.room.RoomGifsRepository
import com.example.test.domain.Gif
import com.example.test.domain.GifsRepository
import com.example.test.ui.GifUiEntity
import com.example.test.ui.Selections
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
    private val roomGifsRepository: RoomGifsRepository
) : ViewModel() {

    val gifsFlow: Flow<PagingData<GifUiEntity>>

    private val localChanges = LocalChanges()
    private val localChangesFlow = MutableStateFlow(OnChange(localChanges))

    private val _scrollEvents = MutableLiveEvent<Unit>()
    val scrollEvents = _scrollEvents.share()

    private val _errorEvents = MutableLiveEvent<Int>()
    val errorEvents = _errorEvents.share()

    private val _toastEvent = MutableLiveEvent<Int>()
    val toastEvent = _toastEvent.share()

    private val selections = Selections()

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
        val originGifsFlow = searchLiveData.asFlow()
            // if user types text too quickly -> filtering intermediate values to avoid excess loads
            .debounce(500)
            .flatMapLatest {
                Log.i("My_APP", "search name: $it")
                gifsRepository.getGifs(it)
            }
            .cachedIn(viewModelScope)


        gifsFlow = combine(
            originGifsFlow,
            localChangesFlow.debounce(50),
            this::merge
        )
    }

    private fun setProgress(Gif: GifUiEntity, inProgress: Boolean) {
        if (inProgress) {
            localChanges.idsInProgress.add(Gif.id)
        } else {
            localChanges.idsInProgress.remove(Gif.id)
        }
        localChangesFlow.value = OnChange(localChanges)
    }

    private fun isInProgress(gif: GifUiEntity) =
        localChanges.idsInProgress.contains(gif.id)

    private suspend fun delete(Gif: GifUiEntity) {
//        usersRepository.delete(Gif.imageUrl)
        invalidateList()
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

    private fun merge(
        gifs: PagingData<Gif>,
        localChanges: OnChange<LocalChanges>
    ): PagingData<GifUiEntity> {
        return gifs.map { gif ->
            val isInProgress = localChanges.value.idsInProgress.contains(gif.id)
            GifUiEntity(gif = gif, isInProgress)
        }
    }


    class OnChange<T>(val value: T)

    /**
     * Contains:
     * 1) identifiers of items which are processed now (deleting or favorite
     * flag updating).
     * 2) local favorite flag updates to avoid list reloading
     */
    class LocalChanges {
        val idsInProgress = mutableSetOf<String>()
        val flags = mutableMapOf<String, Boolean>()
    }

    private companion object {
        const val KEY_YEAR = "KEY_SEARCH"
    }
}