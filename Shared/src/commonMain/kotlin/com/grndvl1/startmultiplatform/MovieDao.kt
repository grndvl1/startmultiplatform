package com.grndvl1.startmultiplatform

import com.grndvl1.startmultiplatform.db.model.MovieModel

class MovieDao(database: AnyNameDatabase) {

    private val db = database.movieModelQueries

    internal fun insert(item: MovieData) {
        db.insertItem(
            title = item.Title,
            poster = item.Poster
        )
    }

    internal fun select():List<MovieModel> = db.selectAll().executeAsList()
}