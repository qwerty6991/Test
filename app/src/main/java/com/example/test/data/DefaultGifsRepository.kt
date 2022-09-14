package com.example.test.data

import androidx.paging.*
import com.example.test.data.retrofit.GifsApi
import com.example.test.data.room.RoomService
import com.example.test.domain.Gif
import com.example.test.domain.GifsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class DefaultGifsRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val gifsApi: GifsApi,
    private val roomService: RoomService
) : GifsRepository {

    override fun getGifs(search: String?): Flow<PagingData<Gif>> {
        val loader: GifsPageLoader = { pageIndex, pageSize ->
            getGifsApi(pageIndex, pageSize, search)
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { GifsPagingSource(loader, roomService) }
        ).flow
    }

    private suspend fun getGifsApi(
        pageIndex: Int,
        pageSize: Int,
        search: String?
    ): List<Gif> =
        withContext(ioDispatcher) {

            val offset = pageIndex * pageSize

            return@withContext gifsApi.getGifs(
                GIPHY_KEY,
                search,
                pageSize,
                offset,
                DEFAULT_RATING,
                DEFAULT_LANG
            ).data.map { it.toGif() }
        }

    private companion object {
        const val PAGE_SIZE = 6
        const val GIPHY_KEY = "130J19ub3mYfJDe6WgXyHrUkzJnUmM9D"
        const val DEFAULT_RATING = "g"
        const val DEFAULT_LANG = "en"
    }
}