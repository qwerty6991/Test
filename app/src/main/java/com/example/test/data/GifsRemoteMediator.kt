package com.example.test.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.test.data.retrofit.GifsApi
import com.example.test.data.room.GifRoomEntity
import com.example.test.data.room.GifsDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@ExperimentalPagingApi
class GifsRemoteMediator @AssistedInject constructor(
    private val launchesDao: GifsDao,
    private val launchesApi: GifsApi,
    @Assisted private val search: String?
) : RemoteMediator<Int, GifRoomEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GifRoomEntity>
    ): MediatorResult {

        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize
        val offset = pageIndex * limit

        return try {
            val gifs = fetchGifs(limit, offset)
            if (loadType == LoadType.REFRESH) {
                launchesDao.refresh(search, gifs)
            } else {
                launchesDao.save(gifs)
            }
            MediatorResult.Success(
                endOfPaginationReached = gifs.size < limit
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    private suspend fun fetchGifs(
        limit: Int,
        offset: Int
    ): List<GifRoomEntity> {
        return launchesApi.getGifs(GIPHY_KEY, search, limit, offset, DEFAULT_RATING, DEFAULT_LANG)
            .data
            .map { GifRoomEntity(it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(search: String?): GifsRemoteMediator
    }

    companion object {
        const val GIPHY_KEY = "130J19ub3mYfJDe6WgXyHrUkzJnUmM9D"
        const val DEFAULT_RATING = "g"
        const val DEFAULT_LANG = "en"
    }

}