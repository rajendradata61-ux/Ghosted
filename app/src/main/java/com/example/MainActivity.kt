package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.game.GameUiState
import com.example.game.GameViewModel
import com.example.model.GamePhase
import com.example.ui.screens.GameResultScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LobbyScreen
import com.example.ui.screens.PhoneCallScenarioScreen
import com.example.ui.theme.DarkBg
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val gameViewModel: GameViewModel = viewModel()
                val uiState by gameViewModel.uiState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DarkBg,
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(DarkBg)
                    ) {
                        GameContentRouter(
                            uiState = uiState,
                            viewModel = gameViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameContentRouter(
    uiState: GameUiState,
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    when (uiState.phase) {
        GamePhase.HOME -> {
            HomeScreen(
                playerName = uiState.localPlayerName,
                playerAvatar = uiState.localPlayerAvatar,
                onUpdateProfile = { name, avatar -> viewModel.updateProfile(name, avatar) },
                onCreateRoom = { viewModel.createRoom() },
                onJoinRoom = { code -> viewModel.joinRoom(code) },
                modifier = modifier
            )
        }
        GamePhase.LOBBY -> {
            LobbyScreen(
                uiState = uiState,
                onUpdateProfile = { name, avatar -> viewModel.updateProfile(name, avatar) },
                onAddBot = { viewModel.addBotPlayer() },
                onRemovePlayer = { id -> viewModel.removePlayer(id) },
                onStartGame = { viewModel.startMiniGame() },
                onBackToHome = { viewModel.backToHome() },
                modifier = modifier
            )
        }
        GamePhase.GAMEPLAY -> {
            PhoneCallScenarioScreen(
                timerSecondsRemaining = uiState.timerSecondsRemaining,
                options = viewModel.callOptions,
                selectedOptionId = uiState.selectedOptionId,
                onSelectOption = { option -> viewModel.selectExcuse(option) },
                modifier = modifier
            )
        }
        GamePhase.RESULT -> {
            GameResultScreen(
                resultData = uiState.resultData,
                onPlayAgain = { viewModel.playAgain() },
                onBackToLobby = { viewModel.backToLobby() },
                onBackToHome = { viewModel.backToHome() },
                modifier = modifier
            )
        }
    }
}

