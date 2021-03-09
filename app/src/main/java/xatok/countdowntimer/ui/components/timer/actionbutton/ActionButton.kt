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
package xatok.countdowntimer.ui.components.timer.actionbutton

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xatok.countdowntimer.ui.components.timer.ButtonType
import xatok.countdowntimer.ui.util.buttonTypeText

@Composable
fun ActionButton(
    buttonType: ButtonType,
    enabled: Boolean,
    onButtonClick: (ButtonType) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        onClick = { onButtonClick(buttonType) },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.background),
        enabled = enabled
    ) {
        Text(
            text = buttonType.buttonTypeText,
            style = MaterialTheme.typography.subtitle2,
        )
    }
}
