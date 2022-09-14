package com.example.test.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GifsRepository {

    fun getGifs(search: String? = null): Flow<PagingData<Gif>>

}