package com.floatke.sundries.model

import android.os.Bundle

/**
 * Created on 2019/8/6
 * @author Xiongke Wang
 */
data class LocationStatusModel(
    val provider: String,
    val status: Int,
    val extras: Bundle
)