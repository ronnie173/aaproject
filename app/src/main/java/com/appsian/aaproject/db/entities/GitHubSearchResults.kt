package com.appsian.aaproject.db.entities

import androidx.room.Entity
import com.appsian.aaproject.dataclasses.Item

@Entity
data class GitHubSearchResults(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)