package com.example.playlistmaker.media.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.models.FavoritesFragmentViewModel
import com.example.playlistmaker.search.domain.models.Track

@Composable
fun FavoritesContent(
    viewModel: FavoritesFragmentViewModel? = null,
    onClick:(track: Track)-> Unit
){
    val tracks = viewModel?.tracksFlow?.collectAsState()?.value?:emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 13.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (tracks.isEmpty()){
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
            tracks.forEach {
                TrackItem(it, onClick)
            }
        }
    }
}

@Composable
fun TrackItem(track: Track, onClick:(track: Track)-> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable{
                onClick.invoke(track)
            }
    ) {
        Card(
            modifier = Modifier.size(45.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(45.dp),
                model = track.artworkUrl100,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.default_album_icon)
            )
        }
        Column (
            modifier = Modifier
                .padding(start = 8.dp, top = 6.dp)
                .weight(1f)
        ){
            Text(
                text = track.trackName,
                style = TextStyle(
                    color = colorResource(R.color.black),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                    fontWeight = FontWeight(400)
                ),
            )
            Row {
                Text(
                    text = track.artistName,
                    style = TextStyle(
                        color = colorResource(R.color.text_grey),
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        fontWeight = FontWeight(400)
                    ),
                )
                Image(
                    modifier = Modifier
                        .padding(1.dp),
                    painter = painterResource(id = R.drawable.point_icon),
                    contentDescription = null,
                )
                Text(
                    text = track.getDuration(),
                    style = TextStyle(
                        color = colorResource(R.color.text_grey),
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.ys_display_regular)),
                        fontWeight = FontWeight(400)
                    ),
                )
            }
        }
        Image(
            modifier = Modifier
                .padding(end = 12.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.agreement_icon),
            contentDescription = null,
        )

    }
}

@Preview(showBackground = true)
@Composable
fun MyPreview(){
    FavoritesContent(){}
}
