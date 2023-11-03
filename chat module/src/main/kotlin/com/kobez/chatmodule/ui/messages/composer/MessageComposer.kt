package com.kobez.chatmodule.ui.messages.composer

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.kobez.chatmodule.R
import com.kobez.chatmodule.models.Message
import com.kobez.chatmodule.state.MessageComposerState
import com.kobez.chatmodule.theme.ChatTheme
import com.kobez.chatmodule.ui.components.composer.MessageInput
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kobez.chatmodule.ui.components.composer.CoolDownIndicator
import com.kobez.chatmodule.util.mirrorRtl
import com.kobez.chatmodule.viewmodels.MessageComposerViewModel

/**
 * Default MessageComposer component that relies on [MessageComposerViewModel] to handle data and
 * communicate various events.
 *
 * @param viewModel The ViewModel that provides pieces of data to show in the composer, like the
 * currently selected integration data or the user input. It also handles sending messages.
 * @param modifier Modifier for styling.
 * @param onSendMessage Handler when the user sends a message. By default it delegates this to the
 * ViewModel, but the user can override if they want more custom behavior.
 * @param onValueChange Handler when the input field value changes.
 * their own integrations, which they need to hook up to their own data providers and UI.
 * @param label Customizable composable that represents the input field label (hint).
 * @param input Customizable composable that represents the input field for the composer, [MessageInput] by default.
 * while audio recording is in progress.
 * @param trailingContent Customizable composable that represents the trailing content of the composer, send button
 * by default.
 */
@Composable
public fun MessageComposer(
    viewModel: MessageComposerViewModel,
    modifier: Modifier = Modifier,
    onSendMessage: (Message) -> Unit = { viewModel.sendMessage(it) },
    onValueChange: (String) -> Unit = { viewModel.setMessageInput(it) },
    label: @Composable (MessageComposerState) -> Unit = { DefaultComposerLabel() },
    input: @Composable RowScope.(MessageComposerState) -> Unit = {
        DefaultComposerInputContent(
            messageComposerState = it,
            onValueChange = onValueChange,
            // onAttachmentRemoved = onAttachmentRemoved,
            label = label,
        )
    },
    trailingContent: @Composable (MessageComposerState) -> Unit = {
        DefaultMessageComposerTrailingContent(
            value = it.inputValue,
            coolDownTime = it.coolDownTime,
            onSendMessage = { input ->
                val message = viewModel.buildNewMessage(input)
                onSendMessage(message)
            },
        )
    },
) {
    val messageComposerState by viewModel.messageComposerState.collectAsState()

    MessageComposer(
        modifier = modifier,
        onSendMessage = { text ->
            val messageWithData = viewModel.buildNewMessage(text)

            onSendMessage(messageWithData)
        },
        input = input,
        trailingContent = trailingContent,
        messageComposerState = messageComposerState,
    )
}

/**
 * Clean version of the [MessageComposer] that doesn't rely on ViewModels, so the user can provide a
 * manual way to handle and represent data and various operations.
 *
 * @param messageComposerState The state of the message input.
 * @param onSendMessage Handler when the user wants to send a message.
 * @param modifier Modifier for styling.
 * @param onValueChange Handler when the input field value changes.
 * their own integrations, which they need to hook up to their own data providers and UI.
 * @param label Customizable composable that represents the input field label (hint).
 * @param input Customizable composable that represents the input field for the composer, [MessageInput] by default.
 * @param trailingContent Customizable composable that represents the trailing content of the composer, send button
 * by default.
 */
@Composable
public fun MessageComposer(
    messageComposerState: MessageComposerState,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    label: @Composable (MessageComposerState) -> Unit = { DefaultComposerLabel() },
    input: @Composable RowScope.(MessageComposerState) -> Unit = {
        DefaultComposerInputContent(
            messageComposerState = messageComposerState,
            onValueChange = onValueChange,
            // onAttachmentRemoved = onAttachmentRemoved,
            label = label,
        )
    },
    trailingContent: @Composable (MessageComposerState) -> Unit = {
        DefaultMessageComposerTrailingContent(
            value = it.inputValue,
            coolDownTime = it.coolDownTime,
            onSendMessage = onSendMessage,
        )
    },
) {
    Surface(
        modifier = modifier,
        color = ChatTheme.colors.barsBackground,
    ) {
        Column(Modifier.padding(vertical = 4.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Bottom,
            ) {
                Spacer(
                    modifier = Modifier.size(16.dp),
                )
                input(messageComposerState)

                trailingContent(messageComposerState)
            }
        }

    }
}


/**
 * Default input field label
 */
@Composable
internal fun DefaultComposerLabel() {
    Text(
        text = "Ask a question about this location",
        color = ChatTheme.colors.textLowEmphasis,
    )
}

/**
 * Represents the default input content of the Composer.
 *
 * @param label Customizable composable that represents the input field label (hint).
 * @param messageComposerState The state of the message input.
 * @param onValueChange Handler when the input field value changes.
 */
@Composable
private fun RowScope.DefaultComposerInputContent(
    messageComposerState: MessageComposerState,
    onValueChange: (String) -> Unit,
    label: @Composable (MessageComposerState) -> Unit,
) {
    MessageInput(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .weight(1f),
        label = label,
        messageComposerState = messageComposerState,
        onValueChange = onValueChange,
        // onAttachmentRemoved = onAttachmentRemoved,
    )
}

/**
 * Represents the default trailing content for the Composer, which represent a send button or a cooldown timer.
 *
 * @param value The input value.
 * @param coolDownTime The amount of time left in cool-down mode.
 * @param onSendMessage Handler when the user wants to send a message.
 */
// @OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun DefaultMessageComposerTrailingContent(
    value: String,
    coolDownTime: Int,
    onSendMessage: (String) -> Unit,
) {
    val isInputValid by lazy { (value.isNotBlank()) }
    val sendButtonDescription = stringResource(id = R.string.stream_compose_cd_send_button)

    if (coolDownTime > 0) {
        CoolDownIndicator(coolDownTime = coolDownTime)
    } else {
        IconButton(
            modifier = Modifier
                .semantics { contentDescription = sendButtonDescription },
            enabled = isInputValid,
            content = {
                val layoutDirection = LocalLayoutDirection.current

                Icon(
                    modifier = Modifier.mirrorRtl(layoutDirection = layoutDirection),
                    painter = painterResource(id = R.drawable.stream_compose_ic_send),
                    contentDescription = stringResource(id = R.string.stream_compose_send_message),
                    tint = if (isInputValid) ChatTheme.colors.primaryAccent else ChatTheme.colors.textLowEmphasis,
                )
            },
            onClick = {
                if (isInputValid) {
                    onSendMessage(value)
                }
            },
        )
    }
}


