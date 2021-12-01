package com.ilazar.myapp2.todo.data.remote

import com.ilazar.myapp2.core.Api
import com.ilazar.myapp2.todo.data.Bike
import retrofit2.http.*

object BikeApi {
    interface Service {
        @GET("/api/bike")
        suspend fun find(): List<Bike>

        @GET("/api/bike/{id}")
        suspend fun read(@Path("id") bikeId: String): Bike;

        @Headers("Content-Type: application/json")
        @POST("/api/bike")
        suspend fun create(@Body bike: Bike): Bike

        @Headers("Content-Type: application/json")
        @PUT("/api/bike/{id}")
        suspend fun update(@Path("id") bikeId: String, @Body bike: Bike): Bike
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}