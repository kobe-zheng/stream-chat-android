/*
 * Copyright (c) 2014-2023 Stream.io Inc. All rights reserved.
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

package io.getstream.chat.android.ui.feature.messages.list.adapter.view.internal

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.updateLayoutParams
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.audio.AudioPlayer
import io.getstream.chat.android.client.audio.AudioState
import io.getstream.chat.android.client.utils.attachment.isAudioRecording
import io.getstream.chat.android.models.Attachment
import io.getstream.chat.android.ui.common.utils.DurationParser
import io.getstream.chat.android.ui.utils.extensions.createStreamThemeWrapper
import io.getstream.chat.android.ui.utils.extensions.dpToPx

private const val NULL_DURATION = 0.0

/**
 * A LinearLayoutCompat that present the list of audio messages.
 */
@Suppress("MagicNumber")
public class AudioRecordsGroupView : LinearLayoutCompat {

    public constructor(context: Context) : super(context.createStreamThemeWrapper())
    public constructor(context: Context, attrs: AttributeSet?) : super(context.createStreamThemeWrapper(), attrs)
    public constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context.createStreamThemeWrapper(), attrs, defStyleAttr)

    init {
        setPadding(2.dpToPx(), 0.dpToPx(), 2.dpToPx(), 2.dpToPx())
    }

    private var audioAttachments: List<Attachment>? = null

    /**
     * Shows audio track.
     *
     * @param attachments attachments of type "audio_recording".
     */
    public fun showAudioAttachments(attachments: List<Attachment>) {
        removeAllViews()

        val audiosAttachment = attachments.filter { attachment -> attachment.isAudioRecording() }
        this.audioAttachments = audiosAttachment

        audiosAttachment.forEachIndexed(::addAttachmentPlayerView)
    }

    private fun addAttachmentPlayerView(index: Int, attachment: Attachment) {
        AudioRecordPlayer(context).apply {
            (attachment.extraData["duration"] as? Double)
                ?.toInt()
                ?.let(DurationParser::durationInMilliToReadableTime)
                ?.let(this::setTotalDuration)

            (attachment.extraData["waveList"] as? List<Float>)?.let(::setWaveBars)
        }.let { playerView ->
            if (attachment.assetUrl != null) {

                addView(playerView)

                if (index > 0) {
                    playerView.updateLayoutParams {
                        if (this is MarginLayoutParams) {
                            this.setMargins(0, 2.dpToPx(), 0, 0)
                        }
                    }
                }

                val audioPlayer = ChatClient.instance().audioPlayer
                val hashCode = attachment.hashCode()

                audioPlayer.registerStateChange(playerView, hashCode)
                playerView.registerButtonsListeners(audioPlayer, attachment, hashCode)
            }
        }
    }

    private fun AudioPlayer.registerStateChange(playerView: AudioRecordPlayer, hashCode: Int) {
        onAudioStateChange(hashCode) { audioState ->
            when (audioState) {
                AudioState.LOADING -> playerView.setLoading()
                AudioState.PAUSE -> playerView.setPaused()
                AudioState.UNSET, AudioState.IDLE -> playerView.setIdle()
                AudioState.PLAYING -> playerView.setPlaying()
            }
        }
        onProgressStateChange(hashCode) { (duration, progress) ->
            playerView.setDuration(DurationParser.durationInMilliToReadableTime(duration))
            playerView.setProgress(progress)
        }
        onSpeedChange(hashCode, playerView::setSpeedText)
    }

    private fun AudioRecordPlayer.registerButtonsListeners(
        audioPlayer: AudioPlayer,
        attachment: Attachment,
        hashCode: Int,
    ) {
        onPlayButtonPress {
            audioPlayer.clearTracks()
            audioAttachments?.forEachIndexed { index, attachment ->
                audioPlayer.registerTrack(attachment.assetUrl!!, attachment.hashCode(), index)
            }

            if (attachment.assetUrl != null) {
                audioPlayer.play(attachment.assetUrl!!, hashCode)
            } else {
                setLoading()
            }
        }

        onSpeedButtonPress {
            audioPlayer.changeSpeed()
        }

        onSeekbarMove({
            audioPlayer.startSeek(attachment.hashCode())
        }, { progress ->
            audioPlayer.seekTo(
                progressToDecimal(progress, attachment.extraData["duration"] as? Double),
                attachment.hashCode()
            )
        })
    }

    private fun progressToDecimal(progress: Int, totalDuration: Double?): Int =
        progress * (totalDuration ?: NULL_DURATION).toInt() / 100

    /**
     * Unbinds the view.
     */
    public fun unbind() {
        audioAttachments?.map { attachment -> attachment.hashCode() }
            ?.let(ChatClient.instance().audioPlayer::removeAudios)
    }
}