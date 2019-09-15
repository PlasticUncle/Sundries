/*
 * Copyright 2019 xiongke.wang@outlook.com
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

package com.floatke.sundries

import android.os.Looper
import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Main {

    companion object {
        @JvmStatic
        fun main(vararg  args: String) {
            println("main")
            val dispatcher1 = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val dispatcher2 = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

            GlobalScope.launch(dispatcher1) {
                var i = 0
                while (i <= 100) {
                    withContext(dispatcher1) {
                        println("${Thread.currentThread().id}  i: $i")
                        i = i.inc()
                    }
                    withContext(dispatcher2) {
                        println("${Thread.currentThread().id}  i: $i")
                        i = i.inc()
                    }
                }

            }
        }
    }
}