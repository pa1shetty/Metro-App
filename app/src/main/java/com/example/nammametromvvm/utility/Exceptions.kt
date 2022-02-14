package com.example.nammametromvvm.utility

import java.io.IOException

class ApiException(message: String): IOException(message)
class NoInternetException(message: String):IOException(message)
@Suppress("unused")
class ApiError(message: String): Exception(message)
class GeneralException(message: String): Exception(message)
class ErrorException : java.lang.Exception {
    val code: Int
    @Suppress("unused")
    constructor(code: Int) : super() {
        this.code = code
    }
    @Suppress("unused")
    constructor(message: String?, cause: Throwable?, code: Int) : super(message, cause) {
        this.code = code
    }
    @Suppress("unused")
    constructor(message: String?, code: Int) : super(message) {
        this.code = code
    }
    @Suppress("unused")
    constructor(cause: Throwable?, code: Int) : super(cause) {
        this.code = code
    }

    constructor(message: String?) : super(message) {
        this.code = -1
    }
}
