package org.megamind.mycashpoint.utils


sealed class Result<out T>(val data: T? = null, val e: Throwable? = null) {
    object Loading : Result<Nothing>()
    class Success<T>(data: T?) : Result<T>(data)
    class Error<T>(val message: Throwable) : Result<T>(e = message)

}