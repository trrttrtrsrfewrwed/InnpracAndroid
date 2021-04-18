package com.example.myapplication.data.remote

import com.example.myapplication.data.entities.AllQuotes
import com.example.myapplication.data.entities.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuotesService {

    @GET("v1/author/{slug}")
    suspend fun getQuote(@Path("slug") slug: String): Response<Quote>

    @GET("v1/character/{slug}")
    suspend fun getAllQuotes(@Path("slug") slug: String): Response<List<AllQuotes>>
}