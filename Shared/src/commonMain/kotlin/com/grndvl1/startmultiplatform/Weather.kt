package com.grndvl1.startmultiplatform

import kotlinx.serialization.Serializable

@Serializable
data class Weather(val coord: Coordinate, val base: String)