package com.example.test.data.room

import android.util.Log
import com.example.test.domain.Gif
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomService @Inject constructor(
    private val gifsDao: GifsDao
) : RoomGifsRepository {
    override suspend fun saveGifs(saveGifs: List<Gif>) {

        val lift = saveGifs.map { it.toRoomGifEntity() }
        Log.i("My_APP", "list GIfs FINISH: $lift")
        //I don't know why they don't add gifs to the database, the lists come in
        gifsDao.saveGifs(saveGifs.map { it.toRoomGifEntity() })
    }
}