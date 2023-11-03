package com.kobez.chatmodule.models

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
public data class Message(
    /**
     * The unique string identifier of the message. This is either created by Stream
     * or set on the client side when the message is added.
     */
    val id: String = "",

    /**
     * The text of this message
     */
    val text: String = "",

    /**
     * The message text formatted as HTML
     */
    val html: String = "",

    /**
     * Contains type of the message. Can be one of the following: regular, ephemeral,
     * error, reply, system, deleted.
     */
    val type: String = "",

    /**
     * When the message was created
     */
    val createdAt: Date? = null,

    /**
     * The user who sent the message
     */
    val user: User = User(),

    /**
     * All the custom data provided for this message
     */
    val extraData: Map<String, Any> = mapOf(),
    ) {
    public companion object {
        public const val TYPE_REGULAR: String = "regular"
        public const val TYPE_EPHEMERAL: String = "ephemeral"
    }

    override fun toString(): String = StringBuilder().apply {
        append("Message(")
        append("type=\"").append(type).append("\"")
        append(", id=\"").append(id).append("\"")
        append(", text=\"").append(text).append("\"")
        append(", html=\"").append(html).append("\"")
        if (createdAt != null) append(", createdAt=").append(createdAt)
        append(", sentBy=").append("User(id=\"").append(user.id).append("\", name=\"").append(user.name).append("\")")
        if (extraData.isNotEmpty()) append(", extraData=").append(extraData)
        append(")")
    }.toString()

    @SinceKotlin("99999.9")
    @Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
    public fun newBuilder(): Builder = Builder(this)

    @Suppress("TooManyFunctions")
    public class Builder() {
        private var id: String = ""
        private var text: String = ""
        private var html: String = ""
        private var type: String = ""
        private var createdAt: Date? = null
        private var user: User = User()
        private var extraData: Map<String, Any> = mapOf()

        public constructor(message: Message) : this() {
            id = message.id
            text = message.text
            html = message.html
            type = message.type
            createdAt = message.createdAt
            user = message.user
            extraData = message.extraData
        }

        public fun withId(id: String): Builder = apply { this.id = id }
        public fun withText(text: String): Builder = apply { this.text = text }
        public fun withHtml(html: String): Builder = apply { this.html = html }
        public fun withType(type: String): Builder = apply { this.type = type }
        public fun withCreatedAt(createdAt: Date?): Builder = apply { this.createdAt = createdAt }
        public fun withUser(user: User): Builder = apply { this.user = user }
        public fun withExtraData(extraData: Map<String, Any>): Builder = apply { this.extraData = extraData }

        public fun build(): Message {
            return Message(
                id = id,
                text = text,
                html = html,
                type = type,
                createdAt = createdAt,
                user = user,
                extraData = extraData,
            )
        }
    }
}
