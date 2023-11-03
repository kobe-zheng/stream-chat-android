package com.kobez.chatmodule.ui.components.messages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kobez.chatmodule.state.MessageItemState
import com.kobez.chatmodule.theme.ChatTheme
import com.kobez.chatmodule.ui.components.IconPlaceholder

/**
 * The default message container for all messages in the Conversation/Messages screen.
 *
 * It shows the avatar and the message details, which can have a header (reactions), the content which
 * can be a text message, file or image attachment, or a custom attachment and the footer, which can
 * be a deleted message footer (if we own the message) or the default footer, which contains a timestamp
 * or the thread information.
 *
 * It also allows for long click and thread click events.
 *
 * @param messageItem The message item to show, which holds the message and the group position, if the message is in
 * a group of messages from the same user.
 * @param modifier Modifier for styling.
 * @param leadingContent The content shown at the start of a message list item. By default, we provide
 * [DefaultMessageItemLeadingContent], which shows a user avatar if the message doesn't belong to the
 * current user.
 * @param headerContent The content shown at the top of a message list item. By default, we provide
 * [DefaultMessageItemHeaderContent], which shows a list of reactions for the message.
 *  @param centerContent The content shown at the center of a message list item. By default, we provide
 * [DefaultMessageItemCenterContent], which shows the message bubble with text and attachments.
 * @param trailingContent The content shown at the end of a message list item. By default, we provide
 * [DefaultMessageItemTrailingContent], which adds an extra spacing to the end of the message list item.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun MessageItem(
    messageItem: MessageItemState,
    modifier: Modifier = Modifier,
    leadingContent: @Composable RowScope.(MessageItemState) -> Unit = {
        DefaultMessageItemLeadingContent(messageItem = it)
    },
    headerContent: @Composable ColumnScope.(MessageItemState) -> Unit = {
        DefaultMessageItemHeaderContent(
            messageItem = it,
        )
    },
    centerContent: @Composable ColumnScope.(MessageItemState) -> Unit = {
        DefaultMessageItemCenterContent(
            messageItem = it,
        )
    },
    trailingContent: @Composable RowScope.(MessageItemState) -> Unit = {
        DefaultMessageItemTrailingContent(messageItem = it)
    },
) {
    val clickModifier =
        Modifier.combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { },
        )

    val color = Color.Transparent

    val messageAlignment = if (messageItem.isMine) MessageAlignment.End else MessageAlignment.Start

    Box(
        modifier = Modifier
            // .testTag("Stream_MessageItem")
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = color)
        // .semantics { contentDescription = description }
        ,
        contentAlignment = messageAlignment.itemAlignment,
    ) {
        Row(
            modifier
                .widthIn(max = 300.dp)
                .then(clickModifier),
        ) {
            leadingContent(messageItem)

            Column(
                horizontalAlignment = messageAlignment.contentAlignment,
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                headerContent(messageItem)

                centerContent(messageItem)
            }

            trailingContent(messageItem)
        }
    }
}

/**
 * Represents the default content shown at the start of the message list item.
 *
 * By default, we show a user avatar if the message doesn't belong to the current user.
 *
 * @param messageItem The message item to show the content for.
 */
@Composable
internal fun RowScope.DefaultMessageItemLeadingContent(
    messageItem: MessageItemState,
) {
    val modifier = Modifier
        .padding(start = 8.dp, end = 8.dp)
        .align(Alignment.Bottom)

    if (!messageItem.isMine) {
        Box (
            modifier = modifier
        ){
            IconPlaceholder()
        }
    } else {
        Spacer(modifier = modifier)
    }
}

/**
 * Represents the default content shown at the top of the message list item.
 *
 * By default, we show if the message is pinned and a list of reactions for the message.
 *
 * @param messageItem The message item to show the content for.
 */
@Composable
internal fun DefaultMessageItemHeaderContent(
    messageItem: MessageItemState,
) {
    if (!messageItem.isMine) {
        Text(
            text = "Penny",
            color = ChatTheme.colors.textLowEmphasis,
            style = ChatTheme.typography.footnote,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Represents the default content shown at the end of the message list item.
 *
 * By default, we show an extra spacing at the end of the message list item.
 *
 * @param messageItem The message item to show the content for.
 */
@Composable
internal fun DefaultMessageItemTrailingContent(
    messageItem: MessageItemState,
) {
    if (messageItem.isMine) {
        Spacer(modifier = Modifier.width(8.dp))
    }
}

/**
 * Represents the default content shown at the center of the message list item.
 *
 * By default, we show a message bubble with attachments or emoji stickers if message is emoji only.
 *
 * @param messageItem The message item to show the content for.
 */
@Composable
internal fun DefaultMessageItemCenterContent(
    messageItem: MessageItemState,
) {
    val modifier = Modifier.widthIn(max = ChatTheme.dimens.messageItemMaxWidth)
    RegularMessageContent(
        modifier = modifier,
        messageItem = messageItem,
    )
}

/**
 * Message content for messages which consist of more than just emojis.
 *
 * @param messageItem The message item to show the content for.
 * @param modifier Modifier for styling.
 */
@Composable
internal fun RegularMessageContent(
    messageItem: MessageItemState,
    modifier: Modifier = Modifier,
) {
    val message = messageItem.message
    val ownsMessage = messageItem.isMine

    val messageBubbleShape =
        if (ownsMessage) ChatTheme.shapes.myMessageBubble else ChatTheme.shapes.otherMessageBubble

    val messageBubbleColor = when (ownsMessage) {
        true -> Color(0xFF0068EF)
        else -> Color(0xFFD3D3D3)
    }

    MessageBubble(
        modifier = modifier,
        shape = messageBubbleShape,
        color = messageBubbleColor,
        // border = if (messageItem.isMine) null else BorderStroke(1.dp, ChatTheme.colors.borders),
        content = {
            MessageContent(
                message = message,
            )
        },
    )
}


