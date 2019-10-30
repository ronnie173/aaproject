package com.appsian.aaproject.di

import com.appsian.aaproject.db.MyDatabase
import com.appsian.aaproject.repositories.MainRepository
import com.appsian.aaproject.state.AppExecutors
import com.appsian.aaproject.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val setupModule = module {

    single { MyDatabase.getInstance(androidContext()) }
    single { AppExecutors() }
}

val reposModule = module {
    factory { MainRepository() }
}

val viewModelModule = module {
    // MyViewModel ViewModel
    viewModel { MainViewModel() }
}
val daoModules = module {
 single { get<MyDatabase>().gitHubSearchResultsDao() }
}