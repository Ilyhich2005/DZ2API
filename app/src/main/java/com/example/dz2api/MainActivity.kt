package com.example.dz2api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi


class MainActivity : ComponentActivity() {

    private val API_KEY = "5rP5TNDO03bZz9TpaVmEb1av2AY3ty30"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomGifScreen(apiKey = API_KEY)
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RandomGifScreen(apiKey: String) {

    var gifUrl by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        gifUrl?.let {
            GlideImage(
                model = it,
                contentDescription = "Random GIF",
                modifier = Modifier
                    .size(250.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            scope.launch {
                try {
                    val response = RetrofitInstance.api.getRandomGif(apiKey)
                    gifUrl = response.data.images.original.url
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }) {
            Text("Показать рандомный GIF")
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RandomGifScreenPreviu() {

    var gifUrl by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        gifUrl?.let {
            GlideImage(
                model = it,
                contentDescription = "Random GIF",
                modifier = Modifier
                    .size(250.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            scope.launch {
                try {
                    val response = RetrofitInstance.api.getRandomGif("")
                    gifUrl = response.data.images.original.url
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }) {
            Text("Показать рандомный GIF")
        }
    }
}
