package com.example.nammametromvvm.utility

import java.io.IOException

class ApiException(message: String): IOException(message)
class NoInternetException(message: String):IOException(message)
class ApiError(message: String): Exception(message)
class GeneralException(message: String): Exception(message)
class ErrorException : java.lang.Exception {
    val code: Int

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(message: String?, cause: Throwable?, code: Int) : super(message, cause) {
        this.code = code
    }

    constructor(message: String?, code: Int) : super(message) {
        this.code = code
    }

    constructor(cause: Throwable?, code: Int) : super(cause) {
        this.code = code
    }

    constructor(message: String?) : super(message) {
        this.code = -1
    }
}
