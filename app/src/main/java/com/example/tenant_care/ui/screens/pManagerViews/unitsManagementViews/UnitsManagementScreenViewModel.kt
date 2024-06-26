package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.tenant_care.network.ApiRepository
import com.example.tenant_care.datastore.DSRepository
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.util.ReusableFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class Screen {
    OCCUPIED_UNITS,
    UNOCCUPIED_UNITS,
    ADD_UNIT
}
enum class FetchingStatus {
    INITIAL,
    FETCHING,
    SUCCESS,
    FAIL
}
data class UnitsManagementScreenUiState(
    val properties: List<PropertyUnit> = emptyList(),
    val fetchingStatus: FetchingStatus = FetchingStatus.INITIAL,
    val userDetails: ReusableFunctions.UserDetails = ReusableFunctions.UserDetails(),
    val childScreen: String = "",
    val currentScreen: Screen = Screen.OCCUPIED_UNITS
)
class UnitsManagementScreenViewModel(
    private val apiRepository: ApiRepository,
    private val dsRepository: DSRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(value = UnitsManagementScreenUiState())
    val uiState: StateFlow<UnitsManagementScreenUiState> = _uiState.asStateFlow()

    private val childScreen: String? = savedStateHandle[UnitsManagementComposableDestination.childScreen]
    fun changeScreen(screen: Screen) {
        _uiState.update {
            it.copy(
                currentScreen = screen
            )
        }
    }

    fun resetChildScreen() {
        _uiState.update {
            it.copy(
                childScreen = ""
            )
        }
    }
    init {
        _uiState.update {
            it.copy(
                childScreen = childScreen.takeIf { childScreen != null } ?: ""
            )
        }
    }

}