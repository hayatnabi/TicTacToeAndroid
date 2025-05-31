package compose.game.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.game.tictactoe.ui.theme.TicTacToeComposeTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeWidget(
                        name = "Tic Tac Toe",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeWidget(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background animation
        AnimatedEmojiBackground()

        // Foreground content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            Text(
                text = "Welcome to $name!",
                color = Color.Black,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight(500),
                    fontSize = 24.sp // Increased font size
                )
            )

            Spacer(modifier = Modifier.padding(top = 32.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00A86B), // Jade Green
                    contentColor = Color.White
                ),
                onClick = { context.startActivity(Intent(context, GameActivity::class.java)) }
            ) {
                Text(text = "Start Game")
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3), // Jade Green
                    contentColor = Color.White
                ),
                onClick = { context.startActivity(Intent(context, SettingsActivity::class.java)) }
            ) {
                Text(text = "   Settings  ")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicTacToeComposeTheme {
        WelcomeWidget("Tic Tac Toe")
    }
}

@Composable
fun AnimatedEmojiBackground() {
    val emojis = listOf("X", "O", "\uD83D\uDCA5 ", "🛦", "\uD83D\uDCA5\uD83D\uDE80", "☕")
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current

    val emojiItems = remember {
        emojis.map {
            val x = Random.nextFloat()
            val y = Animatable(initialValue = Random.nextFloat())
            EmojiItem(it, x, y)
        }
    }

    LaunchedEffect(Unit) {
        emojiItems.forEach { item ->
            launch {
                while (true) {
                    item.y.snapTo(1f)
                    item.y.animateTo(
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 7000 + Random.nextInt(3000), easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        emojiItems.forEach { item ->
            val xPx = item.x * size.width
            val yPx = item.y.value * size.height
            drawContext.canvas.nativeCanvas.drawText(
                item.emoji,
                xPx,
                yPx,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 50f
                    isAntiAlias = true
                    alpha = 60  // 0 (fully transparent) to 255 (fully opaque)
                }
            )
        }
    }
}

data class EmojiItem(
    val emoji: String,
    val x: Float, // x position (as ratio of screen width)
    val y: Animatable<Float, AnimationVector1D> // y position (0f to 1f, animated)
)



