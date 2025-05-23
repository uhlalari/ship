package com.game.ship.domain.usecase

import com.game.ship.domain.model.Bullet
import com.game.ship.domain.model.Ship

class ShootBulletUseCase {
    fun execute(ship: Ship): Bullet {
        val bulletWidth = 80f
        return Bullet(
            x = ship.x + (ship.width / 2f) - (bulletWidth / 2f),
            y = ship.y,
            speed = 15f
        )
    }
}

