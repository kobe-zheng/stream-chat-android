package com.kobez.chatmodule.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * A modified version of the default [RippleTheme] from [MaterialTheme] which
 * works in case the [MaterialTheme] is not initialized.
 */
@Immutable
internal object StreamRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color {
        return RippleTheme.defaultRippleColor(
            contentColor = LocalContentColor.current,
            lightTheme = !isSystemInDarkTheme(),
        )
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleTheme.defaultRippleAlpha(
            contentColor = LocalContentColor.current,
            lightTheme = !isSystemInDarkTheme(),
        )
    }
}
