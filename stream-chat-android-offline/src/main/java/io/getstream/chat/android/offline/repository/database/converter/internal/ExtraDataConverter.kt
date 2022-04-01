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
 
package io.getstream.chat.android.offline.repository.database.converter.internal

import androidx.room.TypeConverter
import com.squareup.moshi.adapter

internal class ExtraDataConverter {
    @OptIn(ExperimentalStdlibApi::class)
    private val adapter = moshi.adapter<Map<String, Any>>()

    @TypeConverter
    fun stringToMap(data: String?): Map<String, Any>? {
        if (data.isNullOrEmpty() || data == "null") {
            return emptyMap()
        }
        return adapter.fromJson(data)
    }

    @TypeConverter
    fun mapToString(someObjects: Map<String, Any>?): String? {
        if (someObjects == null) {
            return "{}"
        }
        return adapter.toJson(someObjects)
    }
}
