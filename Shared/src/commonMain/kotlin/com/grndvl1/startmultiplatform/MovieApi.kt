package com.grndvl1.startmultiplatform

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON

class MovieApi(private val engine: HttpClientEngine) {

    private val client by lazy {
        HttpClient(engine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(JSON.nonstrict)
            }
        }
    }

    suspend fun fetchMovie(): MovieData {
        return client.get {
            url("$baseUrl&t=Blade%20runner")
        }
    }

    companion object {
        private const val baseUrl = "http://www.omdbapi.com/?apikey=c10bbbce"
    }
}