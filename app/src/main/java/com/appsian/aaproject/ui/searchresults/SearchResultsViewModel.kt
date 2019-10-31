package com.appsian.aaproject.ui.searchresults

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.appsian.aaproject.api.MyApiService
import com.appsian.aaproject.db.entities.Repo
import com.appsian.aaproject.repositories.MainRepository
import com.appsian.aaproject.utils.AbsentLiveData
import com.appsian.aaproject.utils.Resource

class SearchResultsViewModel(mainRepository: MainRepository):ViewModel() {
    private val _query = MutableLiveData<String>()
    val query: LiveData<String>
        get() = _query
    val apiService by lazy {
        MyApiService.create()
    }

    val repositories: LiveData<Resource<List<Repo>>> = Transformations
        .switchMap(query) { query ->
            if (query == null) {
                AbsentLiveData.create()
            } else {
                apiService
                mainRepository.loadRepos(query)
            }
        }


    fun setQuery(query:String){
        _query.value = query
    }
}