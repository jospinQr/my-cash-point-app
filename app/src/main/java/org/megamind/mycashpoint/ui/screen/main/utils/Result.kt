package org.megamind.mycashpoint.ui.screen.main.utils


sealed class Result<out T>(val data: T? = null, val e: Throwable? = null) {
    object Loading : Result<Nothing>()
    class Success<T>(data: T?) : Result<T>(data)
    class Error<T>( throwable: Throwable) : Result<T>(e = throwable)

}