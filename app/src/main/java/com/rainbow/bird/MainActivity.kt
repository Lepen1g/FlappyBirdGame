package com.rainbow.bird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rainbow.bird.MainActivity.Companion.AutoTickDuration
import com.rainbow.bird.model.GameAction
import com.rainbow.bird.model.GameStatus
import com.rainbow.bird.ui.gameView.Clickable
import com.rainbow.bird.ui.gameView.GameScreen
import com.rainbow.bird.ui.theme.BirdTheme
import com.rainbow.bird.utils.GameViewModel
import com.rainbow.bird.utils.StatusBarUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    companion object {

        const val AutoTickDuration = 50L // 300L Control bird and pipe speed.
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StatusBarUtil.transparentStatusBar(this)
            BirdGameApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BirdGameApp() {
    BirdTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val gameViewModel: GameViewModel = viewModel()

            // Send a auto tick action to view model and trigger game start.
            LaunchedEffect(key1 = Unit) {
                while (isActive) {
                    delay(AutoTickDuration)
                    if (gameViewModel.viewState.value.gameStatus != GameStatus.Waiting) {
                        gameViewModel.dispatch(GameAction.AutoTick)
                    }
                }
            }
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(key1 = Unit) {
                val observer = object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                    fun onPause() {
                        // Todo send pause action
                    }

                    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                    fun onResume() {
                        // Todo send resume action
                    }
                }

                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            Flappy(Clickable(

                onStart = {
                    gameViewModel.dispatch(GameAction.Start)
                },

                onTap = {
                    gameViewModel.dispatch(GameAction.TouchLift)
                },

                onRestart = {
                    gameViewModel.dispatch(GameAction.Restart)
                },

                onExit = {
                }
            ))
        }
    }
}

@Composable
fun Flappy(clickable: Clickable = Clickable()) {
    GameScreen(clickable = clickable)
}

