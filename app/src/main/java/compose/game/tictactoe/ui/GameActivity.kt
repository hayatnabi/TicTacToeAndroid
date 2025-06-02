package compose.game.tictactoe.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.game.tictactoe.R
import compose.game.tictactoe.ui.theme.TicTacToeComposeTheme
import compose.game.tictactoe.utils.MediaPlayerManager
import compose.game.tictactoe.utils.MyApp
import kotlinx.coroutines.delay

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeComposeTheme {
                TicTacToeGameScreen()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MediaPlayerManager.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerManager.release()
    }
}

@Preview
@Composable
fun TicTacToeGameScreen() {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var player1Score by remember { mutableIntStateOf(0) }
    var player2Score by remember { mutableIntStateOf(0) }
    var winnerMessage by remember { mutableStateOf<String?>(null) }

    // Store the winning line (the values) and also the positions of the winning cells for flicker
    var winningLinePositions by remember { mutableStateOf<List<Pair<Int, Int>>?>(null) }

    // Modified checkWinner returns the winner symbol and updates winningLinePositions
    fun checkWinner(): String? {
        val lines = listOf(
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)), // Row 0
            listOf(Pair(1, 0), Pair(1, 1), Pair(1, 2)), // Row 1
            listOf(Pair(2, 0), Pair(2, 1), Pair(2, 2)), // Row 2
            listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0)), // Col 0
            listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1)), // Col 1
            listOf(Pair(0, 2), Pair(1, 2), Pair(2, 2)), // Col 2
            listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)), // Diag
            listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))  // Diag
        )

        for (line in lines) {
            val values = line.map { (r, c) -> board[r][c] }
            if (values.all { it == "X" }) {
                winningLinePositions = line
                return "X"
            }
            if (values.all { it == "O" }) {
                winningLinePositions = line
                return "O"
            }
        }
        winningLinePositions = null
        return null
    }

    fun isBoardFull(): Boolean {
        return board.flatten().none { it.isEmpty() }
    }

    fun handleMove(row: Int, col: Int) {
        if (board[row][col].isNotEmpty() || winnerMessage != null) return

        board = board.toMutableList().apply {
            this[row] = this[row].toMutableList().apply {
                this[col] = currentPlayer
            }
        }

        if (currentPlayer == "X") {
            MediaPlayerManager.playSound(MyApp.context, R.raw.i_move)
        } else {
            MediaPlayerManager.playSound(MyApp.context, R.raw.p_move)
        }

        val winner = checkWinner()
        if (winner != null) {
            winnerMessage = "Player $winner wins!"
            MediaPlayerManager.playSound(MyApp.context, R.raw.win_sound)
            if (winner == "X") player1Score++ else player2Score++
        } else if (isBoardFull()) {
            winnerMessage = "It's a draw!"
            MediaPlayerManager.playSound(MyApp.context, R.raw.draw_sound)
        } else {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
        }
    }

    fun resetBoard() {
        board = List(3) { MutableList(3) { "" } }
        currentPlayer = "X"
        winnerMessage = null
        winningLinePositions = null
    }

    // Flicker logic: For the winning boxes, toggle visibility 3 times (flicker)
    // We'll use a Boolean state that toggles every 300ms, 6 times (3 flickers = on-off-on-off-on-off)
    val flickerVisible = remember { mutableStateOf(true) }
    LaunchedEffect(winningLinePositions) {
        if (winningLinePositions != null) {
            repeat(6) {
                flickerVisible.value = !flickerVisible.value
                kotlinx.coroutines.delay(100)
            }
            flickerVisible.value = true // Ensure visible at the end
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // light green
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Scoreboard
            Text("Scoreboard", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Player X: $player1Score",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50)) // Green
                    .padding(8.dp),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Player O: $player2Score",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2196F3)) // Blue
                    .padding(8.dp),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            if (winnerMessage?.isNotEmpty() == true) {
                Text(
                    text = winnerMessage.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        // Game board centered vertically and horizontally
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                for (row in 0..2) {
                    Row {
                        for (col in 0..2) {
                            val isWinningBox = winningLinePositions?.contains(Pair(row, col)) == true
                            val backgroundColor = when {
                                isWinningBox && !flickerVisible.value -> Color.Transparent
                                board[row][col] == "X" -> Color(0xFFC8E6C9) // Green
                                board[row][col] == "O" -> Color(0xFFBBDEFB) // Blue
                                else -> Color.Transparent
                            }

                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .drawBehind {
                                        val strokeWidth = 0.7.dp.toPx()
                                        val gray = Color.Gray

                                        // Right border
                                        if (col < 2) {
                                            drawLine(
                                                color = gray,
                                                start = Offset(size.width, 0f),
                                                end = Offset(size.width, size.height),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                        // Bottom border
                                        if (row < 2) {
                                            drawLine(
                                                color = gray,
                                                start = Offset(0f, size.height),
                                                end = Offset(size.width, size.height),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                    }
                                    .clickable { handleMove(row, col) },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(3.dp)
                                        .background(backgroundColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        board[row][col],
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Reset Button at Bottom Center
        Button(
            onClick = { resetBoard() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336), // Moderate red
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
            Text("Reset Game", fontWeight = FontWeight.Bold)
        }
    }
}
