package com.example.tenant_care.ui.screens.caretakerViews.units

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.util.LoadingStatus
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.ReusableFunctions.toUserDetails
import com.example.tenant_care.util.tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UnoccupiedUnitsScreenUiState(
    val properties: List<PropertyUnit> = emptyList(),
    val currentTenant: PropertyTenant = tenant,
    val rooms: String = "",
    val roomName: String = "",
    val selectableRooms: List<String> = emptyList(),
    val filteringOn: Boolean = false,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val loadingStatus: LoadingStatus = LoadingStatus.INITIAL
)
class UnoccupiedUnitsScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(value = UnoccupiedUnitsScreenUiState())
    val uiState: StateFlow<UnoccupiedUnitsScreenUiState> = _uiState.asStateFlow()

    fun loadStartUpData() {
        viewModelScope.launch {
            dsRepository.userDSDetails.collect() {dsUserDetails ->
                _uiState.update {
                    it.copy(
                        userDetails = dsUserDetails.toUserDetails()
                    )
                }
            }
        }
        loadUnoccupiedUnits()
    }

    fun updateRoomName(roomName: String) {
        _uiState.update {
            it.copy(
                roomName = roomName,
                filteringOn = true
            )
        }
        loadUnoccupiedUnits()
    }

    fun updateRooms(rooms: String) {
        _uiState.update {
            it.copy(
                rooms = rooms,
                filteringOn = true
            )
        }
        loadUnoccupiedUnits()
    }

    fun unfilter() {
        _uiState.update {
            it.copy(
                roomName = "",
                rooms = "",
                filteringOn = false
            )
        }
        loadUnoccupiedUnits()
    }

    private fun loadUnoccupiedUnits() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
                val response = apiRepository.fetchFilteredProperties(
                    tenantName = null,
                    rooms = _uiState.value.rooms.takeIf { it.isNotEmpty() },
                    roomName = _uiState.value.roomName,
                    occupied = false
                )
                if(response.isSuccessful) {
                    val rooms = mutableListOf<String>()
                    for(property in response.body()?.data?.property!!) {
                        rooms.add(property.propertyNumberOrName)
                    }
                    _uiState.update {
                        it.copy(
                            properties = response.body()?.data?.property!!.reversed(),
                            selectableRooms = rooms,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAILURE
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAILURE
                    )
                }
            }
        }
    }

    init {
        loadStartUpData()
    }
}