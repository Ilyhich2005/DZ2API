package com.example.dz2api

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi



class MainActivity : ComponentActivity() {
    private val viewModel: GifViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GifScreen(viewModel)
        }
    }
}

@Composable
fun GifScreen(viewModel: GifViewModel) {
    val listState = rememberLazyListState()
    val padding = dimensionResource(id = R.dimen.gif_item_padding)
    Column(modifier = Modifier.fillMaxSize()) {

        if (viewModel.error != null && viewModel.gifUrl.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(viewModel.error!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(padding))
                Button(onClick = { viewModel.loadGif() }) {
                    Text(stringResource(R.string.retry))
                }
            }
            return@Column
        }


        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(80.dp))
            }
        } else {

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(padding),
                verticalArrangement = Arrangement.spacedBy(padding)
            ) {
                itemsIndexed(viewModel.gifUrl) { index,url ->
                    GifItem(url = url, index = index + 1)
                }

                item {
                    if (viewModel.error != null && !viewModel.isLoading) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(viewModel.error!!, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadNextPage() }) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    }
                    else if (viewModel.isLoadingMore) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(80.dp))
                        }
                    }
                }
            }

            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { index ->
                        if (index == viewModel.gifUrl.lastIndex && !viewModel.isLoadingMore) {
                            viewModel.loadNextPage()
                        }
                    }
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifItem(url: String,index: Int) {
    val context = LocalContext.current
    val toastText = stringResource(R.string.toast_gif, index)
    val height = dimensionResource(id = R.dimen.gif_item_height)
    val bgColor = colorResource(id = R.color.gif_background)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(bgColor)
            .clickable {
                Toast.makeText(
                    context,
                    toastText,
                    Toast.LENGTH_SHORT
            ).show()},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))

        GlideImage(
            model = url,
            contentDescription = "GIF",
            modifier = Modifier.fillMaxSize()
        )
    }
}





