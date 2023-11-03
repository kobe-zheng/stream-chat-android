package com.kobez.chatmodule.models

import androidx.compose.runtime.Immutable
import java.util.Date

/**
 * Represents a user
 *
 * @param id The unique id of the user. This field if required.
 * @param role Determines the set of user permissions.
 * @param name User's name.
 * @param createdAt Date/time of creation
 * @param extraData A map of custom fields for the user.
 */
@Immutable
public data class User(
    val id: String = "",
    val role: String = "",
    val name: String = "",
    val createdAt: Date? = null,
    val extraData: Map<String, Any> = mapOf(),
) {

    fun getComparableField(fieldName: String): Comparable<*>? {
        return when (fieldName) {
            "id" -> id
            "role" -> role
            "name" -> name
            "createdAt" -> createdAt
            else -> extraData[fieldName] as? Comparable<*>
        }
    }

    @SinceKotlin("99999.9")
    @Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
    public fun newBuilder(): Builder = Builder(this)

    public class Builder() {
        private var id: String = ""
        private var role: String = ""
        private var name: String = ""
        private var createdAt: Date? = null
        private var extraData: Map<String, Any> = mutableMapOf()

        public constructor(user: User) : this() {
            id = user.id
            role = user.role
            name = user.name
            createdAt = user.createdAt
            extraData = user.extraData
        }
        public fun withId(id: String): Builder = apply { this.id = id }
        public fun withRole(role: String): Builder = apply { this.role = role }
        public fun withName(name: String): Builder = apply { this.name = name }
        public fun withCreatedAt(createdAt: Date?): Builder = apply { this.createdAt = createdAt }
        public fun withExtraData(extraData: Map<String, Any>): Builder = apply { this.extraData = extraData }

        public fun build(): User {
            return User(
                id = id,
                role = role,
                name = name,
                createdAt = createdAt,
                extraData = extraData.toMutableMap(),
            )
        }
    }
}
