package com.example.myapplication.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.entities.CharacterDTO
import com.example.myapplication.data.entities.QuoteDTO

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    fun getAllCharacters() : LiveData<List<CharacterDTO>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacter(id: Int): LiveData<CharacterDTO>

    @Query("SELECT sentence FROM quotes WHERE slug = :slug")
    fun getAllQuotes(slug: String): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCharacters(characters: List<CharacterDTO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuotes(characters: List<QuoteDTO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(character: QuoteDTO)


}