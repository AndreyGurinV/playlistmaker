package com.example.playlistmaker.media.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.models.PlaylistsFragmentViewModel

@Composable
fun PlaylistsContent(
    viewModel: PlaylistsFragmentViewModel? = null,
    onNewPlaylistClicked:() -> Unit,
    onClick:(playlist: PlaylistDto) -> Unit
) {
    val playlists = viewModel?.playlistsFlow?.collectAsState()?.value?:emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 13.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            modifier = Modifier
                .padding(top = 6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.button_text),
            ),
            shape = RoundedCornerShape(54.dp),
            onClick = onNewPlaylistClicked
        ) {
            Text(
                text = stringResource(R.string.new_playlist),
            )
        }

        if (playlists.isEmpty()){
            Image(
                modifier = Modifier
                    .padding(end = 18.dp, top = 106.dp),
                painter = painterResource(id = R.drawable.nothing_find_icon),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(top = 21.dp, start = 16.dp, bottom = 21.dp),
                text = stringResource(R.string.empty_media),
                style = TextStyle(
                    color = colorResource(R.color.button_text),
                    fontSize = 19.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontWeight = FontWeight(400)
                ),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(playlists.size) { i ->
                    PlaylistItem(playlist = playlists[i], onClick = onClick)
                }
            }
        }
    }

}

@Composable
fun PlaylistItem(playlist: PlaylistDto, onClick:(playlist: PlaylistDto)-> Unit) {
    Column {
        Card(
            modifier = Modifier.size(160.dp).clickable{
                onClick.invoke(playlist)
            },
            shape = RoundedCornerShape(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = playlist.pathToImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.default_album_icon)
            )
        }
        Text(
            text = playlist.playlistTitle,
            style = TextStyle(
                color = colorResource(R.color.button_text),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontWeight = FontWeight(400)
            ),
        )
        Text(
            text = playlist.playlistDescription,
            style = TextStyle(
                color = colorResource(R.color.button_text),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                fontWeight = FontWeight(400)
            ),
        )

    }
}