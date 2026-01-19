package uk.co.liampeters.haptictoggle.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
fun SuccessScreen(
    showQuickAddButton: Boolean = true,
    tileAdded: Boolean = false,
    onQuickAddClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = uk.co.liampeters.haptictoggle.ui.theme.Success,
            modifier = Modifier.size(64.dp)
        )
        Text(
            stringResource(R.string.success_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (!tileAdded) {
            Text(
                stringResource(R.string.success_overview_1),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(1.0f)
                    .align(Alignment.Start)
            )
            Text(
                stringResource(R.string.success_overview_2),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(1.0f)
                    .align(Alignment.Start)
            )
            Text(
                stringResource(R.string.success_overview_3),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(1.0f)
                    .align(Alignment.Start)
            )
        } else {
            Text(
                stringResource(R.string.success_tile_added),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (showQuickAddButton) {
            Button(
                enabled = !tileAdded,
                onClick = onQuickAddClick,
                modifier = Modifier.padding(top = 24.dp).semantics{
                    contentDescription = "add to quick settings button"
                }
            ) {
                if (tileAdded) {
                    Text(stringResource(R.string.success_button_inactive))
                } else {
                    Text(stringResource(R.string.success_button_active))
                }
            }
        }
    }
}

@MultiLanguagePreview
@Composable
fun SuccessScreenPreview() {
    HapticToggleTheme {
        SuccessScreen(tileAdded = true){}
    }
}

@NightModeMultiLanguagePreview
@Composable
fun SuccessScreenPreviewNight() {
    HapticToggleTheme (darkTheme = true) {
        SuccessScreen(tileAdded = true){}
    }
}

@MultiLanguagePreview
@Composable
fun SuccessScreenNotAddedPreview() {
    HapticToggleTheme {
        SuccessScreen(tileAdded = false){}
    }
}

@NightModeMultiLanguagePreview
@Composable
fun SuccessScreenNotAddedPreviewNight() {
    HapticToggleTheme (darkTheme = true) {
        SuccessScreen(tileAdded = false){}
    }
}