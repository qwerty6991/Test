package com.example.test.ui

import com.example.test.domain.Gif


/**
 * Represents data from [Launch] + selection state.
 */
data class GifUiEntity(
    val gif: Gif,
    val inProgress: Boolean
) : Gif {

    override val id: String
        get() = gif.id

    override val imageUrl: String
        get() = gif.imageUrl
}

