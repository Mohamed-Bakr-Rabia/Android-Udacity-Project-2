package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlin.collections.ArrayList


@Dao
interface AsteroidDatabaseDao {


    @Query("SELECT * FROM asteroid_table Where close_approach_date >= :currentDay ORDER BY close_approach_date ASC")
    fun getAllAsteroid(currentDay:String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list:ArrayList<Asteroid>)

    @Query("SELECT * FROM  asteroid_table WHERE id = :id")
    suspend fun get(id: Long):Asteroid



}