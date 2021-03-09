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

import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Round
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xatok.countdowntimer.ui.components.timer.ButtonType
import xatok.countdowntimer.ui.components.timer.ButtonType.Reset
import xatok.countdowntimer.ui.components.timer.TimeUnit
import xatok.countdowntimer.ui.components.timer.TimeUnit.Hours
import xatok.countdowntimer.ui.components.timer.TimeUnit.Minutes
import xatok.countdowntimer.ui.components.timer.TimeUnit.Seconds
import xatok.countdowntimer.ui.theme.red
import xatok.countdowntimer.ui.theme.transparent
import xatok.countdowntimer.ui.util.asDegreeAngle
import xatok.countdowntimer.ui.util.buttonTypeText

private val strokeWidth = 18.dp
private val style = Stroke(width = strokeWidth.value, cap = Round)
private const val scaleTimeUp = 1.05f

@Preview(widthDp = 320, heightDp = 320)
@Composable
fun TimerProgressBar(
    modifier: Modifier = Modifier,
    state: TimerProgressBarState = TimerProgressBarState(),
    onSelectedTimeUnit: (TimeUnit) -> Unit = {},
    onButtonClick: (ButtonType) -> Unit = {}
) {
    /**
     * Animations
     */
    val backgroundProgress = animateBackgroundProgress()
    val progress by animateProgress(state)
    val timeUp = state.totalTimeSeconds == 0L && state.isTimerRunning
    val scale by animateFloatAsState(if (timeUp) scaleTimeUp else 1f, spring(DampingRatioHighBouncy, StiffnessLow))
    val backgroundColor by animateColorAsState(if (timeUp) red else MaterialTheme.colors.secondaryVariant)
    val runningTextColor by animateColorAsState(if (timeUp) red else MaterialTheme.colors.primary)

    /**
     * Composables
     */
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
    ) {
        /**
         * Time units
         */
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                TimeUnitField(state, Hours, runningTextColor, onSelectedTimeUnit)
                NumberSpacer()
                TimeUnitField(state, Minutes, runningTextColor, onSelectedTimeUnit)
                NumberSpacer()
                TimeUnitField(state, Seconds, runningTextColor, onSelectedTimeUnit)
            }

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = { onButtonClick(Reset) },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.background)
            ) {
                Text(
                    text = Reset.buttonTypeText,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }

        /**
         * Circular progress bar
         */
        val progressColor = MaterialTheme.colors.primary
        Canvas(
            modifier = Modifier.padding(strokeWidth),
            onDraw = {
                drawProgressCircle(color = backgroundColor, progress = backgroundProgress.value, style = style)
                if (state.isTimerRunning) drawProgressCircle(color = progressColor, progress = progress, style = style)
            }
        )
    }
}

@Composable
private fun TimeUnitField(
    state: TimerProgressBarState,
    timeUnit: TimeUnit,
    runningTextColor: Color,
    onSelectedTimeUnit: (TimeUnit) -> Unit
) {
    val value = when (timeUnit) {
        Hours -> state.hours
        Minutes -> state.minutes
        Seconds -> state.seconds
    }
    NumberField(
        timeUnit = timeUnit,
        value = value,
        selected = state.selectedTimeUnit == timeUnit,
        running = state.isTimerRunning,
        runningTextColor = runningTextColor,
        onClick = { onSelectedTimeUnit(timeUnit) }
    )
}

@Composable
private fun NumberField(
    timeUnit: TimeUnit,
    value: Int,
    selected: Boolean,
    running: Boolean,
    runningTextColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val background = if (selected) MaterialTheme.colors.secondary else transparent

        Text(
            modifier = Modifier
                .clickable(enabled = !running) { onClick() }
                .background(background),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.SemiBold,
            text = value.toString().padStart(2, '0'),
            color = if (running) runningTextColor else MaterialTheme.typography.h4.color,
        )

        val timeUnitText = when (timeUnit) {
            Hours -> "h"
            Minutes -> "min"
            Seconds -> "sec"
        }
        Text(
            textAlign = TextAlign.Center,
            text = timeUnitText,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
private fun NumberSpacer() {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4,
        text = ":"
    )
}

private fun DrawScope.drawProgressCircle(
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    style: DrawStyle
) {
    drawArc(
        color = color,
        startAngle = 0.75f.asDegreeAngle,
        sweepAngle = progress.asDegreeAngle,
        useCenter = false,
        style = style
    )
}
