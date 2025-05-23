package com.game.ship.domain.usecase

import com.game.ship.domain.model.Enemy
import com.game.ship.domain.repository.GameRepository

class GenerateEnemyUseCase(private val repository: GameRepository) {
    fun execute(screenWidth: Int): Enemy {
        return repository.getRandomEnemy(screenWidth)
    }
}
