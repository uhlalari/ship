package com.game.ship.domain.usecase

import com.game.ship.domain.model.Ship

class MoveShipUseCase {
    fun execute(ship: Ship, touchX: Float): Ship {
        return ship.copy(x = touchX - ship.width / 2)
    }
}
