package com.ces.apilib.state

import com.appsian.aaproject.state.RequestStatus

/**
 * Tracks the state of a background event.
 *
 * @param status Tracks the current state. See [Status] for possible states.
 * @param data The result of the background operation.
 * @param requestResult Contains error information. (Codes, error messages, etc.)
 */
data class StateHolder<T> constructor(val status: Status, val data: T?, val requestResult: RequestStatus?) {

    companion object {
        fun <T> loading(temporaryData: T?): StateHolder<T> {
            return StateHolder(Status.LOADING, temporaryData, null)
        }

        fun <T> success(data: T?): StateHolder<T> {
            return StateHolder(Status.SUCCESS, data, null)
        }

        fun <T> error(requestResult: RequestStatus?): StateHolder<T> {
            return StateHolder(Status.ERROR, null, requestResult)
        }

        fun <T> error(requestResult: RequestStatus?, cachedData: T?): StateHolder<T> {
            return StateHolder(Status.ERROR, cachedData, requestResult)
        }
    }
}