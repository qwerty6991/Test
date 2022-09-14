package com.example.test.data.room

import androidx.room.*

@Dao
interface GifsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGifs(gifs: List<GifRoomEntity>)
}