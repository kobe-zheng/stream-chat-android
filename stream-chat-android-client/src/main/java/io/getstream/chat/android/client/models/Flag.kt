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
 
package io.getstream.chat.android.client.models

import java.util.Date

public data class Flag(
    val user: User,
    val targetUser: User?,
    val targetMessageId: String,
    val reviewedBy: String,
    val createdByAutomod: Boolean,
    val createdAt: Date,
    val updatedAt: Date,
    val reviewedAt: Date,
    val approvedAt: Date?,
    val rejectedAt: Date,
)
