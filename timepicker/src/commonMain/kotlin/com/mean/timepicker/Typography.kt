package com.mean.timepicker

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import com.mean.timepicker.tokens.TypographyKeyTokens


/** Helper function for component typography tokens. */
internal fun Typography.fromToken(value: TypographyKeyTokens): TextStyle {
    return when (value) {
        TypographyKeyTokens.DisplayLarge -> displayLarge
        TypographyKeyTokens.DisplayMedium -> displayMedium
        TypographyKeyTokens.DisplaySmall -> displaySmall
        TypographyKeyTokens.HeadlineLarge -> headlineLarge
        TypographyKeyTokens.HeadlineMedium -> headlineMedium
        TypographyKeyTokens.HeadlineSmall -> headlineSmall
        TypographyKeyTokens.TitleLarge -> titleLarge
        TypographyKeyTokens.TitleMedium -> titleMedium
        TypographyKeyTokens.TitleSmall -> titleSmall
        TypographyKeyTokens.BodyLarge -> bodyLarge
        TypographyKeyTokens.BodyMedium -> bodyMedium
        TypographyKeyTokens.BodySmall -> bodySmall
        TypographyKeyTokens.LabelLarge -> labelLarge
        TypographyKeyTokens.LabelMedium -> labelMedium
        TypographyKeyTokens.LabelSmall -> labelSmall
    }
}

internal val TypographyKeyTokens.value: TextStyle
    @Composable @ReadOnlyComposable get() = MaterialTheme.typography.fromToken(this)

