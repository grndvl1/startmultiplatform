package com.grndvl1.startmultiplatform

import com.grndvl1.startmultiplatform.db.model.MovieModel
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver

fun createDatabase(driver: SqlDriver): AnyNameDatabase {
    val coordinateAdapter = object : ColumnAdapter<String, String> {
        override fun decode(databaseValue: String): String {
            return databaseValue
        }

        override fun encode(value: String): String {
            return value
        }
    }

    return AnyNameDatabase(driver)
}
