package app.omnivore.omnivore.feature.reader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.omnivore.omnivore.R
import app.omnivore.omnivore.core.designsystem.component.SwitchPreferenceWidget
import app.omnivore.omnivore.feature.components.SliderWithPlusMinus
import app.omnivore.omnivore.feature.theme.OmnivoreTheme

@Composable
fun ReaderPreferencesView(
    webReaderViewModel: WebReaderViewModel
) {
    val isDark = isSystemInDarkTheme()
    val currentWebPreferences = webReaderViewModel.storedWebPreferences(isDark)
    val isFontListExpanded = remember { mutableStateOf(false) }
    val highContrastTextSwitchState =
        remember { mutableStateOf(currentWebPreferences.prefersHighContrastText) }

    val justifyTextSwitchState =
        remember { mutableStateOf(currentWebPreferences.prefersJustifyText) }

    val selectedWebFontName =
        remember { mutableStateOf(currentWebPreferences.fontFamily.displayText) }

    var fontSizeSliderValue by remember { mutableStateOf(currentWebPreferences.textFontSize.toFloat()) }
    var marginSliderValue by remember { mutableStateOf(currentWebPreferences.maxWidthPercentage.toFloat()) }
    var lineSpacingSliderValue by remember { mutableStateOf(currentWebPreferences.lineHeight.toFloat()) }

    val themeState = remember { mutableStateOf(currentWebPreferences.storedThemePreference) }


    val volumeForScrollState by webReaderViewModel.volumeRockerForScrollState.collectAsStateWithLifecycle()

    OmnivoreTheme {
        // Temporary wrapping for margin while migrating components to design system
        Column(
            modifier = Modifier
                .padding(vertical = 35.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Font", style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(red = 137, green = 137, blue = 137)
                        )
                    )
                    Spacer(modifier = Modifier.weight(1.0F))
                    Box {
                        AssistChip(
                            onClick = { isFontListExpanded.value = true },
                            label = {
                                Text(
                                    selectedWebFontName.value,
                                    color = Color(red = 137, green = 137, blue = 137)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Choose the Reader font",
                                    tint = Color(red = 137, green = 137, blue = 137)
                                )
                            },
                        )
                        if (isFontListExpanded.value) {
                            DropdownMenu(
                                expanded = isFontListExpanded.value,
                                onDismissRequest = { isFontListExpanded.value = false },
                            ) {
                                WebFont.entries.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                it.displayText, style = TextStyle(
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = Color(red = 137, green = 137, blue = 137)
                                                )
                                            )
                                        },
                                        onClick = {
                                            webReaderViewModel.applyWebFont(it)
                                            selectedWebFontName.value = it.displayText
                                            isFontListExpanded.value = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    stringResource(R.string.reader_preferences_view_font_size), style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(red = 137, green = 137, blue = 137)
                    )
                )

                SliderWithPlusMinus(
                    value = fontSizeSliderValue,
                    onValueChange = {
                        fontSizeSliderValue = it
                        webReaderViewModel.setFontSize(it.toInt())
                    },
                    steps = 40,
                    valueRange = 10f..50f,
                )

                Text(
                    stringResource(R.string.reader_preferences_view_page_width), style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(red = 137, green = 137, blue = 137)
                    )
                )

                SliderWithPlusMinus(
                    value = marginSliderValue,
                    onValueChange = {
                        marginSliderValue = it
                        webReaderViewModel.setMaxWidthPercentage(it.toInt())
                    },
                    steps = 40,
                    valueRange = 60f..100f,
                )

                Text(
                    stringResource(R.string.reader_preferences_view_line_spacing), style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(red = 137, green = 137, blue = 137)
                    )
                )
                SliderWithPlusMinus(
                    value = lineSpacingSliderValue,
                    onValueChange = {
                        lineSpacingSliderValue = it
                        webReaderViewModel.setLineHeight(it.toInt())
                    },
                    steps = 50,
                    valueRange = 100f..300f,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        stringResource(R.string.reader_preferences_view_theme), style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(red = 137, green = 137, blue = 137)
                        )
                    )
                    Spacer(modifier = Modifier.weight(1.0F))
                    Text(
                        stringResource(R.string.reader_preferences_view_auto), style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(red = 137, green = 137, blue = 137)
                        )
                    )
                    Checkbox(checked = themeState.value == "System", onCheckedChange = {
                        if (it) {
                            themeState.value = "System"
                            webReaderViewModel.updateStoredThemePreference("System")
                        } else {
                            val newThemeKey = if (isDark) "Black" else "Light"
                            themeState.value = newThemeKey
                            webReaderViewModel.updateStoredThemePreference(newThemeKey)
                        }
                    })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    for (theme in Themes.entries) {
                        if (theme.themeKey != "System") {
                            val isSelected = theme.themeKey == themeState.value
                            Button(
                                onClick = {
                                    themeState.value = theme.themeKey
                                    webReaderViewModel.updateStoredThemePreference(theme.themeKey)
                                },
                                shape = CircleShape,
                                border = BorderStroke(
                                    3.dp,
                                    if (isSelected) colorResource(R.color.cta_yellow) else Color.Transparent
                                ),
                                modifier = Modifier.size(35.dp),
                                colors = ButtonDefaults.buttonColors(

                                    containerColor = Color(theme.backgroundColor)
                                )
                            ) {

                            }
                            Spacer(modifier = Modifier.weight(0.1F))
                        }

                    }
                    Spacer(modifier = Modifier.weight(2.0F))
                }
            }
            // TODO : Use state flow
            SwitchPreferenceWidget(
                title = stringResource(R.string.reader_preferences_view_high_constrast_text),
                checked = highContrastTextSwitchState.value,
                onCheckedChanged = {
                    highContrastTextSwitchState.value = it
                    webReaderViewModel.updateHighContrastTextPreference(it)
                },
            )
            // TODO : Use state flow
            SwitchPreferenceWidget(
                title = stringResource(R.string.reader_preferences_view_justify_text),
                checked = justifyTextSwitchState.value,
                onCheckedChanged = {
                    justifyTextSwitchState.value = it
                    webReaderViewModel.updateJustifyText(it)
                },
            )
            SwitchPreferenceWidget(
                title = stringResource(R.string.reader_preferences_view_volume_scroll),
                checked = volumeForScrollState,
                onCheckedChanged = { webReaderViewModel.setVolumeRockerForScrollState(it) },
            )
        }
    }
}

data class WebPreferences(
    val textFontSize: Int,
    val lineHeight: Int,
    val maxWidthPercentage: Int,
    val themeKey: String,
    val storedThemePreference: String,
    val fontFamily: WebFont,
    val prefersHighContrastText: Boolean,
    val prefersJustifyText: Boolean
)
