package com.appsian.aaproject.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.appsian.aaproject.db.daos.GitHubSearchResultsDao
import com.appsian.aaproject.db.entities.GitHubSearchResults

@Database(entities = [GitHubSearchResults::class], version = 1)
abstract class MyDatabase:RoomDatabase() {
abstract fun gitHubSearchResultsDao(): GitHubSearchResultsDao
    companion object {
        var instance: MyDatabase? = null
            private set


        fun getInstance(context: Context): MyDatabase {
            if (instance == null) {
                synchronized(MyDatabase::class) {
                    instance = Room.databaseBuilder(
                        context,
                        MyDatabase::class.java, "aa.db"
                    )
                        .build()
                }
            }

            return instance!!
        }

    }
}