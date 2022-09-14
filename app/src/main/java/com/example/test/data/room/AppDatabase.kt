package com.example.test.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        GifRoomEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getLaunchesDao(): GifsDao

}