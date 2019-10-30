/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ces.apilib.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.appsian.aaproject.state.AppExecutors
import com.appsian.aaproject.state.RequestResult
import timber.log.Timber
import java.util.concurrent.FutureTask

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 *
 * Source: Google samples (GithubBrowserSample).
 *
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<REQUEST_TYPE, RESULT_TYPE>
    @MainThread constructor(protected val appExecutors: AppExecutors){

    private val result = MediatorLiveData<StateHolder<RESULT_TYPE>>()

    /**
     * Begin request execution.
     * @return An instance of this NetworkBoundResource.
     */
    @CallSuper
    open fun execute(): NetworkBoundResource<REQUEST_TYPE, RESULT_TYPE> {
        // Expiration may be stored in the database.
        // Retrieve it from a background thread to circumvent IllegalStateException.
        val isExpiredFuture: FutureTask<Boolean> = FutureTask {
            isExpired()
        }
        appExecutors.diskIO().execute(isExpiredFuture)

        result.value = StateHolder.loading(null)

        val diskSource = loadFromDisk()

        // Prevent IllegalStateException.
        result.removeSource(diskSource)

        result.addSource(diskSource) { diskData ->
            result.removeSource(diskSource)

            val isExpired = isExpiredFuture.get()
            Timber.d("shouldFetch(data):: isExpired? $isExpired")
            if(shouldFetch(diskData, isExpired)) {
                fetchFromNetwork(diskSource)
            } else {
                result.addSource(diskSource) { newDiskData ->
                    setState(StateHolder.success(newDiskData))
                }
            }
        }

        return this
    }

    @MainThread
    private fun setState(newState: StateHolder<RESULT_TYPE>) {
        if(result.value != newState) {
            result.value = newState
        }
    }

    private fun fetchFromNetwork(dataSource: LiveData<RESULT_TYPE>) {
        appExecutors.networkIO().execute {
            val requestResult = makeApiCall()

           when {
                requestResult.isError() -> {
                    appExecutors.mainThread().execute {
                        onFetchFailed()
                        // Reload from existing data source.
                        result.addSource(dataSource) { newData ->
                            setState(StateHolder.error(requestResult.status, newData))
                        }
                    }
                }

                requestResult.result == null -> {
                    appExecutors.mainThread().execute {
                        onFetchFailed()
                        result.addSource(dataSource) { newData ->
                            setState(StateHolder.error(null, newData))
                        }
                    }
                }

                else -> {
                    saveCallResult(requestResult.result)
                    // Refresh data source.
                    val newDataSource = loadFromDisk()
                    appExecutors.mainThread().execute {
                        result.addSource(newDataSource) { newData ->
                        setState(StateHolder.success(newData))
                        }
                    }
                }
            }
        }
    }

    /**
     * @param isExpired False by default. Override [isExpired] to customize expiration logic.
     */
    protected abstract fun shouldFetch(data: RESULT_TYPE?, isExpired: Boolean): Boolean

    @MainThread
    protected abstract fun loadFromDisk(): LiveData<RESULT_TYPE>

    @WorkerThread
    protected abstract fun makeApiCall(): RequestResult<REQUEST_TYPE>

    @WorkerThread
    protected abstract fun saveCallResult(item: REQUEST_TYPE?)

    protected abstract fun onFetchFailed()

    /**
     * Override this method to customize expiration logic.
     *
     * @return False by default. True if data is expired.
     */
    @WorkerThread
    protected open fun isExpired(): Boolean {
        return false
    }


    fun asLiveData() = result as LiveData<StateHolder<RESULT_TYPE>>
}
