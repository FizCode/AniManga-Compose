package dev.fizcode.common.base.callhandler

import dev.fizcode.common.util.ErrorType

/**
 * Represents the state of a network call in the domain layer,
 * providing a sealed and type-safe way to model successful, empty, or failed responses.
 *
 * This class is typically used to wrap responses from repository or remote data sources,
 * enabling the caller to differentiate between success, expected empty results,
 * and various categories of network-related errors.
 *
 * @param T The type of the data returned on success.
 */
sealed class DomainNetworkState<out T> {

    /**
     * Represents a successful response with no content.
     * Common for cases like empty lists, empty strings, or null results.
     */
    data object Empty : DomainNetworkState<Nothing>()

    /**
     * Represents a successful network response containing non-empty data.
     *
     * @param data The successfully retrieved result.
     */
    data class Success<T>(val data: T) : DomainNetworkState<T>()

    /**
     * Represents a failed network response, optionally including exception details,
     * HTTP status code, and a categorized error type.
     *
     * @param exception The exception that caused the failure, if available.
     * @param message A human-readable message describing the error, if any.
     * @param code The HTTP status code returned by the server, if applicable.
     * @param type The categorized error type, e.g. client, server, network, or unknown.
     */
    data class ErrorNetwork(
        val exception: Exception? = null,
        val message: String? = null,
        val code: Int? = null, // HTTP status code, if available
        val type: ErrorType = ErrorType.Unknown
    ) : DomainNetworkState<Nothing>()
}

/**
 * Represents the result state of a local (database) operation in the domain layer.
 * Commonly used for Room database queries or cached data retrieval.
 *
 * This sealed class allows distinguishing between successful, empty, and error cases
 * from local data sources such as Room.
 *
 * @param T The type of the data returned on success.
 */
sealed class DomainLocalState<out T> {

    /**
     * Represents a successful result with non-empty data from the local database.
     *
     * @param data The data returned from the local source.
     */
    data class Success<T>(val data: T) : DomainLocalState<T>()

    /**
     * Indicates that the database query returned no data.
     * This could be an empty list, null object, or blank string.
     */
    data object Empty : DomainLocalState<Nothing>()

    /**
     * Represents an error that occurred while accessing or reading from the local database.
     *
     * @param exception The exception that was thrown.
     * @param message Optional message explaining the error.
     */
    data class Error(
        val exception: Exception? = null,
        val message: String? = exception?.message
    ) : DomainLocalState<Nothing>()
}
