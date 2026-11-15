package dev.fizcode.mal.util

/** MyAnimeList rejects a search shorter than this with a 400 "invalid q". */
private const val MIN_SEARCH_QUERY_LENGTH = 3

/** MyAnimeList ignores it when matching, so padding a query with it does not change what is found. */
private const val SEARCH_QUERY_PADDING = '_'

/**
 * Makes [query] long enough for a MyAnimeList search by padding it, so titles of one or two
 * characters, such as "K", can be searched.
 */
fun malSearchQuery(query: String): String =
    query.padEnd(MIN_SEARCH_QUERY_LENGTH, SEARCH_QUERY_PADDING)
