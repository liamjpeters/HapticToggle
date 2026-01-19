package uk.co.liampeters.haptictoggle.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.core.content.edit
import uk.co.liampeters.haptictoggle.R
import uk.co.liampeters.haptictoggle.ui.MainActivity
import uk.co.liampeters.haptictoggle.util.SettingsHelper.isMasterVibrationEnabled
import uk.co.liampeters.haptictoggle.util.SettingsHelper.toggleMasterVibration

class HapticTileService : TileService() {

    // Called when the user pulls down the Quick Settings panel
    override fun onStartListening() {
        super.onStartListening()
        qsTile?.contentDescription = "HapticToggleTile"

        updateTileState()
    }

    // Called when the user taps the tile
    override fun onClick() {
        super.onClick()

        // Check if we have permission to write settings
        if (!Settings.System.canWrite(this)) {

            // If not, open our main activity to explain the required permissions to the user, and
            // deep link them to the settings screen to grant the permissions.

            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                // API 34+ requires a PendingIntent
                val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                startActivityAndCollapse(pendingIntent)
            } else {
                // API 24-33 can use the standard Intent
                @Suppress("StartActivityAndCollapseDeprecated","DEPRECATION")
                startActivityAndCollapse(intent)
            }
            return
        }

        // Toggle the setting
        toggleMasterVibration(this)

        // Update the tiles UI to reflect the new state
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile ?: return

        // Check current system state
        val isEnabled = isMasterVibrationEnabled(this)

        // Update Tile Appearance
        tile.state = if (isEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = if (isEnabled)
            getString(R.string.haptics_enabled)
        else
            getString(R.string.haptics_disabled)

        val iconRes = if (isEnabled) {
            R.drawable.mobile_vibrate_24px
        } else {
            R.drawable.mobile_off_24px
        }
        tile.icon = Icon.createWithResource(this, iconRes)

        tile.contentDescription = "HapticToggleTile"

        // Push updates
        tile.updateTile()
    }

    override fun onTileAdded() {
        super.onTileAdded()

        // When the tile is added, we use shared preferences to store that our tile has been added.
        // This is a reliable, backwards-compatible method to query the tiles state so we can show
        // the correct UI within the main activity.

        getSharedPreferences("prefs", MODE_PRIVATE).edit {
            putBoolean("tile_added", true)
        }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()

        // When the tile is removed, we use shared preferences to store that our tile has been removed.
        // This is a reliable, backwards-compatible method to query the tiles state so we can show
        // the correct UI within the main activity.

        getSharedPreferences("prefs", MODE_PRIVATE).edit {
            putBoolean("tile_added", false)
        }
    }

}