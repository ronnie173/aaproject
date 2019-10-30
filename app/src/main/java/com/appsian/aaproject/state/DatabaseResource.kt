package com.ces.apilib.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

/**
 * Helper class to coordinate Room DB LiveData with loading LiveData.
 *
 * This class supports parameterized requests that may change during runtime. Requests that vary by
 * a single parameter, such as an ID, may pass the parameter directly. If a request requires multiple
 * parameters, the developer must define a Parameter object that contains all parameters.
 */
abstract class DatabaseResource<PARAMETER_TYPE, RESULT_TYPE> {
    private val parameters = MutableLiveData<PARAMETER_TYPE>()
    private val dbResult: LiveData<RESULT_TYPE> = Transformations.switchMap<PARAMETER_TYPE, RESULT_TYPE>(parameters) {
        loadFromDisk(it)
    }

    private val result = MediatorLiveData<StateHolder<RESULT_TYPE>>()

    init {
        result.value = StateHolder.loading(null)

        // Prevent IllegalStateException.
        result.removeSource(dbResult)

        result.addSource(dbResult) { data ->
            setState(StateHolder.success(data))
        }
    }

    fun execute(params: PARAMETER_TYPE): DatabaseResource<PARAMETER_TYPE, RESULT_TYPE> {
        result.value = StateHolder.loading(null)
        this.parameters.value = params
        return this
    }

    private fun setState(newState: StateHolder<RESULT_TYPE>) {
        if(result.value != newState) {
            result.value = newState
        }
    }

    protected abstract fun loadFromDisk(params: PARAMETER_TYPE): LiveData<RESULT_TYPE>

    fun asLiveData() = result as LiveData<StateHolder<RESULT_TYPE>>
}