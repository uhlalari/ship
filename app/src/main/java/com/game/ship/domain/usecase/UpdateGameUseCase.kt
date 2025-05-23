package com.game.ship.domain.usecase

import com.game.ship.domain.model.Bullet
import com.game.ship.domain.model.Enemy
import com.game.ship.domain.model.Ship

class UpdateGameUseCase {
    fun updateBullets(bullets: List<Bullet>): List<Bullet> {
        return bullets.map { it.copy(y = it.y - it.speed) }.filter { it.y > 0 }
    }

    fun updateEnemies(enemies: List<Enemy>): List<Enemy> {
        return enemies.map { it.copy(y = it.y + it.speed) }.filter { it.y < 2000 }
    }

    fun detectCollisions(
        bullets: List<Bullet>,
        enemies: List<Enemy>
    ): Pair<List<Bullet>, List<Enemy>> {
        val remainingBullets = bullets.toMutableList()
        val remainingEnemies = enemies.toMutableList()

        bullets.forEach { bullet ->
            val hit = enemies.find { enemy ->
                bullet.x in enemy.x..(enemy.x + enemy.width) &&
                        bullet.y in enemy.y..(enemy.y + enemy.height)
            }
            if (hit != null) {
                remainingBullets.remove(bullet)
                remainingEnemies.remove(hit)
            }
        }

        return Pair(remainingBullets, remainingEnemies)
    }

    fun hasEnemyReachedShip(enemies: List<Enemy>, ship: Ship): Boolean {
        return enemies.any { enemy ->
            val enemyBottom = enemy.y + enemy.height
            val shipTop = ship.y
            val shipBottom = ship.y + ship.height

            val overlapsHorizontally = enemy.x < (ship.x + ship.width) &&
                    (enemy.x + enemy.width) > ship.x

            val collidedWithShip = overlapsHorizontally && enemyBottom >= shipTop
            val passedShip = enemy.y > shipBottom

            collidedWithShip || passedShip
        }
    }
}
