package com.bankmandiri.cartshop.core.exception

sealed class Failure {
    data object NetworkConnection : Failure()
    data object  UserNotFound : Failure()
    data object BadRequest : Failure()
    data object InternalServerError : Failure()
    data class ServerError(val message: String) : Failure()
}