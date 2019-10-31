package com.appsian.aaproject.db

import android.content.Context
import androidx.room.*
import com.appsian.aaproject.db.daos.GitHubSearchResultsDao
import com.appsian.aaproject.db.entities.Repo
import com.appsian.aaproject.db.entities.User

@Database(entities = [Repo::class, User::class], version = 1,exportSchema = false)
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