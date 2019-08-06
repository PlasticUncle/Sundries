package com.floatke.sundries.extensions

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Created on 2019/8/6
 * @author Xiongke Wang
 */

/**
 * extension for check permission
 */
fun Fragment.allPermissionGranted(permissions: Array<String>) = permissions.all {
    ContextCompat.checkSelfPermission(this.requireContext(), it) == PackageManager.PERMISSION_GRANTED
}
