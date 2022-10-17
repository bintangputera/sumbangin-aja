package com.bintangpoetra.sumbanginaja.data.auth

import com.bintangpoetra.sumbanginaja.data.auth.remote.AuthService
import com.bintangpoetra.sumbanginaja.data.lib.ApiResponse
import com.bintangpoetra.sumbanginaja.domain.auth.mapper.toDomain
import com.bintangpoetra.sumbanginaja.domain.auth.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception

class AuthDataStore(
    private val api: AuthService
): AuthRepository {

    override fun loginUser(email: String, password: String): Flow<ApiResponse<User>> = flow {
        try {
            emit(ApiResponse.Loading)
            val response = api.loginUser(email, password)

            if (response.status) {
                val userData = response.data.toDomain()
                emit(ApiResponse.Success(userData))
            } else {
                emit(ApiResponse.Error(response.message))
            }
        } catch (ex: Exception) {
            emit(ApiResponse.Error(ex.toString()))
            Timber.e(ex.toString())
        }
    }

    override fun registerUser(): Flow<ApiResponse<User>> = flow {
        try {
            emit(ApiResponse.Loading)
            val response = api.registerUser()

            if (response.status) {
                val userData = response.data.toDomain()
                emit(ApiResponse.Success(userData))
            } else {
                emit(ApiResponse.Error(response.message))
            }
        } catch (ex: Exception) {
            emit(ApiResponse.Error(ex.toString()))
            Timber.e(ex.toString())
        }
    }

}