package com.laivinieks.cogimatch.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("arcade_score_table")
data class ArcadeScore(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val totalTurn:Int,
    val totalMatch:Int,
    val score:Int
)
