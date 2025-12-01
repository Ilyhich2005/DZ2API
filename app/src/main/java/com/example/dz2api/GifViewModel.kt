package com.example.dz2api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay





class GifViewModel : ViewModel() {
    val apiKey = BuildConfig.GIPHY_API_KEY

    val gifUrl = mutableStateListOf<String>()
    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var isLoadingMore by mutableStateOf(false)
        private set

    private var currentPage = 0
    private val pageSize = 2

    init{
        loadGif()
    }
    fun loadGif() {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null
                repeat(4) {
                    delay(2000)
                    val response = RetrofitInstance.api.getRandomGif(
                        apiKey = apiKey,
                        tag = "",
                        rating = "g"
                    )
                    response.data.images.original.url.let { gifUrl.add(it) }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                error = "Ошибка сети или сервера"
            }
            finally {
                isLoading = false
            }
        }
    }
    fun loadNextPage() {
        viewModelScope.launch {
            try {
                isLoadingMore = true
                error = null
                delay(5000) // имитация долгой загрузки

                repeat(pageSize) {
                    delay(2000)
                    val response = RetrofitInstance.api.getRandomGif(
                        apiKey = apiKey,
                        tag = "",
                        rating = "g"
                    )
                    response.data.images.original.url.let { gifUrl.add(it) }
                }

                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
                error = "Ошибка загрузки следующей страницы"
            } finally {
                isLoadingMore = false
            }
        }
    }
}