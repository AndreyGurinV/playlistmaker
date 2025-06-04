package com.example.playlistmaker.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
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
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.model.SettingsViewModel

@Composable
fun SettingsContent(viewModel: SettingsViewModel? = null){
    val context = LocalContext.current
    val isDarkTheme = viewModel?.isDarkTheme?.collectAsState()?.value != false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .background(colorResource(R.color.button_text_night))
    ) {
        Row {
            Text(
                modifier = Modifier
                    .padding(top = 10.dp, start = 12.dp, bottom = 12.dp),
                text = stringResource(R.string.title_settings),
                style = TextStyle(
                    color = colorResource(R.color.button_text),
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                    fontWeight = FontWeight(500)
                ),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 21.dp, start = 16.dp, bottom = 21.dp),
                    text = stringResource(R.string.dark_theme)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Switch(
                    modifier = Modifier
                        .padding(end = 18.dp),
                    checked = isDarkTheme,
                    onCheckedChange = { it->
                        (context.applicationContext as App).switchTheme(it)
                        viewModel?.saveCurrentTheme(it)
                    }
                )
            }
        }

        SettingsRow(
            stringResource(R.string.share),
            painterResource(id = R.drawable.share_icon)) {
            viewModel?.shareApp()
        }

        SettingsRow(
            stringResource(R.string.support),
            painterResource(id = R.drawable.support_icon)) {
            viewModel?.openSupport()
        }

        SettingsRow(
            stringResource(R.string.agreement),
            painterResource(id = R.drawable.agreement_icon)) {
            viewModel?.openTerms()
        }
    }
}

@Composable
fun SettingsRow(title: String, image: Painter, onClick: ()-> Unit){
    Row(
        verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(3f)
                .clickable{
                    onClick.invoke()
                },
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 21.dp, start = 16.dp, bottom = 21.dp),
                text = title,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 18.dp),
                painter = image,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPreview(){
    SettingsContent()
}
