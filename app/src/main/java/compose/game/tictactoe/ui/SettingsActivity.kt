package compose.game.tictactoe.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.game.tictactoe.ui.theme.JadeGreen
import compose.game.tictactoe.ui.theme.TicTacToeComposeTheme
import compose.game.tictactoe.utils.PreferencesManager

class SettingsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Settings", color = Color.White) },
                            navigationIcon = {
                                val context = LocalContext.current
                                IconButton(onClick = {
                                    (context as? Activity)?.finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.mediumTopAppBarColors(
                                containerColor = JadeGreen, // background color here
                                titleContentColor = Color.White,    // Title text color
                                navigationIconContentColor = Color.White // Back icon color
                            )
                        )
                    }
                ) { innerPadding ->
                    SettingsWidget(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsWidget(modifier: Modifier = Modifier) {
    var isDarkMode by rememberSaveable { mutableStateOf(false) }
    var isPlayWithPI by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SoundToggleRow()

        // Dark Mode Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Dark Mode", fontSize = 18.sp)
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )
        }

        // Play with "P" and "I" Switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Play with \"P\" and \"I\"", fontSize = 18.sp)
            Switch(
                checked = isPlayWithPI,
                onCheckedChange = { isPlayWithPI = it }
            )
        }
    }
}

@Composable
fun SoundToggleRow() {
    val context = LocalContext.current
    var shouldPlaySound by rememberSaveable {
        mutableStateOf(
            PreferencesManager.get(context, "should_play_sound", true)
        )
    }

    LaunchedEffect(shouldPlaySound) {
        PreferencesManager.save(context, "should_play_sound", shouldPlaySound)
    }

    // Play Sound
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Sound", fontSize = 18.sp)
        Switch(
            checked = shouldPlaySound,
            onCheckedChange = {
                shouldPlaySound = it
                PreferencesManager.save(context, "should_play_sound", it)
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    TicTacToeComposeTheme {
        SettingsWidget(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}
