package com.udacity.asteroidradar.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "asteroid_table")
data class Asteroid(
    @PrimaryKey val id: Long,

    @ColumnInfo(name = "codename") val codename: String,

    @ColumnInfo(name = "close_approach_date") val closeApproachDate :String,

    @ColumnInfo(name = "absolute_magnitude_h") val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter_max") val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_velocity") val relativeVelocity: Double,

    @ColumnInfo(name = "miss_distance") val distanceFromEarth: Double,

    @ColumnInfo(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean
) : Parcelable
