package com.example.myapplication.data.remote

import com.example.myapplication.data.entities.GOTCharacter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {
    @GET("Characters")
    suspend fun getAllCharacters() : Response<List<GOTCharacter>>

    @GET("Characters/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<GOTCharacter>
}