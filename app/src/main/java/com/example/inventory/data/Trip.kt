package com.example.inventory.data

import java.io.Serializable

data class Trip(
    val userId: String = "",
    var title: String = "",
    var description: String = "",
    val timestamp: Long = 0L,
    val positions: List<GeoPosition> = emptyList()
) : Serializable

data class GeoPosition(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timestamp: Long = 0L
)
