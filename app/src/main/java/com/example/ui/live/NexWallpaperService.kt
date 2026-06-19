package com.example.ui.live

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.os.Handler
import android.os.Looper
import kotlin.random.Random

class NexWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return LiveCanvasEngine()
    }

    inner class LiveCanvasEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var visible = false

        // Drawing properties
        private var type = "particles" // default
        private val paint = Paint().apply { isAntiAlias = true }
        
        // Matrix Rain specific state
        private var columns = 0
        private lateinit var letterPos: IntArray
        private val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ@#$&".toCharArray()

        // Particles specific state
        private val particles = mutableListOf<Particle>()

        // Liquid Flow wave offset
        private var waveOffset = 0f

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                if (visible) {
                    handler.postDelayed(this, 30) // ~33 FPS
                }
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            // Load current user selection from static state or SharedPreferences
            val prefs = getSharedPreferences("NexWallPrefs", MODE_PRIVATE)
            type = prefs.getString("current_live_type", "particles") ?: "particles"
            setupAnimations()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                val prefs = getSharedPreferences("NexWallPrefs", MODE_PRIVATE)
                type = prefs.getString("current_live_type", "particles") ?: "particles"
                setupAnimations()
                handler.post(drawRunnable)
            } else {
                handler.removeCallbacks(drawRunnable)
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            setupAnimations()
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(drawRunnable)
        }

        private fun setupAnimations() {
            val width = desiredMinimumWidth.coerceAtLeast(1080)
            val height = desiredMinimumHeight.coerceAtLeast(1920)

            // Setup Matrix Columns
            val fontSize = 42
            columns = (width / fontSize).coerceAtLeast(10)
            letterPos = IntArray(columns) { Random.nextInt(0, height / fontSize) }

            // Setup Particles
            particles.clear()
            for (i in 0..70) {
                particles.add(
                    Particle(
                        x = Random.nextFloat() * width,
                        y = Random.nextFloat() * height,
                        vx = (Random.nextFloat() * 4) - 2f,
                        vy = (Random.nextFloat() * 4) - 2f,
                        radius = Random.nextFloat() * 10f + 4f,
                        color = when(Random.nextInt(3)) {
                            0 -> Color.rgb(0, 255, 200) // Neon Teal
                            1 -> Color.rgb(255, 0, 128) // Neon Pink
                            else -> Color.rgb(138, 43, 226) // Deep Violet
                        }
                    )
                )
            }
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    val prefs = getSharedPreferences("NexWallPrefs", MODE_PRIVATE)
                    type = prefs.getString("current_live_type", "particles") ?: "particles"
                    
                    when (type) {
                        "matrix" -> drawMatrix(canvas)
                        "particles" -> drawParticles(canvas)
                        "neon_glow", "liquid_flow" -> drawWaves(canvas)
                        else -> drawParticles(canvas)
                    }
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        // 1. Render Matrix Code Rain
        private fun drawMatrix(canvas: Canvas) {
            val w = canvas.width
            val h = canvas.height
            
            // Semi-transparent black overlay for fade trail effect
            paint.style = Paint.Style.FILL
            paint.color = Color.argb(35, 0, 5, 0) // highly dark greenish black
            canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)

            paint.textSize = 42f
            paint.textAlign = Paint.Align.CENTER

            for (i in 0 until columns) {
                // Randomly change characters
                val text = chars[Random.nextInt(chars.size)].toString()
                val x = i * 42f + 21f
                val y = letterPos[i] * 42f

                // Lead character is glowing white, others are emerald green
                if (Random.nextInt(10) == 0) {
                    paint.color = Color.WHITE
                    paint.setShadowLayer(10f, 0f, 0f, Color.GREEN)
                } else {
                    paint.color = Color.rgb(0, 255, 70)
                    paint.clearShadowLayer()
                }

                canvas.drawText(text, x, y, paint)

                // Update column positions
                if (y > h && Random.nextFloat() > 0.98f) {
                    letterPos[i] = 0
                } else {
                    letterPos[i]++
                }
            }
        }

        // 2. Render Radiant Floating Cosmic Particles
        private fun drawParticles(canvas: Canvas) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()

            // Pure AMOLED deep space background
            paint.style = Paint.Style.FILL
            paint.color = Color.BLACK
            canvas.drawRect(0f, 0f, w, h, paint)

            // Draw glowing connections
            paint.strokeWidth = 1.5f
            paint.style = Paint.Style.STROKE
            for (i in 0 until particles.size) {
                val p1 = particles[i]
                for (j in i + 1 until particles.size) {
                    val p2 = particles[j]
                    val dx = p1.x - p2.x
                    val dy = p1.y - p2.y
                    val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                    if (dist < 180f) {
                        val alpha = (255 - (dist / 180f * 255)).toInt().coerceIn(0, 200)
                        paint.color = Color.argb(alpha, 80, 120, 255)
                        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
                    }
                }
            }

            // Draw floating particles
            paint.style = Paint.Style.FILL
            for (p in particles) {
                paint.color = p.color
                paint.setShadowLayer(12f, 0f, 0f, p.color)
                canvas.drawCircle(p.x, p.y, p.radius, paint)

                // Move particles
                p.x += p.vx
                p.y += p.vy

                // Boundary bounce
                if (p.x < 0 || p.x > w) p.vx *= -1f
                if (p.y < 0 || p.y > h) p.vy *= -1f

                p.x = p.x.coerceIn(0f, w)
                p.y = p.y.coerceIn(0f, h)
            }
            paint.clearShadowLayer()
        }

        // 3. Render Cyberpunk Waves / Liquid Flows
        private fun drawWaves(canvas: Canvas) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()

            // Deep background
            paint.style = Paint.Style.FILL
            paint.color = Color.rgb(10, 10, 20)
            canvas.drawRect(0f, 0f, w, h, paint)

            waveOffset += 0.05f

            // Draw multiple overlay flowing gradients
            paint.style = Paint.Style.FILL
            
            // Draw Wave 1 (Deep Purple)
            paint.color = Color.argb(70, 138, 43, 226)
            drawSineWave(canvas, h * 0.4f, 150f, 0.005f, waveOffset)

            // Draw Wave 2 (Neon Pink)
            paint.color = Color.argb(60, 255, 0, 128)
            drawSineWave(canvas, h * 0.5f, 200f, 0.003f, -waveOffset * 1.2f)

            // Draw Wave 3 (Neon Cyan)
            paint.color = Color.argb(50, 0, 255, 230)
            drawSineWave(canvas, h * 0.62f, 220f, 0.004f, waveOffset * 0.8f)
        }

        private fun drawSineWave(canvas: Canvas, baseY: Float, amplitude: Float, frequency: Float, offset: Float) {
            val w = canvas.width.toFloat()
            val path = android.graphics.Path()
            path.moveTo(0f, canvas.height.toFloat())
            path.lineTo(0f, baseY)

            var x = 0f
            while (x <= w) {
                val y = baseY + amplitude * kotlin.math.sin(x * frequency + offset)
                path.lineTo(x, y)
                x += 10f
            }
            path.lineTo(w, canvas.height.toFloat())
            path.close()
            canvas.drawPath(path, paint)
        }
    }

    data class Particle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        val radius: Float,
        val color: Int
    )
}
