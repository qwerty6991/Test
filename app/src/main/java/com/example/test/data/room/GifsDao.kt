package com.example.test.data.room

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface GifsDao {

    @Query("SELECT * FROM gifs WHERE :search")
    fun getPagingSource(
        search: String?
    ): PagingSource<Int, GifRoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(launches: List<GifRoomEntity>)

    @Query("DELETE FROM gifs WHERE :search")
    suspend fun clear(search: String?)

    @Transaction
    suspend fun refresh(search: String?, launches: List<GifRoomEntity>) {
        clear(search)
        save(launches)
    }

}