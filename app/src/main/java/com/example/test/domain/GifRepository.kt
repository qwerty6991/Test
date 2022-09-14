package com.example.test.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GifRepository {

    fun getGifs(searchBy: String): Flow<PagingData<Gif>>

}