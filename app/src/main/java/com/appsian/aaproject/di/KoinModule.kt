package com.appsian.aaproject.di

import com.appsian.aaproject.api.MyApiService
import com.appsian.aaproject.db.MyDatabase
import com.appsian.aaproject.repositories.MainRepository
import com.appsian.aaproject.ui.searchresults.SearchResultsViewModel
import com.appsian.aaproject.utils.AppExecutors
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val setupModule = module {

    single { MyDatabase.getInstance(androidContext()) }
    single { AppExecutors() }
    single { MyApiService.create() }
}

val reposModule = module {
    factory { MainRepository(get(),get(),get(),get()) }
}

val viewModelModule = module {
    // MyViewModel ViewModel
    viewModel { SearchResultsViewModel(get()) }
}
val daoModules = module {
 single { get<MyDatabase>().gitHubSearchResultsDao() }
}