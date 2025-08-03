package dev.fizcode.common.base.domainhandler

import dev.fizcode.common.base.callhandler.DomainNetworkState
import dev.fizcode.common.base.callhandler.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Combines the results of two domain suspend functions that return [UiState] instances,
 * and merges their successful data results into a single [UiState.Success] with a mapped model.
 *
 * This function invokes [domain1] and [domain2] sequentially and expects both to return
 * [UiState.Success]. If both succeed, it applies the [model] function to combine their data
 * into a single [Result] object, which is wrapped in [UiState.Success].
 *
 * If either call fails or returns a non-success state, the function returns [UiState.Empty].
 *
 * It also catches common HTTP exceptions (3xx, 4xx, 5xx) and other exceptions,
 * wrapping them into corresponding [UiState.Error*] types for error handling.
 *
 * @param Req1 The type of data returned by the first domain suspend function.
 * @param Req2 The type of data returned by the second domain suspend function.
 * @param Result The combined result type returned by the [model] mapping function.
 * @param domain1 The first suspend function returning a [UiState] with data of type [Req1].
 * @param domain2 The second suspend function returning a [UiState] with data of type [Req2].
 * @param model A function that combines successful data from both [Req1] and [Req2] into [Result].
 *
 * @return A [UiState] wrapping the combined result on success, or an appropriate error or empty state otherwise.
 *
 * Example usage:
 * ```
 * val combinedResult: UiState<CombinedDomainModel> = combineMalAndJikan(
 *     domain1 = { fetchMalAnimeDetails(animeId) },
 *     domain2 = { fetchJikanAnimeDetails(animeId) },
 *     returnModel = { malData, jikanData -> CombinedDomainModel(malData, jikanData) }
 * )
 * ```
 */
suspend fun <Req1, Req2, Result> combineMalAndJikan(
    domain1: suspend () -> DomainNetworkState<Req1>,
    domain2: suspend () -> DomainNetworkState<Req2>,
    returnModel: (Req1, Req2) -> Result
): DomainNetworkState<Result> = coroutineScope {
    try {
        val state1 = async { domain1() }.await()
        val state2 = async { domain2() }.await()

        if (state1 is DomainNetworkState.Success && state2 is DomainNetworkState.Success) {
            DomainNetworkState.Success(returnModel(state1.data, state2.data))
        } else {
            DomainNetworkState.Empty
        }

    } catch (e: Exception) {
        DomainNetworkState.ErrorNetwork(exception = e, message = e.message)
    }
}
