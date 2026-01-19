package uk.co.liampeters.haptictoggle.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import uk.co.liampeters.haptictoggle.ui.screens.PermissionScreen
import uk.co.liampeters.haptictoggle.ui.screens.SuccessScreen
import uk.co.liampeters.haptictoggle.ui.theme.HapticToggleTheme
import uk.co.liampeters.haptictoggle.util.OnResumeObserver
import uk.co.liampeters.haptictoggle.util.requestAddTile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HapticToggleTheme {
                Scaffold { innerPadding ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Check if the tile has been added to the quick settings shade. We do this
                        // by reading from shared preferences. This is a reliable,
                        // backwards-compatible method to query the tiles state so we can show the
                        // correct UI
                        val isTileAdded = remember {
                            mutableStateOf(
                                getSharedPreferences("prefs", MODE_PRIVATE).getBoolean(
                                    "tile_added",
                                    false
                                )
                            )
                        }

                        // Check permission state
                        val hasPermission = remember {
                            mutableStateOf(Settings.System.canWrite(this))
                        }

                        // Re-check permission state and tile status when the app UI comes back to
                        // the foreground.
                        OnResumeObserver {
                            hasPermission.value = Settings.System.canWrite(this)
                            isTileAdded.value = getSharedPreferences("prefs", MODE_PRIVATE).getBoolean(
                                "tile_added",
                                false
                            )
                        }

                        if (!hasPermission.value) {
                            // If we don't have the requisite permissions, show the UI which explains
                            // the permissions we require and why. Clicking the grant permission
                            // button will deep link into settings to where the user can choose to
                            // toggle the settings.
                            PermissionScreen(onGrantClick = {
                                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                                    data = "package:$packageName".toUri()
                                }
                                startActivity(intent)
                            })
                        } else {
                            // If we have the required permissions, show the success screen. This
                            // will guide the user to adding the tile to the Quick Settings shade.
                            SuccessScreen(
                                tileAdded = isTileAdded.value,
                                showQuickAddButton = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
                                onQuickAddClick = {
                                    requestAddTile(this) { success ->
                                        if (success) isTileAdded.value = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}