package com.kobez.chatmodule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kobez.chatmodule.models.Message
import com.kobez.chatmodule.theme.ChatTheme
import com.kobez.chatmodule.ui.components.header.MessageListHeader
import com.kobez.chatmodule.ui.composer.MessageComposer
import com.kobez.chatmodule.ui.messages.MessageList
import com.kobez.chatmodule.util.rememberMessageListState
import com.kobez.chatmodule.viewmodels.MessageComposerViewModel
import com.kobez.chatmodule.viewmodels.MessageListViewModel
import com.kobez.chatmodule.viewmodels.MessagesViewModelFactory

/**
 * Default root Messages screen component, that provides the necessary ViewModels and
 * connects all the data handling operations, as well as some basic actions, like back pressed handling.
 *
 * Because this screen can be shown only if there is an active/selected Channel, the user must provide
 * a [viewModelFactory] that contains the channel ID, in order to load up all the data. Otherwise, we can't show the UI.
 *
 * @param viewModelFactory The factory used to build ViewModels and power the behavior.
 * You can customize the behavior of the list through its parameters. For default behavior,
 * simply create an instance and pass in just the channel ID and the context.
 * @param showHeader If we're showing the header or not.
 * @param onBackPressed Handler for when the user taps on the Back button and/or the system
 * back button.
 */
@Composable
public fun MessagesScreen(
    viewModelFactory: MessagesViewModelFactory,
    showHeader: Boolean = true,
    onBackPressed: () -> Unit = {},
    onInformationIconClick: () -> Unit = {},
) {
    val listViewModel = viewModel(MessageListViewModel::class.java, factory = viewModelFactory)
    val composerViewModel = viewModel(MessageComposerViewModel::class.java, factory = viewModelFactory)

    val backAction = remember(listViewModel, composerViewModel/*, attachmentsPickerViewModel*/) {
        {
            onBackPressed()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (showHeader) {
                    val connectionState by listViewModel.connectionState.collectAsState()

                    MessageListHeader(
                        modifier = Modifier
                            .height(56.dp),
                        connectionState = connectionState,
                        onBackPressed = backAction,
                        onInformationIconClick = onInformationIconClick,
                    )
                }
            },
            bottomBar = {
                MessageComposer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.Center),
                    viewModel = composerViewModel,
                    onSendMessage = remember(composerViewModel) {
                        { message: Message ->
                            listViewModel.sendMessage(message)
                            composerViewModel.clearData()
                        }
                    },
                )
            },
        ) {
            MessageList(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ChatTheme.colors.appBackground)
                    .padding(it),
                viewModel = listViewModel,
                messagesLazyListState = rememberMessageListState(parentMessageId = null),
            )
        }
    }
}
