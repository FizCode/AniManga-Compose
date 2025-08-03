package dev.fizcode.common.base.callhandler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Converts a [Flow] from a local database operation (e.g., Room query)
 * into a flow of [DomainLocalState], representing success, empty, or error states.
 *
 * This utility helps standardize local data state handling in the domain layer
 * for easier transformation and consumption in the UI layer.
 *
 * ### Behavior:
 * - Maps emitted data to:
 *   - [DomainLocalState.Success] if the result is non-empty.
 *   - [DomainLocalState.Empty] if the result is null, blank, or an empty collection/map.
 * - Catches exceptions during collection or transformation and emits [DomainLocalState.Error].
 *
 * ### Use Case:
 * Recommended for Room DAO queries wrapped in `Flow`, where result emptiness and exceptions
 * must be handled explicitly.
 *
 * @param block A lambda that returns a [Flow] from a local database (e.g., Room DAO).
 * @return A [Flow] of [DomainLocalState], reflecting the status of the local operation.
 */
inline fun <T> processDatabaseCall(
    crossinline block: () -> Flow<T>
): Flow<DomainLocalState<T>> = flow {
    block()
        .map<T, DomainLocalState<T>> { data ->
            val isEmpty = when (data) {
                is Collection<*> -> data.isEmpty()
                is Map<*, *> -> data.isEmpty()
                is String -> data.isBlank()
                null -> true
                else -> false
            }
            if (isEmpty) DomainLocalState.Empty else DomainLocalState.Success(data)
        }
        .catch { e -> emit(DomainLocalState.Error(e as? Exception)) }
        .collect { emit(it) }
}
