package com.kobez.chatmodule.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.painter.Painter

/**
 * Local providers for various properties we connect to our components, for styling.
 */
private val LocalColors = compositionLocalOf<StreamColors> {
    error("No colors provided! Make sure to wrap all usages of Stream components in a ChatTheme.")
}
private val LocalDimens = compositionLocalOf<StreamDimens> {
    error("No dimens provided! Make sure to wrap all usages of Stream components in a ChatTheme.")
}
private val LocalTypography = compositionLocalOf<StreamTypography> {
    error("No typography provided! Make sure to wrap all usages of Stream components in a ChatTheme.")
}
private val LocalShapes = compositionLocalOf<StreamShapes> {
    error("No shapes provided! Make sure to wrap all usages of Stream components in a ChatTheme.")
}
private val LocalOwnMessageTheme = compositionLocalOf<MessageTheme> {
    error("No OwnMessageTheme provided! Make sure to wrap all usages of Stream components in a ChatTheme.")
}
private val LocalOtherMessageTheme = compositionLocalOf<MessageTheme> {
    error("No OtherMessageTheme provided! Make sure to wrap all usages of Stream components in a ChatTheme.")
}

/**
 * Our theme that provides all the important properties for styling to the user.
 *
 * @param isInDarkMode If we're currently in the dark mode or not. Affects only the default color palette that's
 * provided. If you customize [colors], make sure to add your own logic for dark/light colors.
 * @param colors The set of colors we provide, wrapped in [StreamColors].
 * @param dimens The set of dimens we provide, wrapped in [StreamDimens].
 * @param typography The set of typography styles we provide, wrapped in [StreamTypography].
 * @param shapes The set of shapes we provide, wrapped in [StreamShapes].
 * @param rippleTheme Defines the appearance for ripples.
 * @param attachmentFactories Attachment factories that we provide.
 * @param attachmentPreviewHandlers Attachment preview handlers we provide.
 * @param quotedAttachmentFactories Quoted attachment factories that we provide.
 * @param reactionIconFactory Used to create an icon [Painter] for the given reaction type.
 * @param dateFormatter [DateFormatter] used throughout the app for date and time information.
 * @param channelNameFormatter [ChannelNameFormatter] used throughout the app for channel names.
 * @param messagePreviewFormatter [MessagePreviewFormatter] used to generate a string preview for the given message.
 * @param imageLoaderFactory A factory that creates new Coil [ImageLoader] instances.
 * @param messageAlignmentProvider [MessageAlignmentProvider] used to provide message alignment for the given message.
 * @param messageOptionsUserReactionAlignment Alignment of the user reaction inside the message options.
 * @param attachmentsPickerTabFactories Attachments picker tab factories that we provide.
 * @param videoThumbnailsEnabled Dictates whether video thumbnails will be displayed inside video previews.
 * @param streamCdnImageResizing Sets the strategy for resizing images hosted on Stream's CDN. Disabled by default,
 * set [StreamCdnImageResizing.imageResizingEnabled] to true if you wish to enable resizing images. Note that resizing
 * applies only to images hosted on Stream's CDN which contain the original height (oh) and width (ow) query parameters.
 * @param ownMessageTheme Theme of the current user messages.
 * @param otherMessageTheme Theme of the other users messages.
 * @param streamMediaRecorder Used for recording audio messages.
 * @param content The content shown within the theme wrapper.
 */
@Composable
public fun ChatTheme(
    isInDarkMode: Boolean = isSystemInDarkTheme(),
    colors: StreamColors = if (isInDarkMode) StreamColors.defaultDarkColors() else StreamColors.defaultColors(),
    dimens: StreamDimens = StreamDimens.defaultDimens(),
    typography: StreamTypography = StreamTypography.defaultTypography(),
    shapes: StreamShapes = StreamShapes.defaultShapes(),
    rippleTheme: RippleTheme = StreamRippleTheme,

    ownMessageTheme: MessageTheme = MessageTheme.defaultOwnTheme(
        typography = typography,
        colors = colors,
    ),
    otherMessageTheme: MessageTheme = MessageTheme.defaultOtherTheme(
        typography = typography,
        colors = colors,
    ),
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalDimens provides dimens,
        LocalTypography provides typography,
        LocalShapes provides shapes,
        LocalRippleTheme provides rippleTheme,
        LocalOwnMessageTheme provides ownMessageTheme,
        LocalOtherMessageTheme provides otherMessageTheme,
    ) {
        content()
    }
}

/**
 * Contains ease-of-use accessors for different properties used to style and customize the app
 * look and feel.
 */
public object ChatTheme {
    /**
     * Retrieves the current [StreamColors] at the call site's position in the hierarchy.
     */
    public val colors: StreamColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    /**
     * Retrieves the current [StreamDimens] at the call site's position in the hierarchy.
     */
    public val dimens: StreamDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current

    /**
     * Retrieves the current [StreamTypography] at the call site's position in the hierarchy.
     */
    public val typography: StreamTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    /**
     * Retrieves the current [StreamShapes] at the call site's position in the hierarchy.
     */
    public val shapes: StreamShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

    /**
     * Retrieves the current own [MessageTheme] at the call site's position in the hierarchy.
     */
    public val ownMessageTheme: MessageTheme
        @Composable
        @ReadOnlyComposable
        get() = LocalOwnMessageTheme.current

    /**
     * Retrieves the current other [MessageTheme] at the call site's position in the hierarchy.
     */
    public val otherMessageTheme: MessageTheme
        @Composable
        @ReadOnlyComposable
        get() = LocalOtherMessageTheme.current
}
