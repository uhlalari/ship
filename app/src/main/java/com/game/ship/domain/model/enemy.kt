package com.game.ship.domain.model

data class Enemy(
    var x: Float,
    var y: Float,
    val speed: Float,
    val width: Int,
    val height: Int
)
