/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getstream.chat.android.compose.ui.messages.list

import android.text.format.DateUtils
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.R
import io.getstream.chat.android.compose.state.messages.MessageAlignment
import io.getstream.chat.android.compose.ui.components.DotAnimationDurationMillis
import io.getstream.chat.android.compose.ui.components.TypingIndicator
import io.getstream.chat.android.compose.ui.components.avatar.IconPlaceholder
import io.getstream.chat.android.compose.ui.components.messages.MessageBubble
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import io.getstream.chat.android.ui.common.feature.messages.list.MessageListController
import io.getstream.chat.android.ui.common.state.messages.list.DateSeparatorItemState
import io.getstream.chat.android.ui.common.state.messages.list.EmptyThreadPlaceholderItemState
import io.getstream.chat.android.ui.common.state.messages.list.MessageItemState
import io.getstream.chat.android.ui.common.state.messages.list.MessageListItemState
import io.getstream.chat.android.ui.common.state.messages.list.SystemMessageItemState
import io.getstream.chat.android.ui.common.state.messages.list.ThreadDateSeparatorItemState
import io.getstream.chat.android.ui.common.state.messages.list.TypingItemState
import kotlinx.coroutines.delay

/**
 * Represents the message item container that allows us to customize each type of item in the MessageList.
 *
 * @param messageListItemState The state of the message list item.
 * @param onLongItemClick Handler when the user long taps on an item.
 * @param onReactionsClick Handler when the user taps on message reactions.
 * @param onThreadClick Handler when the user taps on a thread within a message item.
 * @param onGiphyActionClick Handler when the user taps on Giphy message actions.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user receives a result from the Media Gallery Preview.
 * @param dateSeparatorContent Composable that represents date separators.
 * @param threadSeparatorContent Composable that represents thread separators.
 * @param systemMessageContent Composable that represents system messages.
 * @param messageItemContent Composable that represents regular messages.
 * @param typingIndicatorContent Composable that represents a typing indicator.
 * @param emptyThreadPlaceholderItemContent Composable that represents placeholders inside of an empty thread.
 * This content is disabled by default and can be enabled via [MessagesViewModelFactory.showDateSeparatorInEmptyThread]
 * or [MessageListController.showDateSeparatorInEmptyThread].
 */
@Composable
public fun MessageContainer(
    messageListItemState: MessageListItemState,
    // onLongItemClick: (Message) -> Unit = {},
    // onReactionsClick: (Message) -> Unit = {},
    // onThreadClick: (Message) -> Unit = {},
    // onGiphyActionClick: (GiphyAction) -> Unit = {},
    // onQuotedMessageClick: (Message) -> Unit = {},
    // onMediaGalleryPreviewResult: (MediaGalleryPreviewResult?) -> Unit = {},
    // dateSeparatorContent: @Composable (DateSeparatorItemState) -> Unit = {
    //     DefaultMessageDateSeparatorContent(dateSeparator = it)
    // },
    // threadSeparatorContent: @Composable (ThreadDateSeparatorItemState) -> Unit = {
    //     DefaultMessageThreadSeparatorContent(threadSeparator = it)
    // },
    // systemMessageContent: @Composable (SystemMessageItemState) -> Unit = {
    //     DefaultSystemMessageContent(systemMessageState = it)
    // },
    messageItemContent: @Composable (MessageItemState) -> Unit = {
        DefaultMessageItem(
            messageItem = it,
            // onLongItemClick = onLongItemClick,
            // onReactionsClick = onReactionsClick,
            // onThreadClick = onThreadClick,
            // onGiphyActionClick = onGiphyActionClick,
            // onMediaGalleryPreviewResult = onMediaGalleryPreviewResult,
            // onQuotedMessageClick = onQuotedMessageClick,
        )
    },
    typingIndicatorContent: @Composable (TypingItemState) -> Unit = {
        DefaultTypingIndicatorItem(it)
    },
    defaultFillerContent: @Composable () -> Unit = {
        DefaultFillerContent()
    },
    // emptyThreadPlaceholderItemContent: @Composable (EmptyThreadPlaceholderItemState) -> Unit = { },
) {
    // TODO: handle all message types
    when (messageListItemState) {
        is DateSeparatorItemState -> defaultFillerContent
        is ThreadDateSeparatorItemState -> defaultFillerContent
        is SystemMessageItemState -> defaultFillerContent
        is MessageItemState -> messageItemContent(messageListItemState)
        is TypingItemState -> typingIndicatorContent(messageListItemState)
        is EmptyThreadPlaceholderItemState -> defaultFillerContent
    }
}
//
// /**
//  * Represents a date separator item that shows whenever messages are too far apart in time.
//  *
//  * @param dateSeparator The data used to show the separator text.
//  */
// @Composable
// internal fun DefaultMessageDateSeparatorContent(dateSeparator: DateSeparatorItemState) {
//     Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//         Surface(
//             modifier = Modifier
//                 .padding(vertical = 8.dp),
//             color = ChatTheme.colors.overlayDark,
//             shape = RoundedCornerShape(16.dp),
//         ) {
//             Text(
//                 modifier = Modifier.padding(vertical = 2.dp, horizontal = 16.dp),
//                 text = DateUtils.getRelativeTimeSpanString(
//                     dateSeparator.date.time,
//                     System.currentTimeMillis(),
//                     DateUtils.DAY_IN_MILLIS,
//                     DateUtils.FORMAT_ABBREV_RELATIVE,
//                 ).toString(),
//                 color = ChatTheme.colors.barsBackground,
//                 style = ChatTheme.typography.body,
//             )
//         }
//     }
// }
//
// /**
//  * Represents a thread separator item that is displayed in thread mode to separate a parent message
//  * from thread replies.
//  *
//  * @param threadSeparator The data used to show the separator text.
//  */
// @Composable
// internal fun DefaultMessageThreadSeparatorContent(threadSeparator: ThreadDateSeparatorItemState) {
//     val backgroundGradient = Brush.verticalGradient(
//         listOf(
//             ChatTheme.colors.threadSeparatorGradientStart,
//             ChatTheme.colors.threadSeparatorGradientEnd,
//         ),
//     )
//     val replyCount = threadSeparator.replyCount
//
//     Box(
//         modifier = Modifier
//             .fillMaxWidth()
//             .padding(vertical = ChatTheme.dimens.threadSeparatorVerticalPadding)
//             .background(brush = backgroundGradient),
//         contentAlignment = Alignment.Center,
//     ) {
//         Text(
//             modifier = Modifier.padding(vertical = ChatTheme.dimens.threadSeparatorTextVerticalPadding),
//             text = LocalContext.current.resources.getQuantityString(
//                 R.plurals.stream_compose_message_list_thread_separator,
//                 replyCount,
//                 replyCount,
//             ),
//             color = ChatTheme.colors.textLowEmphasis,
//             style = ChatTheme.typography.body,
//         )
//     }
// }
//
// /**
//  * The default System message content.
//  *
//  * A system message is a message generated by a system event, such as updating the channel or muting a user.
//  *
//  * @param systemMessageState The system message item to show.
//  */
// @Composable
// internal fun DefaultSystemMessageContent(systemMessageState: SystemMessageItemState) {
//     Text(
//         modifier = Modifier
//             .fillMaxWidth()
//             .padding(vertical = 12.dp, horizontal = 16.dp),
//         text = systemMessageState.message.text,
//         color = ChatTheme.colors.textLowEmphasis,
//         style = ChatTheme.typography.footnoteBold,
//         textAlign = TextAlign.Center,
//     )
// }

