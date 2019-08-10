/*
 * Copyright 2019 floatke@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.floatke.sundries.view.compass


import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.floatke.sundries.BuildConfig
import com.floatke.sundries.R
import com.floatke.sundries.extensions.allPermissionGranted
import com.floatke.sundries.viewmodel.DirectionViewModel
import com.floatke.sundries.viewmodel.LocationViewFactory
import com.floatke.sundries.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_compass.*

private val REQUEST_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.BODY_SENSORS
)

private const val REQUEST_PERMISSION_CODE = 10

private const val TAG = "CompassFragment"


//TODO broken device sensor?
class CompassFragment : Fragment() {

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var directionViewModel: DirectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_compass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        debug_info.visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        directionViewModel = ViewModelProviders.of(this).get(DirectionViewModel::class.java)
        directionViewModel.degree.observe(this, Observer {
            debug_info.text = "$it"
        })
    }

    override fun onResume() {
        super.onResume()
        if (!allPermissionGranted(REQUEST_PERMISSIONS)) {
            requestPermissions(
                REQUEST_PERMISSIONS,
                REQUEST_PERMISSION_CODE
            )
            if (::locationViewModel.isInitialized) {
                locationViewModel.stop()
            }
            return
        }
        if (::locationViewModel.isInitialized) {
            locationViewModel.start()
        } else {
            startObserverLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_PERMISSION_CODE == requestCode) {
            if (!allPermissionGranted(REQUEST_PERMISSIONS)) {
                Toast.makeText(
                    requireContext(),
                    R.string.feature_incomplete_with_no_permissions,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                startObserverLocation()
            }
        }
    }

    private fun startObserverLocation() {
        if (!::locationViewModel.isInitialized) {
            locationViewModel =
                ViewModelProviders.of(this, LocationViewFactory(requireContext())).get(LocationViewModel::class.java)
            locationViewModel.location.observe(this, Observer {
                Log.d(TAG, "location : $it")
            })
            locationViewModel.locationStatus.observe(this, Observer {
                Log.d(TAG, "location : $it")
            })
        }
    }
}
