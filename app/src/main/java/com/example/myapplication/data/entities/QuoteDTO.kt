package com.example.myapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteDTO(
    var slug: String,
    @PrimaryKey
    var sentence: String
)