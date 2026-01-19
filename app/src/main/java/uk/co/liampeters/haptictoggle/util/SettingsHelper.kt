package uk.co.liampeters.haptictoggle.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings

object SettingsHelper {

    // The master switch constant name
    private const val VIBRATE_ON = "vibrate_on"
    private const val HAPTIC_FEEDBACK = Settings.System.HAPTIC_FEEDBACK_ENABLED

    /**
     * Checks if the master vibration setting is enabled in the system.
     *
     * This function queries the system setting for "vibrate_on" to determine the global
     * state of vibration. It returns true if the setting is present and set to 1,
     * indicating that vibration is enabled. It returns false in all other cases,
     * including when the setting is not found or an error occurs.
     *
     * @param context The context used to access the content resolver.
     * @return `true` if master vibration is enabled, `false` otherwise.
     */
    fun isMasterVibrationEnabled(context: Context): Boolean {
        return try {
            Settings.System.getInt(context.contentResolver, VIBRATE_ON) == 1
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Toggles the master vibration setting and haptic feedback in the system.
     *
     * This function reads the current state of the master vibration setting ("vibrate_on").
     * It then inverts this state and applies it to both the master vibration setting and the
     * standard haptic feedback setting (`HAPTIC_FEEDBACK_ENABLED`). This ensures that toggling
     * the master switch also toggles touch haptics for a consistent user experience.
     *
     * After enabling vibration (if it was previously off), it triggers a short vibration to
     * provide immediate feedback to the user that the setting has been changed. This feedback
     * is only provided on Android Oreo (API 26) and newer.
     *
     * @param context The context used to access the content resolver and the vibrator service.
     */
    fun toggleMasterVibration(context: Context) {
        val newState = if (isMasterVibrationEnabled(context)) 0 else 1

        // Toggle the Master Switch
        Settings.System.putInt(
            context.contentResolver,
            VIBRATE_ON,
            newState
        )

        // Toggle the Touch Haptics to match
        Settings.System.putInt(
            context.contentResolver,
            HAPTIC_FEEDBACK,
            newState
        )

        // If we just toggled haptics on, vibrate to give the user some feedback
        val vibrator = context.getSystemService(Vibrator::class.java)
        if (newState == 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    50,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }

    }
}