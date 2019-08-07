package com.floatke.sundries.util

import android.util.Log
import com.floatke.sundries.BuildConfig

/**
 * Created on 2019/8/7
 * @author Xiongke Wang
 */
class LogUtil {

    companion object {
        @JvmStatic
        fun d(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, msg)
            }
        }

        @JvmStatic
        fun d(tag: String, msg: String, thr: Throwable) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, msg, thr)
            }
        }

        @JvmStatic
        fun i(tag: String, msg: String) {
            Log.i(tag, msg)
        }

        @JvmStatic
        fun i(tag: String, msg: String, thr: Throwable) {
            Log.i(tag, msg, thr)
        }

        @JvmStatic
        fun v(tag: String, msg: String) {
            Log.v(tag, msg)
        }

        @JvmStatic
        fun v(tag: String, msg: String, thr: Throwable) {
            Log.v(tag, msg, thr)
        }

        @JvmStatic
        fun e(tag: String, msg: String) {
            Log.e(tag, msg)
        }

        @JvmStatic
        fun e(tag: String, msg: String, thr: Throwable) {
            Log.e(tag, msg, thr)
        }


        @JvmStatic
        fun w(tag: String, msg: String) {
            Log.w(tag, msg)
        }

        @JvmStatic
        fun w(tag: String, msg: String, thr: Throwable) {
            Log.w(tag, msg, thr)
        }

        @JvmStatic
        fun wtf(tag: String, msg: String) {
            Log.wtf(tag, msg)
        }

        @JvmStatic
        fun wtf(tag: String, thr: Throwable) {
            Log.wtf(tag, thr)
        }

        @JvmStatic
        fun wtf(tag: String, msg: String, thr: Throwable) {
            Log.wtf(tag, msg, thr)
        }
    }
}