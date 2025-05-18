package com.example.nutritrack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nutritrack.model.NutriCoachTips

@Dao
interface NutriCoachTipsDao {
    @Insert
    suspend fun insertTip(message: NutriCoachTips)

    @Query("SELECT * FROM nutri_coach_tips")
    suspend fun getAllTips(): List<NutriCoachTips>
}
