package dev.fizcode.datasource.local.model

private const val SYNONYM_SEPARATOR = "\n"

/** Synonyms are kept in one column, separated by a new line which a title never contains. */
fun encodeSynonyms(synonyms: List<String>): String =
    synonyms.filter { it.isNotBlank() }.joinToString(SYNONYM_SEPARATOR)

fun decodeSynonyms(value: String): List<String> =
    value.split(SYNONYM_SEPARATOR).filter { it.isNotBlank() }
