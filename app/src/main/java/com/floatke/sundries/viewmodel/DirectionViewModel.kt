/*
 * Copyright [2019] xiongke.wang@outlook.com
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

package com.floatke.sundries.viewmodel

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.hardware.SensorManager.SENSOR_DELAY_UI
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.floatke.sundries.model.DirectionModel
import com.floatke.sundries.util.LogUtil

/**
 * Created on 2019/8/6
 * @author Xiongke Wang
 */
private const val TAG = "DirectionViewModel"

class DirectionViewModel(application: Application) : AndroidViewModel(application), SensorEventListener2 {


    private val direction: MutableLiveData<DirectionModel> = MutableLiveData()
    private val sensorManager: SensorManager = application.getSystemService(SensorManager::class.java) as SensorManager
    val eventLiveData: MutableLiveData<SensorEvent> = MutableLiveData()
    val degree: MutableLiveData<Double> = MutableLiveData()
    private var r = FloatArray(16)
    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)

    init {
        var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensor, SENSOR_DELAY_NORMAL)
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(this, sensor, SENSOR_DELAY_NORMAL)
    }

    override fun onFlushCompleted(sensor: Sensor) {
        LogUtil.d(TAG, "onFlushCompleted $sensor")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        LogUtil.d(TAG, "onAccuracyChanged $sensor $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent) {
        LogUtil.d(TAG, "onSensorChanged ${event.accuracy} ${event.sensor} ${event.timestamp} ${event.values.asList()}")
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values
        }
        if (SensorManager.getRotationMatrix(r, null, gravity, geomagnetic)) {
            val values = FloatArray(3)
            SensorManager.getOrientation(r, values)
            val azimuth = (360f + Math.toDegrees(values[0].toDouble()))% 360
            LogUtil.d(TAG, "degree : ${values.asList()}   $azimuth")
            degree.value = azimuth
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}