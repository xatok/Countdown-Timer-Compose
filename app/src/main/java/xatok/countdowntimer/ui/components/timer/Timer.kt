/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xatok.countdowntimer.ui.components.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xatok.countdowntimer.ui.components.timer.ButtonType.Stop
import xatok.countdowntimer.ui.components.timer.actionbutton.ActionButton
import xatok.countdowntimer.ui.components.timer.numerickeyboard.NumericKeyboard
import xatok.countdowntimer.ui.components.timer.timerprogressbar.TimerProgressBar
import xatok.countdowntimer.ui.components.timer.timerprogressbar.TimerProgressBarState
import java.util.Timer

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun Timer() {
    var timer by remember { mutableStateOf<Timer?>(null) }

    var lastSetTime by remember { mutableStateOf(Time()) }
    var timerState by remember { mutableStateOf(TimerProgressBarState()) }
    var numberStateMachine by remember { mutableStateOf<NumberStateMachine>(NumberStateMachine.Idle) }

    // Lambda called on every second tick when the timer is running
    val onTick: () -> Unit = {
        timerState = when {
            timerState.totalTimeSeconds > 0 -> timerState.subtractOneSecond()
            else -> timerState.nextTimerState(numberStateMachine, lastSetTime, Stop).also { timer?.cancel() }
        }
    }

    // Lambda that takes a ButtonType argument and mutates all states based on it.
    val onButtonClick: (ButtonType) -> Unit = { button ->
        lastSetTime = lastSetTime.handleLastSetTime(timerState, button)
        timerState = timerState.nextTimerState(numberStateMachine, lastSetTime, button)
        numberStateMachine = numberStateMachine.nextNumberState(timerState, button)
        timer = handleTimer(timer, button, onTick)
    }

    /**
     * TimerProgressBar
     */
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top) {
        TimerProgressBar(
            modifier = Modifier.padding(16.dp),
            state = timerState,
            onSelectedTimeUnit = {
                timerState = timerState.copy(selectedTimeUnit = it)
                numberStateMachine = NumberStateMachine.Idle
            },
            onButtonClick = onButtonClick
        )
    }

    val actionAllowed = timerState.totalTimeSeconds != 0L && !timerState.isTimerRunning

    /**
     * Either NumericKeyboard, Action Stop or Action Start]
     */
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            timerState.selectedTimeUnit != null -> NumericKeyboard(actionAllowed, onButtonClick)
            timerState.isTimerRunning -> ActionButton(Stop, timerState.isTimerRunning, onButtonClick)
            else -> ActionButton(ButtonType.Start, actionAllowed, onButtonClick)
        }
    }
}
