package com.kobez.chatmodule.ui.components.composer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.kobez.chatmodule.state.MessageComposerState

/**
 * Input field for the Messages/Conversation screen. Allows label customization, as well as handlers
 * when the input changes.
 *
 * @param messageComposerState The state of the input.
 * @param onValueChange Handler when the value changes.
 * @param onAttachmentRemoved Handler when the user removes a selected attachment.
 * @param modifier Modifier for styling.
 * @param maxLines The number of lines that are allowed in the input.
 * @param keyboardOptions The [KeyboardOptions] to be applied to the input.
 * @param label Composable that represents the label UI, when there's no input.
 * @param innerLeadingContent Composable that represents the persistent inner leading content.
 * @param innerTrailingContent Composable that represents the persistent inner trailing content.
 */
@Composable
public fun MessageInput(
    messageComposerState: MessageComposerState,
    onValueChange: (String) -> Unit,
    // onAttachmentRemoved: (Attachment) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = DefaultMessageInputMaxLines,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    label: @Composable (MessageComposerState) -> Unit = {
        DefaultComposerLabel()
    },
    innerLeadingContent: @Composable RowScope.() -> Unit = {},
    innerTrailingContent: @Composable RowScope.() -> Unit = {},
) {
    val (value) = messageComposerState

    InputField(
        modifier = modifier,
        value = value,
        maxLines = maxLines,
        onValueChange = onValueChange,
        enabled = true,
        innerPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    innerLeadingContent()

                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()

                        if (value.isEmpty()) {
                            label(messageComposerState)
                        }
                    }

                    innerTrailingContent()
                }
            }
        },
    )
}

/**
 * The default number of lines allowed in the input. The message input will become scrollable after
 * this threshold is exceeded.
 */
private const val DefaultMessageInputMaxLines = 6

/**
 * Default input field label
 */
@Composable
internal fun DefaultComposerLabel() {
    Text(
        text = "Ask a question about this location",
        color = Color.Gray,
    )
}
