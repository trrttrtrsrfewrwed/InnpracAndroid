package com.example.myapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "characters")
data class CharacterDTO(
    @PrimaryKey
    val id: Int, // A unique number that identifies this character.

    val fullName: String?, // The First + Last name of the character.

    val title: String?, // The character's formal title.

    val slug: String?,

    val imageUrl: String?
)