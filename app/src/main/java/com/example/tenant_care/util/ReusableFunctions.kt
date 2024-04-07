package com.example.tenant_care.util

import com.example.tenant_care.datastore.UserDSDetails
import java.text.NumberFormat
import java.util.Locale

object ReusableFunctions {
    val formattedRent = NumberFormat.getCurrencyInstance(Locale("en", "KE")).format(10.0)
    // enforce only email values to be entered
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    data class UserDetails(
        val roleId: Int = 0,
        val userId: Int = 0,
        val fullName: String = "",
        val email: String = "",
        val userAddedAt: String = "",
        val phoneNumber: String = ""
    )

    // format currency values

    fun formatMoneyValue(amount: Double): String {
        return  NumberFormat.getCurrencyInstance(Locale("en", "KE")).format(amount)
    }

    // userUDDetails to UserDetails

    fun UserDSDetails.toUserDetails(): UserDetails = UserDetails(
        userId = userId!!,
        roleId = roleId!!,
        fullName = fullName,
        email = email,
        userAddedAt = userAddedAt,
        phoneNumber = phoneNumber
    )
}