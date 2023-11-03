package com.kobez.chatmodule.ui.components.messages

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kobez.chatmodule.state.DateSeparatorItemState
import com.kobez.chatmodule.state.EmptyThreadPlaceholderItemState
import com.kobez.chatmodule.state.MessageItemState
import com.kobez.chatmodule.state.MessageListItemState
import com.kobez.chatmodule.state.TypingItemState
import com.kobez.chatmodule.theme.ChatTheme
import com.kobez.chatmodule.ui.components.IconPlaceholder

/**
 * Represents the message item container that allows us to customize each type of item in the MessageList.
 *
 * @param messageListItemState The state of the message list item.
 * @param messageItemContent Composable that represents regular messages.
 * @param typingIndicatorContent Composable that represents a typing indicator.
 */
@Composable
public fun MessageContainer(
    messageListItemState: MessageListItemState,
    messageItemContent: @Composable (MessageItemState) -> Unit = {
        DefaultMessageItem(
            messageItem = it,
        )
    },
    typingIndicatorContent: @Composable () -> Unit = {
        DefaultTypingIndicatorItem()
    },
    defaultFillerContent: @Composable () -> Unit = {
        DefaultFillerContent()
    },
) {
    when (messageListItemState) {
        is DateSeparatorItemState -> defaultFillerContent
        is MessageItemState -> messageItemContent(messageListItemState)
        is TypingItemState -> typingIndicatorContent()
        is EmptyThreadPlaceholderItemState -> defaultFillerContent
    }
}

/**
 * The default message item content.
 *
 * @param messageItem The message item to show.
 */
@Composable
internal fun DefaultMessageItem(
    messageItem: MessageItemState,
) {
    MessageItem(
        messageItem = messageItem,
    )
}

/**
 * The default message item content.
 *
 */
@Composable
internal fun DefaultTypingIndicatorItem(
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


        Box(
            modifier = Modifier.height(25.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column (
                verticalArrangement = Arrangement.Bottom,
            ){
                Text(
                    text = "typing . . .",
                    color = ChatTheme.colors.textLowEmphasis,
                    style = ChatTheme.typography.footnote,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(offsetY))
            }
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
            }
            trailingContent()
        }
    }

}

@Composable
internal fun DefaultFillerContent() {
    TypingIndicator()
}
