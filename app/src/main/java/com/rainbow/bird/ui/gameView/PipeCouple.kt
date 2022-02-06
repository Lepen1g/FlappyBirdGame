package com.rainbow.bird.ui.gameView

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rainbow.bird.model.GameAction
import com.rainbow.bird.model.PipeResetThreshold
import com.rainbow.bird.model.PreviewPipeState
import com.rainbow.bird.model.ViewState
import com.rainbow.bird.utils.GameViewModel


@Composable
fun PipeCouple(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState(),
    pipeIndex: Int = 0
) {
    val viewModel: GameViewModel = viewModel()

    val pipeState = state.pipeStateList[pipeIndex]

    Box(
        modifier
    ) {
        // New a list of pipes to keep moving ,when offset to device, invisible.


        GetUpPipe(height = pipeState.upHeight,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = pipeState.offset)
        )

        GetDownPipe(height = pipeState.downHeight,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = pipeState.offset)
        )

        if (state.playZoneSize.first > 0) {
            val playZoneWidthInDP = with(LocalDensity.current) {
                state.playZoneSize.first.toDp()
            }

            if (pipeState.offset < - playZoneWidthInDP - PipeResetThreshold) {
                // Send pipe reset action.
                viewModel.dispatch(GameAction.PipeExit, pipeIndex = pipeIndex)
            }
        }
    }
}

@Preview(widthDp = 411, heightDp = 660)
@Composable
fun PipeScreen() {
    val pipeState = PreviewPipeState

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GetUpPipe(height = pipeState.upHeight,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = pipeState.offset)
        )

        GetDownPipe(height = pipeState.downHeight,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = pipeState.offset)
        )
    }
}