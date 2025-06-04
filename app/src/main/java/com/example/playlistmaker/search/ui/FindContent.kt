package com.example.playlistmaker.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.TrackItem
import com.example.playlistmaker.search.data.TracksState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksSearchViewModel

@Composable
fun FindContent(
    viewModel: TracksSearchViewModel,
    onUpdateClicked:() -> Unit,
    onTextChanged:(String)-> Unit,
    onClick:(track: Track)-> Unit){
    val state = viewModel.stateFlow.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(colorResource(R.color.button_text_night))
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp, start = 12.dp, bottom = 12.dp),
                text = stringResource(R.string.title_find),
                style = TextStyle(
                    color = colorResource(R.color.button_text),
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontWeight = FontWeight(500)
                ),
            )
        }
        SearchInput(viewModel, onTextChanged)

        when(state){
            is TracksState.Content -> {
                state.tracks.forEach {
                    TrackItem(it, onClick = onClick)
                }
            }
            is TracksState.Empty -> {
                Image(
                    modifier = Modifier
                        .padding(end = 18.dp, top = 106.dp),
                    painter = painterResource(id = R.drawable.nothing_find_icon),
                    contentDescription = null,
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.nothing_found),
                        style = TextStyle(
                            color = colorResource(R.color.button_text),
                            fontSize = 19.sp,
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontWeight = FontWeight(400)
                        ),
                    )
                }

            }
            is TracksState.Error -> {
                Image(
                    modifier = Modifier
                        .padding(end = 18.dp, top = 106.dp),
                    painter = painterResource(id = R.drawable.nothing_find_icon),
                    contentDescription = null,
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.something_went_wrong),
                        style = TextStyle(
                            color = colorResource(R.color.button_text),
                            fontSize = 19.sp,
                            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                            fontWeight = FontWeight(400)
                        ),
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(top = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.button_text),
                    ),
                    shape = RoundedCornerShape(54.dp),
                    onClick = onUpdateClicked
                ) {
                    Text(
                        text = stringResource(R.string.button_update),
                    )
                }

            }
            is TracksState.History -> {
                state.tracks.forEach {
                    TrackItem(it, onClick = onClick)
                }
                Button(
                    modifier = Modifier
                        .padding(top = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.button_text),
                    ),
                    shape = RoundedCornerShape(54.dp),
                    onClick = {viewModel.clear()}
                ) {
                    Text(
                        text = stringResource(R.string.button_clear_search_history),
                    )
                }

            }
            TracksState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(44.dp),
                    color = colorResource(R.color.yp_blue),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}



@Composable
fun SearchInput(viewModel: TracksSearchViewModel, onTextChanged:(String)-> Unit) {
    val text = viewModel.text.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, top = 8.dp, bottom = 8.dp)
            .height(36.dp)
            .background(
                    color = colorResource(R.color.grey),
                    shape = RoundedCornerShape(8.dp)
            )
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                if (it.length <= 50) onTextChanged.invoke(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.5.dp)
                .padding(horizontal = 36.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = colorResource(R.color.button_text),
                fontFamily = FontFamily(Font(R.font.ys_display_regular))
            ),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.Black)
        )

        Image(
            painter = painterResource(id = R.drawable.find_icon2),
            contentDescription = "Поиск",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
                .size(18.dp)
        )

        if (text.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.clear_icon2),
                contentDescription = "Очистить",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .size(12.dp)
                    .clickable {
                        onTextChanged.invoke("")
                        viewModel.load()
                    }
            )
        }
    }
}

