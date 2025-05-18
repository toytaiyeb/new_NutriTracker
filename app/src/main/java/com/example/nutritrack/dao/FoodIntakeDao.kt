package com.example.nutritrack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritrack.model.FoodIntake

@Dao
interface FoodIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intake WHERE userId = :userId")
    suspend fun getFoodIntakeByUserId(userId: String): List<FoodIntake>

    @Query("DELETE FROM food_intake WHERE userId = :userId")
    suspend fun deleteFoodIntakeByUserId(userId: String)
}
