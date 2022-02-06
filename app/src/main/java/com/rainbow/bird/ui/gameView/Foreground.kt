package com.rainbow.bird.ui.gameView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rainbow.bird.ui.theme.GroundDividerPurple
import com.rainbow.bird.R
import com.rainbow.bird.model.ViewState

@Composable
fun Foreground(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState()
) {
    Column {
        Divider(
            color = GroundDividerPurple,
            thickness = 5.dp
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.foreground_road),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.23f)
                    .offset(x = (-10).dp)
            )
            Image(
                painter = painterResource(id = R.drawable.foreground_road),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.23f)
                    .offset(x = 290.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.foreground_earth),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.77f)
        )
    }

}

@Preview(widthDp = 411, heightDp = 180)
@Composable
fun PreviewForeground() {

    Foreground()

}
