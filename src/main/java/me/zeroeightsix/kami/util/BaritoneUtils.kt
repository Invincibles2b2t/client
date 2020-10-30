package me.zeroeightsix.kami.util

import baritone.api.BaritoneAPI
import baritone.api.Settings
import me.zeroeightsix.kami.process.TemporaryPauseProcess

object BaritoneUtils {
    var settingsInitialized = false

    var paused = false
        private set

    val isPathing get() = primary?.pathingBehavior!!.isPathing
    val isCustomGoalActive get() = primary?.customGoalProcess!!.isActive
    val isActive get() = primary?.customGoalProcess?.isActive ?: false

    val api get() = if (!settingsInitialized) null else BaritoneAPI.getProvider()
    val primary get() = if (!settingsInitialized) null else BaritoneAPI.getProvider().primaryBaritone

    fun pause() {
        if (!paused) {
            primary?.pathingControlManager!!.registerProcess(TemporaryPauseProcess)
            paused = true
        }
    }

    fun unpause() {
        if (paused) {
            val process = primary?.pathingControlManager!!.mostRecentInControl()
            if (process.isPresent && process.get() == TemporaryPauseProcess) /* Don't run if not paused lol */ {
                paused = false
                process.get().onLostControl()
            }
        }
    }

    fun cancelEverything() = primary?.pathingBehavior?.cancelEverything()

    fun settings(): Settings? {
        return if (!settingsInitialized) {
            null
        } else {
            BaritoneAPI.getSettings()
        }
    }
}