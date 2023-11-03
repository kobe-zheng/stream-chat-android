package com.kobez.chatmodule.ui.components.messages

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kobez.chatmodule.models.Message
import com.kobez.chatmodule.theme.ChatTheme

/**
 * Default text element for messages, with extra styling and padding for the chat bubble.
 *
 * Alternatively, it just shows a basic [Text] element.
 *
 * @param message Message to show.
 * @param modifier Modifier for styling.
 */
@Composable
public fun MessageText(
    message: Message,
    modifier: Modifier = Modifier,
) {

    // TODO: Make sure to change text color and handle message owner
    val textColor = if (message.text.contains("from bot")) {
        Color.Black
    } else {
        Color.White
    }

    val horizontalPadding = 12.dp
    val verticalPadding = 8.dp
    Text(
        modifier = modifier
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding,
            )
            .clipToBounds(),
        text = message.text,
        color = textColor
    )
}

@Preview
@Composable
private fun MessageTextPreview() {
    ChatTheme {
        MessageText(
            message = Message(text = "Hello World!"),
        )
    }
}
