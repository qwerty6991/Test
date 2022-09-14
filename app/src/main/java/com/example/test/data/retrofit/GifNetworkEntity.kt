package com.example.test.data.retrofit

import com.example.test.domain.Gif

data class GifNetworkEntity(
    override val id: String,
    val images: Image?
) : Gif {

    override val imageUrl: String get() = images?.original?.url ?: ""

}

data class Image(
    val original: Original?
)

data class Original(
    var url: String? = null
)