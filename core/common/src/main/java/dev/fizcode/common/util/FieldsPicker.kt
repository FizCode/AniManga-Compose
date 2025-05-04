package dev.fizcode.common.util

/**
 * Builds a comma-separated string of field names for use with MyAnimeList API's `fields` parameter.
 *
 * @param fields A list of field names to be included in the query.
 * @return A comma-separated string of the provided fields.
 *
 * Example:
 * ```
 * fieldsPicker(AnimeFieldsConstant.MEAN, AnimeFieldsConstant.STATUS)
 * // returns: "mean,status"
 * ```
 */
fun fieldsPicker(vararg fields: String): String =
    fields.joinToString(Constant.COMMA)
