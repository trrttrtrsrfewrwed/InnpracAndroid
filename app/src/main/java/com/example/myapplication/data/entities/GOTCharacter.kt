package com.example.myapplication.data.entities

data class GOTCharacter(
    val id: Int, // A unique number that identifies this character.

    val firstName: String?, // The character's first name.

    val lastName: String?, // The character's last name.

    val fullName: String?, // The First + Last name of the character.

    val title: String?, // The character's formal title.

    val family: String?, // The character's family name.

    val image: String?,

    val imageUrl: String?
)