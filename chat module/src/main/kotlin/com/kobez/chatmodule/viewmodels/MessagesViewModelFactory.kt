package com.kobez.chatmodule.viewmodels

import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Holds all the dependencies needed to build the ViewModels for the Messages Screen.
 * Currently builds the [MessageComposerViewModel], [MessageListViewModel]
 * @param context Used to build the [ClipboardManager].
 * message we want to scroll to is not in a thread, you can pass in a null value.
 */
public class MessagesViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {

    /**
     * The list of factories that can build [ViewModel]s that our Messages feature components use.
     */
    private val factories: Map<Class<*>, () -> ViewModel> = mapOf(
        MessageComposerViewModel::class.java to {
            MessageComposerViewModel()
        },
        MessageListViewModel::class.java to {
            MessageListViewModel()
        },
    )

    /**
     * Creates the required [ViewModel] for our use case, based on the [factories] we provided.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = factories[modelClass]?.invoke()
            ?: throw IllegalArgumentException(
                "MessageListViewModelFactory can only create instances of " +
                    "the following classes: ${factories.keys.joinToString { it.simpleName }}",
            )

        @Suppress("UNCHECKED_CAST")
        return viewModel as T
    }
}
