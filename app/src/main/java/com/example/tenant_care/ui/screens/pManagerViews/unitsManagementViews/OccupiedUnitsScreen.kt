package com.example.tenant_care.ui.screens.pManagerViews.unitsManagementViews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenant_care.EstateEaseViewModelFactory
import com.example.tenant_care.R
import com.example.tenant_care.model.property.PropertyTenant
import com.example.tenant_care.model.property.PropertyUnit
import com.example.tenant_care.ui.theme.Tenant_careTheme
import com.example.tenant_care.util.FilterByNumOfRoomsBox
import com.example.tenant_care.util.FilterByRoomNameBox
import com.example.tenant_care.util.ReusableFunctions
import com.example.tenant_care.util.SearchFieldForTenants
import com.example.tenant_care.util.UndoFilteringBox
import com.example.tenant_care.util.tenant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OccupiedUnitsComposable(
    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: OccupiedUnitsScreenViewModel = viewModel(factory = EstateEaseViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val rooms = uiState.roomNames
    var selectedPropertyIndex by remember {
        mutableIntStateOf(0)
    }
    var showUnitDetails by remember {
        mutableStateOf(false)
    }
    if (showUnitDetails) {
        OccupiedUnitDetails(
            propertyUnit = uiState.properties[selectedPropertyIndex],
            onBackButtonClicked = { showUnitDetails = !showUnitDetails }
        )
    } else {
        Box(
            modifier = modifier
        ) {
            OccupiedUnitsScreen(
                rooms = rooms,
                numberOfRoomsSelected = uiState.numOfRoomsSelected,
                properties = uiState.properties,
                onSelectNumOfRooms = {
                    viewModel.filterByNumberOfRooms(it)
                },
                searchText = uiState.tenant,
                onSearchTextChanged = {
                    viewModel.filterByTenantName(it)
                },
                selectedUnitName = uiState.unitName,
                onChangeSelectedUnitName = {
                    viewModel.filterByRoomName(it)
                },
                unfilterUnits = {
                    viewModel.unfilterProperties()
                },
                numberOfUnits = uiState.properties.size,
                navigateToOccupiedUnitDetailsScreen = navigateToOccupiedUnitDetailsScreen,
            )
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OccupiedUnitsScreen(
    rooms: List<String>,
    numberOfRoomsSelected: String?,
    properties: List<PropertyUnit>,
    onSelectNumOfRooms: (rooms: String) -> Unit,
    searchText: String?,
    onSearchTextChanged: (newValue: String) -> Unit,
    selectedUnitName: String?,
    onChangeSelectedUnitName: (name: String) -> Unit,
    unfilterUnits: () -> Unit,
    numberOfUnits: Int,
    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchFieldForTenants(
            labelText = "Search Tenant name",
            value = searchText.takeIf { it != null } ?: "",
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            FilterByNumOfRoomsBox(
                selectedNumOfRooms = numberOfRoomsSelected,
                onSelectNumOfRooms = onSelectNumOfRooms
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterByRoomNameBox(
                rooms = rooms,
                selectedUnit = selectedUnitName,
                onChangeSelectedUnitName = onChangeSelectedUnitName
            )
            Spacer(modifier = Modifier.weight(1f))
            UndoFilteringBox(
                unfilterUnits = unfilterUnits
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "$numberOfUnits units",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn() {
            items(properties.size) {
                OccupiedUnitItem(
                    navigateToOccupiedUnitDetailsScreen = navigateToOccupiedUnitDetailsScreen,
                    propertyUnit = properties[it],
                    propertyIndex = it,
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OccupiedUnitItem(
    propertyUnit: PropertyUnit,
    propertyIndex: Int,
    navigateToOccupiedUnitDetailsScreen: (propertyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navigateToOccupiedUnitDetailsScreen(propertyUnit.propertyUnitId.toString())
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(20.dp)
        ) {
            Column(

            ) {
                Row {
                    Text(text = "Room No: ")
                    Text(
                        text = propertyUnit.propertyNumberOrName,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(text = "Occupant: ")
                    Text(text = propertyUnit.tenants[0].fullName)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Text(text = "Occupant Since: ")
                    Text(
                        text = ReusableFunctions.formatDateTimeValue(propertyUnit.tenants[0].tenantAddedAt),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Unit actions"
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OccupiedUnitDetails(
    propertyUnit: PropertyUnit,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            IconButton(onClick = { onBackButtonClicked() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {

                Text(
                    text = propertyUnit.propertyNumberOrName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Uploaded on: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = propertyUnit.propertyAddedAt)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = ": ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = propertyUnit.rooms)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Monthly rent: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = ReusableFunctions.formatMoneyValue(propertyUnit.monthlyRent))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = "Current tenant: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = propertyUnit.tenants[0].fullName)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "Tenant since: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = ReusableFunctions.formatDateTimeValue(propertyUnit.tenants[0].tenantAddedAt))
                }

            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Archive unit")
        }

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun OccupiedUnitItemPreview() {
    val property = PropertyUnit (
        propertyUnitId = 0,
        rooms = "Bedsitter",
        propertyNumberOrName = "Col C2",
        propertyDescription = "2-bedroom unit",
        monthlyRent = 7200.00,
        propertyAddedAt = "20/08/2023 18:06",
        propertyAssignmentStatus = false,
        activeTenant = tenant,
        tenants = emptyList(),
        meterReadings = emptyList(),
    )
    Tenant_careTheme {
        OccupiedUnitItem(
            propertyUnit = property,
            propertyIndex = 1,
            navigateToOccupiedUnitDetailsScreen = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ActiveUnitsComposablePreview() {
    Tenant_careTheme {
        OccupiedUnitsScreen(
            rooms = emptyList(),
            numberOfRoomsSelected = null,
            properties = emptyList(),
            onSelectNumOfRooms = {},
            searchText = null,
            selectedUnitName = null,
            onChangeSelectedUnitName = {},
            onSearchTextChanged = {},
            unfilterUnits = {},
            numberOfUnits = 5,
            navigateToOccupiedUnitDetailsScreen = {}
        )
    }
}