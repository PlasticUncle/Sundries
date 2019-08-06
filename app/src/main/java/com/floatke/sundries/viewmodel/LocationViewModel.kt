package com.floatke.sundries.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.floatke.sundries.model.LocationStatusModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "LocationViewModel"

/**
 * Created on 2019/8/6
 * @author Xiongke Wang
 */
//fixme callback of LocationListener never be invoked
class LocationViewModel(context: Context) : ViewModel(), LocationListener {
    private val locationManager = context.getSystemService(LocationManager::class.java)
    @Volatile
    private var registered = false
    private val criteria: Criteria = Criteria().apply {
        accuracy = Criteria.ACCURACY_FINE
        isCostAllowed = true
        horizontalAccuracy = Criteria.ACCURACY_HIGH
        verticalAccuracy = Criteria.ACCURACY_HIGH
        speedAccuracy = Criteria.ACCURACY_HIGH
        bearingAccuracy = Criteria.ACCURACY_HIGH
        powerRequirement = Criteria.POWER_HIGH
        isAltitudeRequired = true
        isBearingRequired = true
        isSpeedRequired = true
        isCostAllowed = true
    }

    val locationStatus: MutableLiveData<LocationStatusModel> = MutableLiveData()
    val location: MutableLiveData<Location> = MutableLiveData()

    init {
        register()
    }

    private fun register() {
        if (registered) return
        try {
            Log.d(TAG, "$this register for location ${LocationManagerCompat.isLocationEnabled(locationManager)}")
            locationManager.requestSingleUpdate(criteria, this, null)
            val provider = LocationManager.NETWORK_PROVIDER
            locationManager.getLastKnownLocation(provider)?.apply {
                onLocationChanged(this)
            }
            Log.d(TAG, "provider is $provider")
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0F, this)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, this)
            registered = true
        } catch (se: SecurityException) {
            // ignore
        } catch (ie: IllegalArgumentException) {
            // ignore
        }
    }

    fun start() {
        register()
    }

    @SuppressLint("MissingPermission")
    fun stop() {
        if (registered) {
            Log.d(TAG, "$this unregister for location")
            locationManager.removeUpdates(this)
            registered = false
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged: $location")
        GlobalScope.launch(Dispatchers.Main) {
            this@LocationViewModel.location.value = location
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d(TAG, "onStatusChanged: $provider $status ${extras.keySet()}")
        GlobalScope.launch(Dispatchers.Main) {
            locationStatus.value = LocationStatusModel(provider, status, extras)
        }
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(TAG, "$provider is enabled")
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(TAG, "$provider is disabled")
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }

}