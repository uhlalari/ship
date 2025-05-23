package com.game.ship.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.game.ship.data.repositoryimpl.GameRepositoryImpl
import com.game.ship.domain.model.Bullet
import com.game.ship.domain.model.Ship
import com.game.ship.domain.usecase.GenerateEnemyUseCase
import com.game.ship.domain.usecase.ShootBulletUseCase
import com.game.ship.domain.usecase.UpdateGameUseCase
import com.game.ship.presentation.state.GameState

class GameViewModel : ViewModel() {

    private val gameRepository = GameRepositoryImpl()
    private val shootBulletUseCase = ShootBulletUseCase()
    private val generateEnemyUseCase = GenerateEnemyUseCase(gameRepository)
    private val updateGameUseCase = UpdateGameUseCase()

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    init {
        restartGame()
    }

    private fun lerp(start: Float, end: Float, factor: Float): Float {
        return start + (end - start) * factor
    }

    fun moveShip(touchX: Float) {
        _gameState.value?.let { state ->
            if (!state.isGameOver) {
                val currentX = state.ship.x
                val targetX = touchX - state.ship.width / 2
                val newX = lerp(currentX, targetX, 0.15f)
                _gameState.value = state.copy(ship = state.ship.copy(x = newX))
            }
        }
    }

    fun shoot() {
        _gameState.value?.let { state ->
            if (!state.isGameOver) {
                val newBullet = shootBulletUseCase.execute(state.ship)
                val updatedBullets = state.bullets.toMutableList().apply { add(newBullet) }
                _gameState.value = state.copy(bullets = updatedBullets)
            }
        }
    }
    fun shootAt(x: Float, y: Float) {
        _gameState.value?.let { state ->
            if (!state.isGameOver) {
                val bullet = Bullet(x = x, y = y, speed = 15f)
                val updated = state.bullets.toMutableList().apply { add(bullet) }
                _gameState.value = state.copy(bullets = updated)
            }
        }
    }

    fun updateShipY(y: Float) {
        _gameState.value?.let { state ->
            _gameState.value = state.copy(ship = state.ship.copy(y = y))
        }
    }

    fun updateGame() {
        _gameState.value?.let { state ->
            if (state.isGameOver) return

            val updatedBullets = updateGameUseCase.updateBullets(state.bullets)
            val updatedEnemies = updateGameUseCase.updateEnemies(state.enemies)

            val (remainingBullets, remainingEnemies) =
                updateGameUseCase.detectCollisions(updatedBullets, updatedEnemies)

            val gameOver = updateGameUseCase.hasEnemyReachedShip(remainingEnemies, state.ship)

            val newEnemies = if (!gameOver && (0..100).random() > 97) {
                listOf(generateEnemyUseCase.execute(1080))
            } else emptyList()

            _gameState.value = state.copy(
                bullets = remainingBullets.toList(),
                enemies = if (gameOver) remainingEnemies.toList()
                else (remainingEnemies + newEnemies).toList(),
                score = state.score + (state.enemies.size - remainingEnemies.size),
                isGameOver = gameOver
            )
        }
    }

    fun restartGame() {
        _gameState.value = GameState(
            ship = Ship(300f, 0f, 100, 100),
            bullets = emptyList(),
            enemies = emptyList(),
            score = 0,
            isGameOver = false
        )
    }
}
