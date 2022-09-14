package com.example.test.data.retrofit

import com.example.test.domain.Gif

data class GifNetworkEntity(
    val id: String,
    val images: Image? = Image()
) {

    data class Image(
        val original: Original? = Original()
    )

    data class Original(
        var url: String? = null
    )

    fun toGif(): Gif = Gif(
        id = id,
        imageUrl = images?.original?.url
    )
}