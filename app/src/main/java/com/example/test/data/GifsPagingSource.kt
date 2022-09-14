package com.example.test.data

import android.util.Log
import androidx.paging.*
import com.example.test.data.room.RoomService
import com.example.test.domain.Gif
import javax.inject.Inject
import javax.inject.Singleton

typealias GifsPageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<Gif>

@ExperimentalPagingApi
@Singleton
class GifsPagingSource @Inject constructor(
    private val loader: GifsPageLoader,
    private val roomService: RoomService,
) : PagingSource<Int, Gif>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val pageIndex = params.key ?: 0

        return try {
            val gifs = loader.invoke(pageIndex, params.loadSize)
            Log.i("My_APP", "list GIfs: $gifs")

            roomService.saveGifs(gifs)

            return LoadResult.Page(
                data = gifs,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (gifs.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    companion object {
        const val GIPHY_KEY = "130J19ub3mYfJDe6WgXyHrUkzJnUmM9D"
        const val DEFAULT_RATING = "g"
        const val DEFAULT_LANG = "en"
    }

}