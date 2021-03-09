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

sealed class ButtonType {
    /**
     * Adds one number to the NumberStateMachine
     */
    data class ButtonNumber(val value: Int) : ButtonType()

    /**
     * Stops timer, resets timer to default state
     */
    object Reset : ButtonType()

    /**
     * Clears selected TimerUnit value
     */
    object Delete : ButtonType()

    /**
     * Starts timer if the total time is > 0
     */
    object Start : ButtonType()

    /**
     * Stops timer, reverts total time to state before Start.
     */
    object Stop : ButtonType()
}
