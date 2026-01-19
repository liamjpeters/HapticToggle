package uk.co.liampeters.haptictoggle.util

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import uk.co.liampeters.haptictoggle.R
import uk.co.liampeters.haptictoggle.service.HapticTileService

/**
 * A Composable that observes the lifecycle of its context and executes a given lambda
 * function when the `ON_RESUME` event occurs.
 *
 * This is useful for running code that should only execute when the Composable becomes visible
 * and active on the screen, for example, to refresh data.
 *
 * It uses a `DisposableEffect` to ensure the lifecycle observer is correctly added and removed
 * as the Composable enters and leaves the composition, preventing memory leaks.
 *
 * @param onContextResume The lambda function to be executed when the lifecycle's `ON_RESUME` event
 * is triggered.
 */
@Composable
fun OnResumeObserver(onContextResume: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onContextResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

/**
 * Requests the user to add the Quick Settings Tile for this application.
 *
 * On Android Tiramisu (API 33) and above, this function presents a system dialog
 * to the user, allowing them to add the `HapticTileService` tile directly.
 * The result of this action (whether the tile was added, already existed, or the
 * request was denied) is communicated through the `onResult` callback.
 *
 * On versions of Android older than Tiramisu, a system dialog is not available.
 * As a fallback, this function displays a `Toast` message instructing the user
 * to add the tile manually from their Quick Settings panel. In this case, the `onResult`
 * callback is not invoked.
 *
 * @param context The application or activity context, used to access system services
 *                and resources.
 * @param onResult A callback function that receives a `Boolean` indicating the success
 *                 of the tile addition request on API 33+. `true` if the tile was
 *                 successfully added or was already present; `false` otherwise.
 */
fun requestAddTile(context: Context, onResult: (Boolean) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val statusBarManager = context.getSystemService(StatusBarManager::class.java)

        // Our tile service component
        val componentName = ComponentName(context, HapticTileService::class.java)

        // The icon that will show in the preview dialog
        val icon = Icon.createWithResource(context, R.drawable.mobile_vibrate_24px)

        statusBarManager.requestAddTileService(
            componentName,
            context.getString(R.string.app_name), // The label shown in the dialog
            icon,
            { it.run() },
            { result ->
                // 1 = Added, 2 = Already Exists
                val success = result == 1 || result == 2
                onResult(success)
            }
        )
    } else {
        // Fallback: On older versions, show a Toast telling the user to add it manually
        Toast.makeText(
            context,
            context.getString(R.string.manual_tile_add),
            Toast.LENGTH_LONG
        ).show()
    }
}