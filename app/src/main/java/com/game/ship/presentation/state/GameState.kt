package com.game.ship.presentation.state

import com.game.ship.domain.model.Bullet
import com.game.ship.domain.model.Enemy
import com.game.ship.domain.model.Ship

data class GameState(
    val ship: Ship,
    val bullets: List<Bullet>,
    val enemies: List<Enemy>,
    val score: Int,
    val isGameOver: Boolean = false
)
