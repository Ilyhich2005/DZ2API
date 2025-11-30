package com.example.dz2api

data class RandomGifResponse(
    val data: GifData
)

data class GifData(
    val images: Images
)

data class Images(
    val original: Original
)

data class Original(
    val url: String
)

