package uk.co.liampeters.haptictoggle.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import uk.co.liampeters.haptictoggle.R
import uk.co.liampeters.haptictoggle.ui.theme.HapticToggleTheme
import uk.co.liampeters.haptictoggle.util.MultiLanguagePreview
import uk.co.liampeters.haptictoggle.util.NightModeMultiLanguagePreview

@Composable
fun PermissionScreen(onGrantClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Icon(
            Icons.Rounded.Warning,
            tint = uk.co.liampeters.haptictoggle.ui.theme.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Text(
            stringResource(R.string.permission_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            stringResource(R.string.permission_overview),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp)
            )
        Text(
            stringResource(R.string.permission_call_to_action),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = onGrantClick,
            modifier = Modifier.padding(top = 24.dp).semantics{
                contentDescription = "grant permission button"
            },
        ) {
            Text(stringResource(R.string.permission_button))
        }
    }
}

@MultiLanguagePreview
@Composable
fun PermissionScreenPreview() {
    HapticToggleTheme {
        PermissionScreen {}
    }
}

@NightModeMultiLanguagePreview
@Composable
fun PermissionScreenPreviewNight() {
    HapticToggleTheme(darkTheme = true) {
        PermissionScreen {}
    }
}