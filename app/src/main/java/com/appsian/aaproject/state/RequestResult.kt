package com.appsian.aaproject.state

class RequestStatus {
    var errorType = ErrorType.SUCCESS
    var httpCode: HttpCode = HttpCode.OK
    var cesCode: CesResponseCode = CesResponseCode.NoError
    var localCode: Int? = null
    var errorMessage: String = ""
}

class RequestResult<T> {
    val errorType
        get() = status.errorType

    val httpCode
        get() = status.httpCode

    val cesCode
        get() = status.cesCode

    val errorMessage
        get() = status.errorMessage

    var result: T? = null

    val status = RequestStatus()

    fun setHttpError(code: Int, message: String) {
        status.httpCode = HttpCode.fromInt(code)

        if(HttpCode.isError(httpCode)) {
            status.errorType = ErrorType.HTTP_ERROR
            status.errorMessage = message
        }
    }

    fun setTimeoutError() {
        status.errorType = ErrorType.TIMEOUT
    }

    fun setNetworkError() {
        status.errorType = ErrorType.NETWORK_ERROR
    }

    fun setLocalError(code: Int) {
        status.errorType = ErrorType.LOCAL_ERROR
        status.localCode = code
    }

    fun setUnknownError(message: String?) {
        status.errorType = ErrorType.UNKNOWN
        status.errorMessage = message ?: ""
    }

    fun setCesError(errorCode: String?, errorDescription: String?) {
        status.cesCode = CesResponseCode.fromString(errorCode ?: "")
        when(cesCode) {
            CesResponseCode.Success, CesResponseCode.NoError -> {}
            else -> status.errorType = ErrorType.CES_ERROR
        }

        status.errorMessage = errorDescription ?: ""
    }

    fun isError(): Boolean {
        return errorType != ErrorType.SUCCESS
    }
}

enum class ErrorType {
    /** Network timeout error. */
    TIMEOUT,

    /** Error connecting to the Internet. */
    NETWORK_ERROR,

    /** HTTP error. */
    HTTP_ERROR,

    /** CES API error. */
    CES_ERROR,

    /** Custom error defined by this application. (For example, file IO errors.) */
    LOCAL_ERROR,

    /** Unrecognized error. */
    UNKNOWN,

    /** No error. */
    SUCCESS
}