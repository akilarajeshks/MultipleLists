package com.plex.multiplelists

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.transform.CircleCropTransformation
import com.google.accompanist.coil.rememberCoilPainter
import com.plex.multiplelists.ui.theme.MultipleListsTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@ExperimentalStdlibApi
val plexGrid = buildList {
    repeat(10) {
        this.add(
            PlexRow(
                buildList {
                    repeat(10) {
                        this.add(PlexTile(title = "Movie $it"))
                    }
                }
            )
        )
    }
}

@ExperimentalStdlibApi
val tilesState = MutableStateFlow(PlexGrid(plexGrid))

class MainActivity : ComponentActivity() {
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultipleListsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@ExperimentalStdlibApi
@Composable
fun Greeting(name: String) {
    val currentState = tilesState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var focussedRowIndex = remember { 0 }
    var focussedColumnIndex = remember { 0 }
    LazyColumn(
        modifier = Modifier.onPreviewKeyEvent {
            if (it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                coroutineScope.launch {
                    val (newRow, newColumn) = navigate(
                        it, focussedRowIndex, focussedColumnIndex, currentState.value
                    )
                    focussedRowIndex = newRow
                    focussedColumnIndex = newColumn
                }
            }
            return@onPreviewKeyEvent false
        }
    ) {
        items(items = currentState.value.grid) {
            LazyRow {
                items(items = it.rows) {
                    val focussed = it.mutableInteractionSource.collectIsFocusedAsState()
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .background(if (focussed.value) Color.Red else Color.White)
                                .width(100.dp)
                                .height(100.dp),
                            painter = rememberCoilPainter(
                                request = "https://picsum.photos/300/300",
                                requestBuilder = {
                                    transformations(CircleCropTransformation())
                                },
                            ),
                            contentDescription = "Poster",
                        )
                        Text(
                            text = "Movie",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                                .focusable(true, it.mutableInteractionSource)
                                .background(if (focussed.value) Color.Red else Color.White)
                        )
                    }

                }
            }
        }
    }
}

@ExperimentalStdlibApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MultipleListsTheme {
        Greeting("Android")
    }
}