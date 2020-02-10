package com.example.hsexercise.feature.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FeatureTableDao {
    @Query("SELECT * FROM feature")
    fun getAll(): LiveData<List<FeatureModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(models: List<FeatureModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(featureModel: FeatureModel)
}

@Entity(tableName = "feature")
data class FeatureModel(
    @PrimaryKey
    val id: String,
    val author: String,
    val download_url: String,
    val width: Int,
    val height: Int
)
