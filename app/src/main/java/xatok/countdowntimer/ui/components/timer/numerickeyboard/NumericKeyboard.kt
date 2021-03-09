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
package xatok.countdowntimer.ui.components.timer.numerickeyboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xatok.countdowntimer.ui.components.timer.ButtonType
import xatok.countdowntimer.ui.components.timer.ButtonType.ButtonNumber
import xatok.countdowntimer.ui.components.timer.ButtonType.Delete
import xatok.countdowntimer.ui.components.timer.ButtonType.Start
import xatok.countdowntimer.ui.util.buttonTypeText

@Preview(widthDp = 360, heightDp = 360)
@Composable
fun NumericKeyboard(
    actionAllowed: Boolean = false,
    onButtonClick: (ButtonType) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
        NumericRow {
            NumericButton(1, onButtonClick)
            NumericButton(2, onButtonClick)
            NumericButton(3, onButtonClick)
        }
        NumericRow {
            NumericButton(4, onButtonClick)
            NumericButton(5, onButtonClick)
            NumericButton(6, onButtonClick)
        }
        NumericRow {
            NumericButton(7, onButtonClick)
            NumericButton(8, onButtonClick)
            NumericButton(9, onButtonClick)
        }
        NumericRow {
            KeyButton(buttonType = Delete, enabled = actionAllowed, onButtonClick = onButtonClick)
            NumericButton(0, onButtonClick)
            KeyButton(buttonType = Start, enabled = actionAllowed, onButtonClick = onButtonClick)
        }
    }
}

@Composable
private fun NumericRow(content: @Composable RowScope.() -> Unit) =
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        content = content
    )

@Composable
private fun NumericButton(
    number: Int,
    onButtonClick: (ButtonType) -> Unit
) = KeyButton(buttonType = ButtonNumber(number), enabled = true, onButtonClick = onButtonClick)

@Composable
private fun KeyButton(
    buttonType: ButtonType,
    enabled: Boolean,
    onButtonClick: (ButtonType) -> Unit
) {
    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = 52.dp, minWidth = 52.dp)
            .clickable(enabled = enabled) { onButtonClick(buttonType) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.alpha(if (enabled) 1f else 0.5f),
            text = buttonType.buttonTypeText,
            style = when (buttonType) {
                is ButtonNumber -> MaterialTheme.typography.h4
                else -> MaterialTheme.typography.body2
            },
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}
