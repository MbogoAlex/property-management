package com.example.tenant_care.network

import com.example.tenant_care.model.additionalExpense.AdditionalExpenseResponseBody
import com.example.tenant_care.model.additionalExpense.AdditionalExpenseUpdateRequestBody
import com.example.tenant_care.model.amenity.AmenitiesResponseBody
import com.example.tenant_care.model.amenity.AmenityDeletionResponseBody
import com.example.tenant_care.model.amenity.AmenityRequestBody
import com.example.tenant_care.model.amenity.AmenityResponseBody
import com.example.tenant_care.model.caretaker.CaretakerLoginRequestBody
import com.example.tenant_care.model.caretaker.CaretakerLoginResponseBody
import com.example.tenant_care.model.caretaker.CaretakerPaymentRequestBody
import com.example.tenant_care.model.caretaker.CaretakerPaymentResponseBody
import com.example.tenant_care.model.caretaker.CaretakerRegistrationRequestBody
import com.example.tenant_care.model.caretaker.CaretakerRegistrationResponseBody
import com.example.tenant_care.model.caretaker.CaretakerResponseBody
import com.example.tenant_care.model.caretaker.CaretakersResponseBody
import com.example.tenant_care.model.caretaker.MeterReadingRequestBody
import com.example.tenant_care.model.caretaker.MeterReadingResponseBody
import com.example.tenant_care.model.caretaker.MeterReadingsResponseBody
import com.example.tenant_care.model.message.MessageRequestBody
import com.example.tenant_care.model.message.MessageResponseBody
import com.example.tenant_care.model.pManager.PManagerRequestBody
import com.example.tenant_care.model.pManager.PManagerResponseBody
import com.example.tenant_care.model.pManager.RentPaymentDetailsResponseBody
import com.example.tenant_care.model.pManager.RentPaymentOverView
import com.example.tenant_care.model.pManager.RentPaymentRowUpdateRequestBody
import com.example.tenant_care.model.pManager.RentPaymentRowUpdateResponseBody
import com.example.tenant_care.model.pManager.RentPaymentRowsUpdateResponseBody
import com.example.tenant_care.model.penalty.PenaltyRequestBody
import com.example.tenant_care.model.penalty.PenaltyResponseBody
import com.example.tenant_care.model.penalty.PenaltyStatusChangeResponseBody
import com.example.tenant_care.model.penalty.PenaltyUpdateRequestBody
import com.example.tenant_care.model.property.ArchiveUnitResponseBody
import com.example.tenant_care.model.property.PropertyRequestBody
import com.example.tenant_care.model.property.PropertyResponseBody
import com.example.tenant_care.model.property.PropertyUnitResponseBody
import com.example.tenant_care.model.property.SinglePropertyUnitResponseBody
import com.example.tenant_care.model.tenant.LoginTenantRequestBody
import com.example.tenant_care.model.tenant.LoginTenantResponseBody
import com.example.tenant_care.model.tenant.PaymentStatusResponseBody
import com.example.tenant_care.model.tenant.RentPaymentRequestBody
import com.example.tenant_care.model.tenant.RentPaymentResponseBody
import com.example.tenant_care.model.tenant.RentPaymentRowsResponse
import com.example.tenant_care.model.tenant.UnitAssignmentRequestBody
import com.example.tenant_care.model.tenant.UnitAssignmentResponseBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // login pmanager
    @POST("pmanager/login")
    suspend fun loginPManager(
        @Body pManagerLoginDetails: PManagerRequestBody
    ) : Response<PManagerResponseBody>

    // fetch all properties

    @GET("propertyunit")
    suspend fun fetchAllProperties(): Response<PropertyUnitResponseBody>

    // fetch property by propertyId
    @GET("propertyunit/propertyId={propertyId}")
    suspend fun fetchPropertyByPropertyId(@Path("propertyId") propertyId: Int): Response<SinglePropertyUnitResponseBody>

    // fetch all occupied properties
    @GET("propertyunit/filter")
    suspend fun fetchFilteredProperties(
        @Query("tenantName") tenantName: String?,
        @Query("rooms") rooms: String?,
        @Query("roomName") roomName: String?,
        @Query("occupied") occupied: Boolean
    ): Response<PropertyUnitResponseBody>
    @GET("rentpayment/overview/month={month}/year={year}")
    suspend fun fetchRentPaymentOverview(
        @Path("month") month: String,
        @Path("year") year: String
    ): Response<RentPaymentOverView>

    // Add a new unit
    @POST("propertyunit")
    suspend fun addNewUnit(
        @Body propertyRequestBody: PropertyRequestBody
    ): Response<PropertyResponseBody>

    // Update unit
    @PUT("propertyunit/{id}")
    suspend fun updatePropertyUnit(
        @Path("id") propertyId: Int,
        @Body propertyRequestBody: PropertyRequestBody
    ): Response<SinglePropertyUnitResponseBody>

    // fetch unoccupied properties
    @GET("propertyunit/unoccupied")
    suspend fun fetchUnoccupiedUnits(): Response<PropertyUnitResponseBody>

    // assign unit

    @POST("tenant")
    suspend fun assignPropertyUnit(
        @Body assignmentDetails: UnitAssignmentRequestBody
    ): Response<UnitAssignmentResponseBody>

    // archive unit
    @PUT("propertyunit/archive/propertyId={propertyId}/tenantId={tenantId}")
    suspend fun archiveUnit(
        @Path("propertyId") propertyId: String,
        @Path("tenantId") tenantId: String
    ): Response<ArchiveUnitResponseBody>

    // fetch rent payments for tenants

    @GET("rentpayment/detailed")
    suspend fun fetchRentPaymentStatusForAllTenants(
        @Query("month") month: String,
        @Query("year") year: String,
        @Query("roomName") roomName: String?,
        @Query("rooms") rooms: Int?,
        @Query("tenantName") tenantName: String?,
        @Query("tenantId") tenantId: Int?,
        @Query("rentPaymentStatus") rentPaymentStatus: Boolean?,
        @Query("paidLate") paidLate: Boolean?,
        @Query("tenantActive") tenantActive: Boolean?,
    ): Response<RentPaymentDetailsResponseBody>

    // activate late payment penalty for single tenant
    @PUT("tenant/penalty/activate/rentPaymentTblId={rentPaymentTblId}")
    suspend fun activateLatePaymentPenaltyForSingleTenant(
        @Body rentPayment: RentPaymentRowUpdateRequestBody,
        @Path("rentPaymentTblId") rentPaymentTblId: Int
    ): Response<RentPaymentRowUpdateResponseBody>

    // activate late payment penalty for single tenant

    @PUT("tenant/penalty/deactivate/rentPaymentTblId={rentPaymentTblId}")
    suspend fun deActivateLatePaymentPenaltyForSingleTenant(
        @Path("rentPaymentTblId") rentPaymentTblId: Int
    ): Response<RentPaymentRowUpdateResponseBody>

    // activate late payment penalty for single tenant

    @PUT("tenant/penalty/activate/month={month}/year={year}")
    suspend fun activateLatePaymentPenaltyForMultipleTenants(
        @Body rentPayment: RentPaymentRowUpdateRequestBody,
        @Path("month") month: String,
        @Path("year") year: String,
    ): Response<RentPaymentRowsUpdateResponseBody>

    @PUT("tenant/penalty/deactivate/month={month}/year={year}")
    suspend fun deActivateLatePaymentPenaltyForMultipleTenants(
        @Path("month") month: String,
        @Path("year") year: String,
    ): Response<RentPaymentRowsUpdateResponseBody>

    // login tenant
    @POST("tenant/login")
    suspend fun loginTenant(
        @Body tenant: LoginTenantRequestBody
    ): Response<LoginTenantResponseBody>

    // get rent payment rows for various tenants
    @GET("tenant/tenantrentpaymentrow")
    suspend fun getRentPaymentRows(
        @Query("tenantId") tenantId: Int,
        @Query("month") month: String?,
        @Query("year") year: String?,
        @Query("roomName") roomName: String?,
        @Query("rooms") rooms: Int?,
        @Query("tenantName") tenantName: String?,
        @Query("rentPaymentStatus") rentPaymentStatus: Boolean?,
        @Query("paidLate") paidLate: Boolean?,
        @Query("tenantActive") tenantActive: Boolean?
    ): Response<RentPaymentRowsResponse>

    // pay rent
    @POST("rentpayment/rentPaymentTblId={rentPaymentTblId}")
    suspend fun payRent(
        @Path("rentPaymentTblId") rentPaymentTblId: Int,
        @Body rentPaymentRequestBody: RentPaymentRequestBody
    ): Response<RentPaymentResponseBody>

    @GET("tenant/payment/{id}")
    suspend fun getRentPaymentStatus(@Path("id") id: Int): Response<PaymentStatusResponseBody>
    @GET("tenant/rentpaymentsreport")
    suspend fun getRentPaymentRowsAndGenerateReport(
        @Query("tenantId") tenantId: Int,
        @Query("month") month: String?,
        @Query("year") year: String?,
        @Query("roomName") roomName: String?,
        @Query("rooms") rooms: Int?,
        @Query("tenantName") tenantName: String?,
        @Query("rentPaymentStatus") rentPaymentStatus: Boolean?,
        @Query("paidLate") paidLate: Boolean?,
        @Query("tenantActive") tenantActive: Boolean?
    ): Response<ResponseBody>

    // caretaker login
    @POST("caretaker/login")
    suspend fun loginAsCaretaker(
        @Body caretaker: CaretakerLoginRequestBody,
    ): Response<CaretakerLoginResponseBody>

    // get meter readings
    @GET("meterreading/all")
    suspend fun getMeterReadings(
        @Query("month") month: String?,
        @Query("year") year: String?,
        @Query("meterReadingTaken") meterReadingTaken: Boolean?,
        @Query("tenantName") tenantName: String?,
        @Query("propertyName") propertyName: String?,
        @Query("role") role: String?
    ): Response<MeterReadingsResponseBody>

    // upload meterreading
    @Multipart
    @PUT("meterreading")
    suspend fun uploadMeterReading(
        @Part("data") meterReadingRequestBody: MeterReadingRequestBody,
        @Part image: MultipartBody.Part
    ): Response<MeterReadingResponseBody>

    // get specific meter reading
    @GET("meterreading/id={id}")
    suspend fun getMeterReadingDataById(
        @Path("id") id: Int
    ): Response<MeterReadingResponseBody>

    // update meterreading
    @Multipart
    @PUT("meterreading/oldImageId={oldImageId}/meterReadingDataTableId={meterReadingDataTableId}")
    suspend fun updateMeterReading(
        @Path("oldImageId") oldImageId: Int,
        @Path("meterReadingDataTableId") meterReadingDataTableId: Int,
        @Part("data") meterReadingRequestBody: MeterReadingRequestBody,
        @Part image: MultipartBody.Part
    ): Response<MeterReadingResponseBody>

    // add amenity
    @Multipart
    @POST("amenity")
    suspend fun addAmenity(
        @Part("data") amenityRequestBody: AmenityRequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<AmenityResponseBody>

    @Multipart
    @PUT("amenity/{id}/images")
    suspend fun updateAmenityWithImages(
        @Part("data") amenityRequestBody: AmenityRequestBody,
        @Part newImages: List<MultipartBody.Part>?,
        @Path("id") amenityId: Int,
    ): Response<AmenityResponseBody>


    @PUT("amenity/{id}")
    suspend fun updateAmenityWithoutImages(
        @Body amenityRequestBody: AmenityRequestBody,
        @Path("id") amenityId: Int,
    ): Response<AmenityResponseBody>

    @DELETE("amenity/{id}")
    suspend fun deleteAmenity(
        @Path("id") amenityId: Int
    ): Response<AmenityDeletionResponseBody>

    @GET("amenity")
    suspend fun fetchAmenities(): Response<AmenitiesResponseBody>

    @GET("amenity/{id}")
    suspend fun fetchAmenity(@Path("id") id: Int): Response<AmenityResponseBody>

    @GET("amenity/filtered")
    suspend fun fetchFilteredAmenities(@Query("value") searchText: String?): Response<AmenitiesResponseBody>

    @DELETE("amenity/{id}/image")
    suspend fun deleteAmenityImage(@Path("id") id: Int): Response<AmenityDeletionResponseBody>

    @GET("penalty/{id}")
    suspend fun fetchPenalty(@Path("id") id: Int): Response<PenaltyResponseBody>

    @PUT("tenant/penalty/activate/month={month}/year={year}")
    suspend fun activateLatePaymentPenalty(
        @Path("month") month: String,
        @Path("year") year: String,
        @Body penaltyRequestBody: PenaltyRequestBody,
    ): Response<PenaltyStatusChangeResponseBody>

    @PUT("tenant/penalty/deactivate/month={month}/year={year}")
    suspend fun deActivateLatePaymentPenalty(
        @Path("month") month: String,
        @Path("year") year: String
    ): Response<PenaltyStatusChangeResponseBody>

    @PUT("penalty/{id}")
    suspend fun updatePenalty(
        @Path("id") id: Int,
        @Body penaltyUpdateRequestBody: PenaltyUpdateRequestBody
    ): Response<PenaltyResponseBody>

    @PUT("expense/{id}")
    suspend fun updateExpense(
        @Body additionalExpenseUpdateRequestBody: AdditionalExpenseUpdateRequestBody,
        @Path("id") id: Int
    ): Response<AdditionalExpenseResponseBody>

    @GET("expense/{id}")
    suspend fun getExpense(@Path("id") id: Int): Response<AdditionalExpenseResponseBody>

    @GET("caretaker/active")
    suspend fun getActiveCaretakers(): Response<CaretakersResponseBody>

    @GET("caretaker/{id}")
    suspend fun getCaretaker(@Path("id") id: Int): Response<CaretakerResponseBody>

    @POST("caretaker/register")
    suspend fun registerCaretaker(@Body caretaker: CaretakerRegistrationRequestBody): Response<CaretakerRegistrationResponseBody>

    @POST("caretaker/payment")
    suspend fun payCaretaker(@Body caretakerPaymentRequestBody: CaretakerPaymentRequestBody): Response<CaretakerPaymentResponseBody>

    @GET("caretaker/payment/{id}")
    suspend fun getCaretakerPaymentStatus(@Path("id") id: String): Response<PaymentStatusResponseBody>

    @PUT("caretaker/deregister/{caretakerId}")
    suspend fun removeCaretaker(@Path("caretakerId") id: Int): Response<CaretakerLoginResponseBody>

    @GET("rentpayment/generalreport")
    suspend fun generateGeneralReport(
        @Query("month") month: String?,
        @Query("year") year: String?,
        @Query("roomName") roomName: String?,
        @Query("rooms") rooms: String?,
        @Query("tenantName") tenantName: String?,
        @Query("tenantId") tenantId: Int?,
        @Query("rentPaymentStatus") rentPaymentStatus: Boolean?,
        @Query("paidLate") paidLate: Boolean?,
    ): Response<ResponseBody>

    @POST("sendSms")
    suspend fun sendSms(@Body messageRequestBody: MessageRequestBody): Response<MessageResponseBody>
}