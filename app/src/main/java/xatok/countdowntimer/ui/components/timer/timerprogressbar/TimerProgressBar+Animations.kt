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
package xatok.countdowntimer.ui.components.timer.timerprogressbar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import java.util.concurrent.TimeUnit.SECONDS

@Composable
internal fun animateBackgroundProgress(): Animatable<Float, AnimationVector1D> {
    val backgroundProgress = remember { Animatable(0f) }
    LaunchedEffect("backgroundAnimation") {
        backgroundProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(delayMillis = 50, durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }
    return backgroundProgress
}

@Composable
internal fun animateProgress(state: TimerProgressBarState): State<Float> {
    val transitionSpec: @Composable Transition.Segment<Long>.() -> FiniteAnimationSpec<Float> =
        {
            when (state.totalTimeSeconds) {
                0L -> tween(durationMillis = 0)
                else -> tween(
                    durationMillis = SECONDS.toMillis(state.lastTimerEventSeconds).toInt(),
                    easing = LinearEasing
                )
            }
        }

    return updateTransition(targetState = state.lastTimerEventSeconds)
        .animateFloat(transitionSpec) { lastTimerEventSeconds -> if (lastTimerEventSeconds == 0L) 1f else 0f }
}
