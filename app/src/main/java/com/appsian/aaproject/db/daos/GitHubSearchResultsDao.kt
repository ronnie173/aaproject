package com.appsian.aaproject.db.daos

import androidx.room.Dao
import com.appsian.aaproject.db.BaseDao
import com.appsian.aaproject.db.entities.GitHubSearchResults

@Dao
abstract class GitHubSearchResultsDao:BaseDao<GitHubSearchResults>