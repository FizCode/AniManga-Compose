package dev.fizcode.common.base.callhandler

fun isEmptyResult(value: Any?): Boolean = when (value) {
    null -> true
    is Collection<*> -> value.isEmpty()
    is Map<*, *> -> value.isEmpty()
    is String -> value.isBlank()
    else -> false
}
