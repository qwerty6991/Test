package com.example.test.data.room

import com.example.test.domain.Gif

interface RoomGifsRepository {

    suspend fun saveGifs(saveGifs: List<Gif>)
}