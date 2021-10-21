package com.example.virtualfridge.other

data class AuthResult(val status: Status, val message: String?) {
    companion object {
        fun success(message: String?): AuthResult {
            return AuthResult(Status.SUCCESS, message)
        }
        fun error(message: String): AuthResult {
            return AuthResult(Status.ERROR, message)
        }
        fun loading(message: String?): AuthResult {
            return AuthResult(Status.LOADING, message)
        }
    }
}


enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}