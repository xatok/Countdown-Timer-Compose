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

import xatok.countdowntimer.ui.components.timer.ButtonType.ButtonNumber
import xatok.countdowntimer.ui.components.timer.ButtonType.Delete
import xatok.countdowntimer.ui.components.timer.ButtonType.Reset
import xatok.countdowntimer.ui.components.timer.ButtonType.Start
import xatok.countdowntimer.ui.components.timer.ButtonType.Stop
import xatok.countdowntimer.ui.components.timer.NumberStateMachine.Idle
import xatok.countdowntimer.ui.components.timer.NumberStateMachine.OneNumber
import xatok.countdowntimer.ui.components.timer.TimeUnit.Hours
import xatok.countdowntimer.ui.components.timer.TimeUnit.Minutes
import xatok.countdowntimer.ui.components.timer.TimeUnit.Seconds
import xatok.countdowntimer.ui.components.timer.timerprogressbar.TimerProgressBarState
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

/**
 * Mutates numberStateMachine based on button input.
 * @param button ButtonType pressed
 * @param timerState Current timer state
 */
fun NumberStateMachine.nextNumberState(
    timerState: TimerProgressBarState,
    button: ButtonType
): NumberStateMachine {
    // If no time unit is selected, nothing changes
    if (timerState.selectedTimeUnit == null) return this

    return when (button) {
        is ButtonNumber -> {
            when (this) {
                Idle -> OneNumber(button.value)
                is OneNumber -> Idle
            }
        }
        else -> Idle
    }
}

/**
 * Mutates timerProgressBarState based on the numberStateMachine and button input.
 * @param numberStateMachine Current number state machine
 * @param button ButtonType pressed
 */
fun TimerProgressBarState.nextTimerState(
    numberStateMachine: NumberStateMachine,
    lastSetTime: Time,
    button: ButtonType
): TimerProgressBarState {
    return when (button) {
        is ButtonNumber -> when (numberStateMachine) {
            Idle -> {
                val value = button.value

                when (selectedTimeUnit) {
                    Hours -> copy(hours = value)
                    Minutes -> copy(minutes = value)
                    Seconds -> copy(seconds = value)
                    null -> this
                }
            }
            is OneNumber -> {
                val value = numberStateMachine.first * 10 + button.value

                when (selectedTimeUnit) {
                    Hours -> copy(hours = value.coerceAtMost(99), selectedTimeUnit = Minutes)
                    Minutes -> copy(minutes = value.coerceAtMost(59), selectedTimeUnit = Seconds)
                    Seconds -> copy(seconds = value.coerceAtMost(59))
                    null -> this
                }
            }
        }
        Delete -> when (selectedTimeUnit) {
            Hours -> copy(hours = 0)
            Minutes -> copy(minutes = 0)
            Seconds -> copy(seconds = 0)
            null -> this
        }
        Start -> copy(lastTimerEventSeconds = totalTimeSeconds, selectedTimeUnit = null)
        Stop -> copy(
            hours = lastSetTime.hours,
            minutes = lastSetTime.minutes,
            seconds = lastSetTime.seconds,
            lastTimerEventSeconds = 0L
        )
        Reset -> TimerProgressBarState()
    }
}

/**
 * Subtracts one second to the TimerProgressBarState, nothing if all time units are zero.
 */
fun TimerProgressBarState.subtractOneSecond(): TimerProgressBarState {
    return when {
        totalTimeSeconds > 0 -> when {
            seconds > 0 -> copy(seconds = seconds - 1)
            minutes > 0 -> copy(minutes = minutes - 1, seconds = 59)
            hours > 0 -> copy(hours = hours - 1, minutes = 59, seconds = 59)
            else -> this
        }
        else -> this
    }
}

/**
 * Setups the fixedRateTimer based on the ButtonType pressed
 * @param timer Current Timer, typically a fixedRateTimer
 * @param button ButtonType pressed
 * @param onTick lambda called on every tick (every second, with a initial delay of 1 second)
 */
fun handleTimer(timer: Timer?, button: ButtonType, onTick: () -> Unit): Timer? {
    return when (button) {
        Delete -> timer
        Start -> fixedRateTimer(name = "timer", period = 1_000, initialDelay = 1_000) { onTick() }
        Stop, Reset -> timer.also { timer?.cancel() }
        else -> timer
    }
}

/**
 * Return time when Start is pressed, used to store in the state the last set time.
 * @param timerState current TimerProgressBarState state
 * @param button ButtonType pressed
 */
fun Time.handleLastSetTime(timerState: TimerProgressBarState, button: ButtonType): Time {
    return when (button) {
        Start -> Time(timerState.hours, timerState.minutes, timerState.seconds)
        else -> this
    }
}
