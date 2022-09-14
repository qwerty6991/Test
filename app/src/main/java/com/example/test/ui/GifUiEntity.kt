package com.example.test.ui

import com.example.test.domain.Gif


/**
 * Represents data from [Launch] + selection state.
 */
data class GifUiEntity(
    val gif: Gif,
    val inProgress: Boolean
) {

    val id: String get() = gif.id
    val imageUrl: String? get() = gif.imageUrl

}

