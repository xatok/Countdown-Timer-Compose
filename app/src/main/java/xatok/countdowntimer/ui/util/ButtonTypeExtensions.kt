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
package xatok.countdowntimer.ui.util

import xatok.countdowntimer.ui.components.timer.ButtonType
import xatok.countdowntimer.ui.components.timer.ButtonType.ButtonNumber
import xatok.countdowntimer.ui.components.timer.ButtonType.Delete
import xatok.countdowntimer.ui.components.timer.ButtonType.Reset
import xatok.countdowntimer.ui.components.timer.ButtonType.Start
import xatok.countdowntimer.ui.components.timer.ButtonType.Stop

val ButtonType.buttonTypeText: String
    get() = when (this) {
        is ButtonNumber -> value.toString()
        Delete -> "Delete"
        Start -> "Start"
        Stop -> "Stop"
        Reset -> "Reset"
    }
