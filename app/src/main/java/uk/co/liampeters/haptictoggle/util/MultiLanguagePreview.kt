package uk.co.liampeters.haptictoggle.util

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(locale = "en", name = "English", showBackground = true)
@Preview(locale = "de", name = "German", showBackground = true)
@Preview(locale = "el", name = "Greek", showBackground = true)
@Preview(locale = "fr", name = "French", showBackground = true)
@Preview(locale = "es", name = "Spanish", showBackground = true)
@Preview(locale = "hi", name = "Hindi", showBackground = true)
@Preview(locale = "ja", name = "Japanese", showBackground = true)
@Preview(locale = "pt-rBR", name = "Brazilian Portuguese", showBackground = true)
@Preview(locale = "ar", name = "Arabic (RTL)", showBackground = true)
annotation class MultiLanguagePreview


@Preview(locale = "en", name = "English", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "de", name = "German", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "el", name = "Greek", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "fr", name = "French", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "es", name = "Spanish", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "hi", name = "Hindi", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "ja", name = "Japanese", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "pt-rBR", name = "Brazilian Portuguese", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "ar", name = "Arabic (RTL)", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
annotation class NightModeMultiLanguagePreview