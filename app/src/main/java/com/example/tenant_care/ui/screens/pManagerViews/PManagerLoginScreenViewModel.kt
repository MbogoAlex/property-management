package com.example.tenant_care.ui.screens.pManagerViews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.datastore.UserDSDetails
import com.example.tenant_care.model.pManager.PManagerRequestBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LOGIN_STATUS {
    INITIAL,
    LOADING,
    SUCCESS,
    FAIL
}
data class PManagerLoginScreenUiState(
    val pManagerLoginDetails: PManagerLoginDetails = PManagerLoginDetails(),
    val showLoginButton: Boolean = false,
    val loginStatus: LOGIN_STATUS = LOGIN_STATUS.INITIAL,
    val loginResponseMessage: String = ""
)

data class PManagerLoginDetails(
    val phoneNumber: String = "",
    val password: String = "",
)

class PManagerLoginScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
):ViewModel() {
    private val _uiState = MutableStateFlow(value = PManagerLoginScreenUiState())
    val uiState: StateFlow<PManagerLoginScreenUiState> = _uiState.asStateFlow()

    var pManagerLoginDetails by mutableStateOf(PManagerLoginDetails())

    fun updatePhoneNumber(phoneNumber: String) {
        pManagerLoginDetails = pManagerLoginDetails.copy(
            phoneNumber = phoneNumber
        )
        _uiState.update {
            it.copy(
                pManagerLoginDetails = pManagerLoginDetails,
                showLoginButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun updatePassword(password: String) {
        pManagerLoginDetails = pManagerLoginDetails.copy(
            password = password
        )
        _uiState.update {
            it.copy(
                pManagerLoginDetails = pManagerLoginDetails,
                showLoginButton = checkIfFieldsAreEmpty()
            )
        }
    }

    fun checkIfFieldsAreEmpty(): Boolean {
        return pManagerLoginDetails.phoneNumber.isNotEmpty() && pManagerLoginDetails.password.isNotEmpty()
    }

    fun loginPManager() {
        _uiState.update {
            it.copy(
                loginStatus = LOGIN_STATUS.LOADING,
            )
        }
        val pManagerRequestBody = PManagerRequestBody(
            phoneNumber = _uiState.value.pManagerLoginDetails.phoneNumber,
            password = _uiState.value.pManagerLoginDetails.password
        )

        Log.i("LOGIN_DETAILS", pManagerRequestBody.toString())

        // login attempt

        viewModelScope.launch {
            try {
                val response = apiRepository.loginPManager(pManagerRequestBody)
                if(response.isSuccessful) {

                    // save to datastore

                    val userDSDetails = UserDSDetails(
                        roleId = 1,
                        userId = response.body()?.data?.pmanager?.pmanagerId!!,
                        fullName = response.body()?.data?.pmanager?.fullName!!,
                        email = response.body()?.data?.pmanager?.email!!,
                        phoneNumber = response.body()?.data?.pmanager?.phoneNumber!!,
                        userAddedAt = response.body()?.data?.pmanager?.propertyManagerAddedAt!!,
                        room = "",
                        password = pManagerLoginDetails.password,

                        )
                    dsRepository.saveUserDetails(userDSDetails)

                    // update UI

                    _uiState.update {
                        it.copy(
                            loginStatus = LOGIN_STATUS.SUCCESS,
                            loginResponseMessage = "Login success"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loginStatus = LOGIN_STATUS.FAIL,
                            loginResponseMessage = "Invalid credentials"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("LOGIN_FAILED", e.toString())
                _uiState.update {
                    it.copy(
                        loginStatus = LOGIN_STATUS.FAIL,
                        loginResponseMessage = e.message.toString()
                    )
                }
            }
        }

    }

    fun resetLoginStatus() {
        _uiState.update {
            it.copy(
                loginStatus = LOGIN_STATUS.INITIAL
            )
        }
    }
}