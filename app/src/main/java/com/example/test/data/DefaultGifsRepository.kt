package com.example.test.data

import androidx.paging.*
import com.example.test.data.room.GifsDao
import com.example.test.domain.Gif
import com.example.test.domain.GifsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class DefaultGifsRepository @Inject constructor(
    private val launchesDao: GifsDao,
    private val remoteMediatorFactory: GifsRemoteMediator.Factory
) : GifsRepository {

    override fun getGifs(search: String?): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE
            ),
            remoteMediator = remoteMediatorFactory.create(search = search),
            pagingSourceFactory = { launchesDao.getPagingSource(search) }
        )
            .flow
            //.map { it as PagingData<Gif> }
            .map { pagingData ->
                pagingData.map { gifRoomEntity ->
                    gifRoomEntity
                }
            }
    }

    private companion object {
        const val PAGE_SIZE = 5
    }
}