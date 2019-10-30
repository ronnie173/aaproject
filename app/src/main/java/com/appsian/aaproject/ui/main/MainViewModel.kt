package com.appsian.aaproject.ui.main

import androidx.lifecycle.ViewModel
import com.appsian.aaproject.api.MyApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainViewModel : ViewModel() {

    val apiService by lazy {
        MyApiService.create()
    }
    var disposable: Disposable? = null
    var i = 0
    fun beginSearch(searchString: String) {
        disposable = apiService.getData(searchString, "stars", "desc")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Timber.d("")
            }
            .doOnNext {
                it.items.forEach { items ->
                    Timber.d("Jerome ${items.name}")
                    i++
                }
                Timber.d("Jerome total count $i")
            }
            .subscribe()


    }
}
