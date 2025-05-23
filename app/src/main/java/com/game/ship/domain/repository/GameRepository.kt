package com.game.ship.domain.repository

import com.game.ship.domain.model.Enemy

interface GameRepository {
    fun getRandomEnemy(screenWidth: Int): Enemy
}