/**
 * The default message item content.
 *
 * @param messageItem The message item to show.
 * @param onLongItemClick Handler when the user long taps on an item.
 * @param onReactionsClick Handler when the user taps on message reactions.
 * @param onThreadClick Handler when the user clicks on the message thread.
 * @param onGiphyActionClick Handler when the user selects a Giphy action.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user receives a result from the Media Gallery Preview.
 */
@Composable
internal fun DefaultMessageItem(
    messageItem: MessageItemState,
    // onLongItemClick: (Message) -> Unit,
    // onReactionsClick: (Message) -> Unit = {},
    // onThreadClick: (Message) -> Unit,
    // onGiphyActionClick: (GiphyAction) -> Unit,
    // onQuotedMessageClick: (Message) -> Unit,
    // onMediaGalleryPreviewResult: (MediaGalleryPreviewResult?) -> Unit = {},
) {
    MessageItem(
        messageItem = messageItem,
        // onLongItemClick = onLongItemClick,
        // onReactionsClick = onReactionsClick,
        // onThreadClick = onThreadClick,
        // onGiphyActionClick = onGiphyActionClick,
        // onQuotedMessageClick = onQuotedMessageClick,
        // onMediaGalleryPreviewResult = onMediaGalleryPreviewResult,
    )
}

/**
 * The default message item content.
 *
 * @param messageItem The message item to show.
 * @param onLongItemClick Handler when the user long taps on an item.
 * @param onReactionsClick Handler when the user taps on message reactions.
 * @param onThreadClick Handler when the user clicks on the message thread.
 * @param onGiphyActionClick Handler when the user selects a Giphy action.
 * @param onQuotedMessageClick Handler for quoted message click action.
 * @param onMediaGalleryPreviewResult Handler when the user receives a result from the Media Gallery Preview.
 */
@Composable
internal fun DefaultTypingIndicatorItem(
    messageItem: TypingItemState,
    modifier: Modifier = Modifier,
    leadingContent: @Composable RowScope.() -> Unit = {
        Box (
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .align(Alignment.Bottom)
        ){
            IconPlaceholder()
        }
    },
    headerContent: @Composable ColumnScope.() -> Unit = {
        var offsetY by remember { mutableStateOf(0.dp)}
        val bounceAnimation = rememberInfiniteTransition(label = "Bounce Animation")

        val animatedOffsetY by bounceAnimation.animateFloat(
            initialValue = 0f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "Bounce Animation"
        )
        
        LaunchedEffect(animatedOffsetY) {
            offsetY = animatedOffsetY.dp
        }

        Column (
            verticalArrangement = Arrangement.Bottom,
        ){
            Spacer(modifier = Modifier.height(offsetY))
            Text(
                    text = "typing . . .",
                    color = ChatTheme.colors.textLowEmphasis,
                    style = ChatTheme.typography.footnote,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
        }
    },
    centerContent: @Composable ColumnScope.() -> Unit = {
        MessageBubble(
            color = Color(0xFFD3D3D3),
            shape = ChatTheme.shapes.otherMessageBubble,
            content = {
                TypingIndicator(
                    modifier = Modifier.padding(all = 12.dp)
                )
            }
        )
    },
    trailingContent: @Composable RowScope.() -> Unit = {
    },
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
        ,
        contentAlignment = Alignment.BottomStart,
    ) {
        Row(
            modifier
                .widthIn(max = 300.dp)
        ) {
            leadingContent()
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)) {

                headerContent()

                centerContent()

                // footerContent(messageItem)
            }
            trailingContent()
        }
    }

}

@Composable
internal fun DefaultFillerContent() {
    TypingIndicator()
}
