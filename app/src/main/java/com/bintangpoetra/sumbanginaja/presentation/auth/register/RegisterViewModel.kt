package com.bintangpoetra.sumbanginaja.presentation.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintangpoetra.sumbanginaja.data.lib.ApiResponse
import com.bintangpoetra.sumbanginaja.domain.auth.AuthUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authUseCase: AuthUseCase
): ViewModel() {

    val registerUserResult: LiveData<ApiResponse<String>> by lazy { _registerUserResult }
    private val _registerUserResult = MutableLiveData<ApiResponse<String>>()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        type: String
    ) {
        viewModelScope.launch {
            authUseCase.registerUser(name, email, password, type)
                .collect {
                    _registerUserResult.value = it
                }
        }
    }

}