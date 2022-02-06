package com.rainbow.bird.ui.gameView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.rainbow.bird.R
import com.rainbow.bird.model.*

@Composable
fun Bird(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState()
) {


    // Rotate 90 degree when quick falling / dying.
    val rotateDegree =
        if (state.isLifting) LiftingDegree
        else if (state.isFalling) FallingDegree
        else if (state.isQuickFalling) DyingDegree
        else if (state.isOver) DeadDegree
        else PendingDegree

    Box(
        modifier
    ) {
        var correctBirdHeight = state.birdState.birdHeight

        if (state.playZoneSize.second > 0) {
            val playZoneHeightInDP = with(LocalDensity.current) {
                state.playZoneSize.second.toDp()
            }

            val fallingThreshold = BirdHitGroundThreshold

            if (correctBirdHeight + fallingThreshold >= playZoneHeightInDP / 2) {
                // Send hit to ground action.

//                viewModel.dispatch(GameAction.HitGround)

                // Make sure bird not fall over ground.
                correctBirdHeight = playZoneHeightInDP / 2 - fallingThreshold
            }
        }

        Image(
            painter = painterResource(id = R.drawable.bird_match),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .size(state.birdState.birdW, state.birdState.birdH)
                .align(Alignment.Center)
                .offset(y = correctBirdHeight)
                .rotate(rotateDegree)  // Rotate 90 degree when dying/ over.
        )
    }

}

@Preview(widthDp = 411, heightDp = 660)
@Composable
fun PreviewBird() {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bird_match),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .size(BirdSizeWidth, BirdSizeHeight)
                .align(Alignment.Center)
                .offset(y = DefaultBirdHeightOffset)
        )
    }
}

const val PendingDegree = 0f
const val LiftingDegree = -10f
const val FallingDegree = -LiftingDegree
const val DyingDegree = FallingDegree + 10f
const val DeadDegree = DyingDegree - 10f