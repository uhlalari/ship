package com.game.ship.presentation.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.game.ship.R
import com.game.ship.presentation.viewmodel.GameViewModel

class GameActivity : AppCompatActivity() {

    private lateinit var viewModel: GameViewModel
    private lateinit var shipView: ImageView
    private lateinit var bulletContainer: FrameLayout
    private lateinit var enemyContainer: FrameLayout
    private lateinit var scoreText: TextView
    private lateinit var restartButton: Button
    private lateinit var gameContainer: FrameLayout
    private lateinit var background1: ImageView
    private lateinit var background2: ImageView

    private val handler = Handler(Looper.getMainLooper())
    private var gameLoopStarted = false
    private var bgScroll = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        supportActionBar?.hide()

        setContentView(R.layout.activity_game)

        shipView = findViewById(R.id.player_ship)
        bulletContainer = findViewById(R.id.bullet_container)
        enemyContainer = findViewById(R.id.enemy_container)
        scoreText = findViewById(R.id.score_text)
        restartButton = findViewById(R.id.restart_button)
        gameContainer = findViewById(R.id.game_container)
        background1 = findViewById(R.id.background1)
        background2 = findViewById(R.id.background2)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        restartButton.setOnClickListener {
            bgScroll = 0f
            viewModel.restartGame()

            shipView.post {
                val shipY = (gameContainer.height - shipView.height).toFloat()
                shipView.y = shipY
                viewModel.updateShipY(shipY)
            }

            viewModel.updateGame()

            restartButton.visibility = View.GONE
        }

        gameContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!gameLoopStarted) {
                val shipY = (gameContainer.height - shipView.height).toFloat()
                shipView.y = shipY
                viewModel.updateShipY(shipY)
                startGameLoop()
                gameLoopStarted = true
            }
        }

        gameContainer.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val visualOffset = -40f
                    val bulletX = shipView.x + visualOffset + (shipView.width / 2f) - (80f / 2f)
                    val bulletY = shipView.y
                    viewModel.shootAt(bulletX, bulletY)

                    viewModel.moveShip(event.x)
                }

                MotionEvent.ACTION_MOVE -> {
                    viewModel.moveShip(event.x)
                }
            }
            true
        }

        viewModel.gameState.observe(this) { state ->
            shipView.x = state.ship.x
            scoreText.text = "Score: ${state.score}"

            bgScroll += 4f
            val bgHeight = background1.height.toFloat()
            background1.translationY = bgScroll % bgHeight
            background2.translationY = (bgScroll % bgHeight) - bgHeight

            bulletContainer.removeAllViews()
            state.bullets.forEach { bullet ->
                val bulletView = ImageView(this).apply {
                    setImageResource(R.drawable.bullet)
                    layoutParams = FrameLayout.LayoutParams(80, 160)
                    x = bullet.x
                    y = bullet.y
                }
                bulletContainer.addView(bulletView)
            }

            enemyContainer.removeAllViews()
            state.enemies.forEach { enemy ->
                val enemyView = ImageView(this).apply {
                    setImageResource(R.drawable.enemy)
                    layoutParams = FrameLayout.LayoutParams(enemy.width, enemy.height)
                    x = enemy.x
                    y = enemy.y
                    rotation = 180f
                }
                enemyContainer.addView(enemyView)
            }

            restartButton.visibility = if (state.isGameOver) View.VISIBLE else View.GONE
        }
    }

    private fun startGameLoop() {
        handler.post(object : Runnable {
            override fun run() {
                viewModel.updateGame()
                handler.postDelayed(this, 16L)
            }
        })
    }
}
