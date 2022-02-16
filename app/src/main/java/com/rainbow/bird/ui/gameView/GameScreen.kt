package com.rainbow.bird.ui.gameView

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rainbow.bird.model.*
import com.rainbow.bird.ui.theme.ForegroundEarthYellow
import com.rainbow.bird.viewmodel.GameViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(
    clickable: Clickable = Clickable()
) {
    val viewModel: GameViewModel = viewModel()
    val viewState by viewModel.viewState.collectAsState()
    var screenSize: Pair<Int, Int>

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForegroundEarthYellow)
            // .background(DefaultBlackBackground)
            .run {
                pointerInteropFilter {
                    // Send lift action to let bird up.
                    // Todo only work when running status.
                    when (it.action) {
                        ACTION_DOWN -> {
                            if (viewState.gameStatus == GameStatus.Waiting) clickable.onStart()
                            else if (viewState.gameStatus == GameStatus.Running) clickable.onTap()
                            else return@pointerInteropFilter false
                        }

                        // Todo send quick lift when long tap event.
                        MotionEvent.ACTION_MOVE -> {
                            return@pointerInteropFilter false
                        }

                        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                            return@pointerInteropFilter false
                        }
                    }

                    false
                }
            }
    ) {

        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .onGloballyPositioned {
                screenSize = Pair(it.size.width, it.size.height)


                if (viewState.playZoneSize.first <= 0 || viewState.playZoneSize.second <= 0) {
                    viewModel.dispatch(GameAction.ScreenSizeDetect, screenSize)
                }
            }
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
        ) {

            // Far background
            Background(Modifier.fillMaxSize())

            // Put two group pipes
            PipeCouple(
                modifier = Modifier.fillMaxSize(),
                state = viewState,
                pipeIndex = 0
            )

            PipeCouple(
                modifier = Modifier.fillMaxSize(),
                state = viewState,
                pipeIndex = 1
            )

            // Put real time or over score board
            ScoreBoard(
                modifier = Modifier.fillMaxSize(),
                state = viewState,
                clickable = clickable
            )

            val playZoneWidthInDP = with(LocalDensity.current) {
                viewState.playZoneSize.first.toDp()
            }

            val playZoneHeightInDP = with(LocalDensity.current) {
                viewState.playZoneSize.second.toDp()
            }



            // Check hit first or second pipe.
            // And send hit pipe action if bird hit any pipe.
            if (viewState.gameStatus == GameStatus.Running) {
                viewState.pipeStateList.forEachIndexed { pipeIndex, pipeState ->
                    CheckPipeStatus(
                        viewState.birdState.birdHeight,
                        pipeState,
                        playZoneWidthInDP,
                        playZoneHeightInDP
                    ).also {
                        when (it) {
                            PipeStatus.BirdHit -> {
                                viewModel.dispatch(GameAction.HitPipe)
                            }

                            PipeStatus.BirdCrossed -> {
                                viewModel.dispatch(GameAction.CrossedPipe, pipeIndex = pipeIndex)
                            }

                            // No care other status.
                            else -> {}
                        }
                    }
                }
            }

            // Put bird
            Bird(
                modifier = Modifier.fillMaxSize(),
                state = viewState
            )
        }

        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
        ) {
            Foreground(
                modifier = Modifier.fillMaxSize(),
                state = viewState
            )
        }
    }
}

@Composable
fun CheckPipeStatus(birdHeightOffset: Dp, pipeState: PipeState, zoneWidth: Dp, zoneHeight: Dp): PipeStatus {

    if (pipeState.offset - PipeCoverWidth > - zoneWidth / 2 + BirdSizeWidth / 2) {
        return PipeStatus.BirdComing
    } else if (pipeState.offset - PipeCoverWidth < - zoneWidth / 2 - BirdSizeWidth / 2) {
        return PipeStatus.BirdCrossed
    } else {
        val birdTop = (zoneHeight - BirdSizeHeight) / 2 + birdHeightOffset
        val birdBottom = (zoneHeight + BirdSizeHeight) / 2 + birdHeightOffset

        if (birdTop < pipeState.upHeight || birdBottom > zoneHeight - pipeState.downHeight) {
            return PipeStatus.BirdHit
        }

        return PipeStatus.BirdCrossing
    }
 }

@Preview(widthDp = 411, heightDp = 840)
@Composable
fun PreviewGameScreen() {
    GameScreen()
}

data class Clickable(
    val onStart: () -> Unit = {},
    val onTap: () -> Unit = {},
    val onRestart: () -> Unit = {},
    val onExit: () -> Unit = {}
)