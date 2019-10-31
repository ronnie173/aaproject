package com.appsian.aaproject.repositories

import androidx.lifecycle.LiveData
import com.appsian.aaproject.api.MyApiService
import com.appsian.aaproject.db.MyDatabase
import com.appsian.aaproject.db.daos.GitHubSearchResultsDao
import com.appsian.aaproject.db.entities.Repo
import com.appsian.aaproject.utils.*
import java.util.concurrent.TimeUnit

class MainRepository(
    private val appExecutors: AppExecutors,
    private val db: MyDatabase,
    private val searchResultsDao: GitHubSearchResultsDao,
    private val myApiService: MyApiService
) {
    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadRepos(owner:String): LiveData<Resource<List<Repo>>> {
        return object :NetworkBoundResource<List<Repo>,List<Repo>>(appExecutors){
            override fun saveCallResult(item: List<Repo>) {
                searchResultsDao.insertRepos(item)
            }

            override fun shouldFetch(data: List<Repo>?): Boolean {
              return true
            }

            override fun loadFromDb(): LiveData<List<Repo>> {
                return searchResultsDao.loadRepos()
            }

            override fun createCall(): LiveData<ApiResponse<List<Repo>>> {
                return  myApiService.getData(owner)
            }

            override fun onFetchFailed() {
               repoListRateLimit.reset(owner)
            }

        }.asLiveData()
    }

}