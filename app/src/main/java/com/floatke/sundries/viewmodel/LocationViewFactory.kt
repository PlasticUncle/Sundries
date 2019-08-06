package com.floatke.sundries.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.InvocationTargetException

/**
 * Created on 2019/8/6
 * @author Xiongke Wang
 */
class LocationViewFactory(private val context: Context) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (LocationViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(Context::class.java).newInstance(context)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }

        }
        throw RuntimeException("Cannot create an instance of $modelClass")
    }
}