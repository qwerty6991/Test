package com.example.test.di

import androidx.paging.ExperimentalPagingApi
import com.example.test.data.DefaultGifsRepository
import com.example.test.data.room.RoomGifsRepository
import com.example.test.data.room.RoomService
import com.example.test.domain.GifsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindGifsRepository(
        gifsRepository: DefaultGifsRepository
    ): GifsRepository

    @Binds
    fun bindRoomGifsRepository(
        roomGifsRepository: RoomService
    ): RoomGifsRepository

}