package com.grndvl1.startmultiplatform

class MovieRepository(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) {

    suspend fun fetchMovie(): MovieData {
        val result = movieApi.fetchMovie()
        movieDao.insert(result)
        return result
    }

    fun selectFromDb() = movieDao.select()
}