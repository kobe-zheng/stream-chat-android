package com.kobez.chatmodule.ui.components.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kobez.chatmodule.R
import com.kobez.chatmodule.state.ConnectionState
import com.kobez.chatmodule.theme.ChatTheme
import com.kobez.chatmodule.ui.components.BackButton
import com.kobez.chatmodule.ui.components.NetworkLoadingIndicator
import com.kobez.chatmodule.ui.components.messages.TypingIndicator
import com.kobez.chatmodule.util.mirrorRtl

/**
 * A clean, decoupled UI element that doesn't rely on ViewModels or our custom architecture setup.
 * This allows the user to fully govern how the [MessageListHeader] behaves, by passing in all the
 * data that's required to display it and drive its actions, as well as customize the slot APIs.
 *
 * @param connectionState The state of WS connection used to switch between the subtitle and the network loading view.
 * @param modifier Modifier for styling.
 * @param color The color of the header.
 * @param shape The shape of the header.
 * @param elevation The elevation of the header.
 * @param onBackPressed Handler that propagates the back button click event.
 * @param leadingContent The content shown at the start of the header
 * @param centerContent The content shown in the middle of the header and represents the core information, by default
 * [DefaultMessageListHeaderCenterContent].
 * @param trailingContent The content shown at the end of the header.
 */
@Composable
public fun MessageListHeader(
    connectionState: ConnectionState,
    modifier: Modifier = Modifier,
    color: Color = ChatTheme.colors.barsBackground,
    shape: Shape = ChatTheme.shapes.header,
    elevation: Dp = ChatTheme.dimens.headerElevation,
    onBackPressed: () -> Unit = {},
    onInformationIconClick: () -> Unit = {},
    leadingContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderLeadingContent(onBackPressed = onBackPressed)
    },
    centerContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderCenterContent(
            modifier = Modifier.weight(1f),
            connectionState = connectionState,
        )
    },
    trailingContent: @Composable RowScope.() -> Unit = {
        DefaultMessageListHeaderTrailingContent(
            onClick = onInformationIconClick,
        )
    },
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        elevation = elevation,
        color = color,
        shape = shape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent()

            centerContent()

            trailingContent()
        }
    }
}

/**
 * Represents the leading content of [MessageListHeader]. By default shows a back button.
 *
 * @param onBackPressed Handler that propagates the back button click event.
 */
@Composable
internal fun DefaultMessageListHeaderLeadingContent(onBackPressed: () -> Unit) {
    val layoutDirection = LocalLayoutDirection.current

    BackButton(
        modifier = Modifier.mirrorRtl(layoutDirection = layoutDirection),
        painter = painterResource(id = R.drawable.stream_compose_ic_arrow_back),
        onBackPressed = onBackPressed,
    )
}

/**
 * Represents the center content of [MessageListHeader]. By default shows header title, that handles
 * if we should show a loading view for network, or the channel information.
 *
 * @param connectionState A flag that governs if we show the subtitle or the network loading view.
 * @param modifier Modifier for styling.
 */
@Composable
public fun DefaultMessageListHeaderCenterContent(
    connectionState: ConnectionState,
    modifier: Modifier = Modifier,
) {
    val title = "CHATBOT"

    val subtitle = "Initial Implementation"

    Column(
        modifier = modifier
            .height(IntrinsicSize.Max),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = ChatTheme.typography.title3Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = ChatTheme.colors.textHighEmphasis,
        )

        when (connectionState) {
            is ConnectionState.Connected -> {
                DefaultMessageListHeaderSubtitle(
                    subtitle = subtitle,
                    // typingUsers = typingUsers,
                )
            }
            is ConnectionState.Connecting -> {
                NetworkLoadingIndicator(
                    modifier = Modifier.wrapContentHeight(),
                    spinnerSize = 12.dp,
                    textColor = ChatTheme.colors.textLowEmphasis,
                    textStyle = ChatTheme.typography.footnote,
                )
            }
            is ConnectionState.Offline -> {
                Text(
                    text = stringResource(id = R.string.stream_compose_disconnected),
                    color = ChatTheme.colors.textLowEmphasis,
                    style = ChatTheme.typography.footnote,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * Represents the default message list header subtitle, which shows either the number of people online
 * and total member count or the currently typing users.
 *
 * @param subtitle The subtitle to show.
 */
@Composable
internal fun DefaultMessageListHeaderSubtitle(
    subtitle: String,
) {
    val textColor = ChatTheme.colors.textLowEmphasis
    val textStyle = ChatTheme.typography.footnote

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TypingIndicator()

        Text(
            text = subtitle,
            color = textColor,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Represents the trailing content of [MessageListHeader]. By default shows the channel avatar.
 *
 * @param onClick The handler called when the user taps on the channel avatar.
 */
@Composable
internal fun DefaultMessageListHeaderTrailingContent(
    onClick: () -> Unit,
) {
    IconButton(
        enabled = true,
        modifier = Modifier
            .size(32.dp)
            .padding(4.dp),
        content = {
            Icon(
                painter = painterResource(id = R.drawable.stream_compose_ic_send),
                contentDescription = stringResource(id = R.string.stream_compose_attachments),
            )
        },
        onClick = onClick,
    )
}

@Preview(name = "MessageListHeader Preview (Connected)")
@Composable
private fun MessageListHeaderConnectedPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            connectionState = ConnectionState.Connected,
        )
    }
}

@Preview(name = "MessageListHeader Preview (Connecting)")
@Composable
private fun MessageListHeaderConnectingPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            connectionState = ConnectionState.Connecting,
        )
    }
}

@Preview(name = "MessageListHeader Preview (Offline)")
@Composable
private fun MessageListHeaderOfflinePreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            connectionState = ConnectionState.Offline,
        )
    }
}

@Preview(name = "MessageListHeader Preview (User Typing)")
@Composable
private fun MessageListHeaderUserTypingPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            connectionState = ConnectionState.Connected,
        )
    }
}

@Preview(name = "MessageListHeader Preview (Many Members)")
@Composable
private fun MessageListHeaderManyMembersPreview() {
    ChatTheme {
        MessageListHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            connectionState = ConnectionState.Connected,
        )
    }
}
