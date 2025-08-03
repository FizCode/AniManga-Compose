package dev.fizcode.common.util

/**
 * Represents the category of an error returned from a network call.
 */
enum class ErrorType {
    Redirect, // 3xx
    Client, // 4xx
    Server, // 5xx
    Network, // IO or timeout
    Unknown // unclassified error
}
