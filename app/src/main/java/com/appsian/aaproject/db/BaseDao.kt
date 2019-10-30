package com.appsian.aaproject.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dataItem: T): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(dataItem: T): Int

    @Delete
    fun delete(dataItem: T)
}