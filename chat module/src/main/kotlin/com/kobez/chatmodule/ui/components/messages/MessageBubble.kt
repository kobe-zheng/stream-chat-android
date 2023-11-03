package com.kobez.chatmodule.ui.components.messages

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Wraps the content of a message in a bubble.
 *
 * @param color The color of the bubble.
 * @param shape The shape of the bubble.
 * @param modifier Modifier for styling.
 * @param content The content of the message.
 */
@Composable
public fun MessageBubble(
    color: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
    ) {
        content()
    }
}
