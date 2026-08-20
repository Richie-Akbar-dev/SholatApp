package com.sholatapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

/**
 * Helper class to get the user's current location using Google Play Services.
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient = FusedLocationProviderClient(context)

    /**
     * Get the current location (latitude, longitude).
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationResult {
        return try {
            val cancellationToken = CancellationTokenSource()
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationToken.token
            ).await()

            if (location != null) {
                val address = reverseGeocode(location.latitude, location.longitude)
                LocationResult.Success(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    address = address
                )
            } else {
                val lastLocation = fusedLocationClient.lastLocation.await()
                if (lastLocation != null) {
                    val address = reverseGeocode(lastLocation.latitude, lastLocation.longitude)
                    LocationResult.Success(
                        latitude = lastLocation.latitude,
                        longitude = lastLocation.longitude,
                        address = address
                    )
                } else {
                    LocationResult.Error("Tidak dapat menemukan lokasi")
                }
            }
        } catch (e: Exception) {
            LocationResult.Error(e.localizedMessage ?: "Error mendapatkan lokasi")
        }
    }

    private fun reverseGeocode(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale("id", "ID"))
            val addresses: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                buildString {
                    addr.subAdminArea?.let { if (it.isNotBlank()) append("$it, ") }
                    addr.locality?.let { if (it.isNotBlank()) append("$it, ") }
                    addr.adminArea?.let { if (it.isNotBlank()) append(it) }
                    if (isEmpty()) append("${String.format("%.4f", lat)}, ${String.format("%.4f", lng)}")
                }.trimEnd(',', ' ')
            } else {
                "${String.format("%.4f", lat)}, ${String.format("%.4f", lng)}"
            }
        } catch (e: Exception) {
            "${String.format("%.4f", lat)}, ${String.format("%.4f", lng)}"
        }
    }
}

sealed class LocationResult {
    data class Success(
        val latitude: Double,
        val longitude: Double,
        val address: String
    ) : LocationResult()

    data class Error(val message: String) : LocationResult()
}
