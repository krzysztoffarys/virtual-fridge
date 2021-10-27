package com.example.virtualfridge.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualfridge.other.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _registerStatus = MutableLiveData<AuthResult>()
    val registerStatus: LiveData<AuthResult> = _registerStatus
    //
    private val _loginStatus = MutableLiveData<AuthResult>()
    val loginStatus: LiveData<AuthResult> = _loginStatus



    fun registerUser(email: String, password: String, confirmedPassword: String) {
        _registerStatus.postValue(AuthResult.loading(null))
        if (email.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
            _registerStatus.postValue(AuthResult.error("Please fill out all the fields"))
            return
        }
        if(password != confirmedPassword) {
            _registerStatus.postValue(AuthResult.error("The passwords do not match"))
            return
        }
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _registerStatus.postValue(AuthResult.success(null))
                    }
                    else {
                        _registerStatus.postValue(AuthResult.error(it.exception.toString()))
                    }
                }

            } catch (e: Exception) {
                _registerStatus.postValue(AuthResult.error(e.message.toString()))
            }
        }
    }

    fun loginUser(email: String, password: String) {
        _loginStatus.postValue(AuthResult.loading(null))
        if (email.isEmpty() || password.isEmpty()) {
            _loginStatus.postValue(AuthResult.error("Please fill out all the fields"))
            return
        }

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _loginStatus.postValue(AuthResult.success(null))
                    }
                    else {
                        _loginStatus.postValue(AuthResult.error(it.exception.toString()))
                    }
                }

            } catch (e: Exception) {
                _loginStatus.postValue(AuthResult.error(e.message.toString()))
            }
        }
    }

    fun isLoggedIn() : Boolean {
        return auth.currentUser != null
    }

}