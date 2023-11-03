package com.kobez.chatmodule.ui.components.messages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kobez.chatmodule.models.Message

/**
 * Represents the default message content within the bubble that can show different UI based on the message state.
 *
 * @param message The message to show.
 * @param modifier Modifier for styling.
 * @param regularMessageContent Composable that represents the default regular message content, such as attachments and
 * text.
 */
@Composable
public fun MessageContent(
    message: Message,
    modifier: Modifier = Modifier,
    regularMessageContent: @Composable () -> Unit = {
        DefaultMessageContent(
            modifier = modifier,
            message = message,
        )
    },
) {
    regularMessageContent()
}

/**
 * Represents the default regular message content that can contain attachments and text.
 *
 * @param message The message to show.
 */
@Composable
internal fun DefaultMessageContent(
    modifier: Modifier = Modifier,
    message: Message,
) {
    Column {
        DefaultMessageTextContent(
            modifier = modifier,
            message = message,
        )
    }
}

/**
 * The default text message content. It holds the quoted message in case there is one.
 *
 * @param message The message to show.
 */
@Composable
internal fun DefaultMessageTextContent(
    modifier: Modifier = Modifier,
    message: Message,
) {
    Column {
        MessageText(
            modifier= modifier,
            message = message,
        )
    }
}