package com.example.nammametromvvm.utility

import java.io.IOException

class ApiException(message: String): IOException(message)
class NoInternetException(message: String):IOException(message)
class ApiError(message: String): Exception(message)
class GeneralException(message: String): Exception(message)
