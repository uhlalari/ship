package com.game.ship.data.repositoryimpl

import com.game.ship.domain.model.Enemy
import com.game.ship.domain.repository.GameRepository

class GameRepositoryImpl : GameRepository {
    override fun getRandomEnemy(screenWidth: Int): Enemy {
        val x = (0..(screenWidth - 100)).random().toFloat()
        return Enemy(
            x = x,
            y = 0f,
            speed = 5f,
            width = 120,
            height = 120
        )
    }
}
