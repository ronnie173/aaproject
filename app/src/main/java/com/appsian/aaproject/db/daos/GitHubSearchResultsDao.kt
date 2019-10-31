package com.appsian.aaproject.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsian.aaproject.db.entities.Repo

@Dao
abstract class GitHubSearchResultsDao{
    @Query("SELECT COUNT(*) FROM Repo" )
    abstract fun count(): Int

    @Query("SELECT * FROM Repo")
    abstract fun loadRepos():LiveData<List<Repo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRepos(repositories: List<Repo>)



}