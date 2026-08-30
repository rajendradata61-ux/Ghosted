package com.example.game

import android.app.Application
import android.content.Context
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import com.example.util.SoundManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class GameUiState(
    val phase: GamePhase = GamePhase.HOME,
    val roomCode: String = "WOLF",
    val players: List<Player> = emptyList(),
    val localPlayerName: String = "You",
    val localPlayerAvatar: String = "👾",
    val timerSecondsRemaining: Int = 10,
    val selectedOptionId: Int? = null,
    val resultData: GameResultData? = null
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    val soundManager = SoundManager(vibrator)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var startTimeMillis: Long = 0

    val callOptions = listOf(
        CallOption(
            id = 1,
            text = "\"Auntie my battery is at 1%, phone dying bye!!\"",
            emoji = "🪫",
            subtext = "Classic quick escape • 95% Success Rate",
            isSuccess = true
        ),
        CallOption(
            id = 2,
            text = "\"Entering an underground metro tunnel, hello? hello??\"",
            emoji = "🚇",
            subtext = "Static noise bluff • Safe escape",
            isSuccess = true
        ),
        CallOption(
            id = 3,
            text = "\"Accidentally picked up on speaker in middle of interview\"",
            emoji = "💀",
            subtext = "Worst possible blunder • Instant failure",
            isSuccess = false
        )
    )

    private val defaultBotNames = listOf(
        Pair("Rahul_fr", "🧢"),
        Pair("Anya_delulu", "✨"),
        Pair("Zaid_yaps", "🗣️")
    )

    init {
        initDefaultLocalPlayer()
    }

    private fun initDefaultLocalPlayer() {
        val local = Player(
            id = "local_1",
            name = "You",
            avatarEmoji = "👾",
            isLocal = true,
            isHost = true,
            isReady = true
        )
        _uiState.update {
            it.copy(
                players = listOf(local),
                phase = GamePhase.HOME
            )
        }
    }

    fun updateProfile(name: String, avatar: String) {
        soundManager.playPop()
        _uiState.update { state ->
            val updated = state.players.map { p ->
                if (p.isLocal) p.copy(name = name, avatarEmoji = avatar) else p
            }
            state.copy(
                localPlayerName = name,
                localPlayerAvatar = avatar,
                players = updated
            )
        }
    }

    fun createRoom() {
        soundManager.playSuccess()
        val code = listOf("WOLF", "RIZZ", "YAPP", "SKIB", "FLEX", "GOAT").random()
        val local = Player(
            id = "local_1",
            name = _uiState.value.localPlayerName,
            avatarEmoji = _uiState.value.localPlayerAvatar,
            isLocal = true,
            isHost = true,
            isReady = true
        )
        val defaultBots = defaultBotNames.take(2).mapIndexed { idx, (bName, bEmoji) ->
            Player(
                id = "bot_${idx + 1}",
                name = bName,
                avatarEmoji = bEmoji,
                isLocal = false,
                isHost = false,
                isReady = true
            )
        }

        _uiState.update {
            it.copy(
                phase = GamePhase.LOBBY,
                roomCode = code,
                players = listOf(local) + defaultBots,
                selectedOptionId = null,
                resultData = null
            )
        }
    }

    fun joinRoom(code: String) {
        soundManager.playSuccess()
        val room = if (code.isBlank()) "SQUAD" else code.uppercase().trim()
        val local = Player(
            id = "local_1",
            name = _uiState.value.localPlayerName,
            avatarEmoji = _uiState.value.localPlayerAvatar,
            isLocal = true,
            isHost = false,
            isReady = true
        )
        val defaultBots = defaultBotNames.mapIndexed { idx, (bName, bEmoji) ->
            Player(
                id = "bot_${idx + 1}",
                name = bName,
                avatarEmoji = bEmoji,
                isLocal = false,
                isHost = (idx == 0),
                isReady = true
            )
        }

        _uiState.update {
            it.copy(
                phase = GamePhase.LOBBY,
                roomCode = room,
                players = listOf(local) + defaultBots,
                selectedOptionId = null,
                resultData = null
            )
        }
    }

    fun addBotPlayer() {
        if (_uiState.value.players.size >= 5) return
        val currentSize = _uiState.value.players.size
        val newBot = Player(
            id = "bot_${System.currentTimeMillis() % 1000}",
            name = "Guest_${currentSize + 1}",
            avatarEmoji = listOf("🥑", "🚀", "👑", "🔥", "🐱", "👀").random(),
            isLocal = false,
            isReady = true
        )
        _uiState.update { it.copy(players = it.players + newBot) }
        soundManager.playPop()
    }

    fun removePlayer(id: String) {
        if (_uiState.value.players.size <= 2) return
        _uiState.update { state ->
            state.copy(players = state.players.filterNot { it.id == id })
        }
        soundManager.playPop()
    }

    fun startMiniGame() {
        soundManager.playBuzzer()
        startTimeMillis = System.currentTimeMillis()
        _uiState.update {
            it.copy(
                phase = GamePhase.GAMEPLAY,
                timerSecondsRemaining = 10,
                selectedOptionId = null,
                resultData = null
            )
        }

        startCountdownTimer()
    }

    private fun startCountdownTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var timeLeft = 10
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
                _uiState.update { it.copy(timerSecondsRemaining = timeLeft) }
                if (timeLeft <= 3) {
                    soundManager.playTick()
                }
            }
            // Timer expired without picking safe excuse -> Loss
            handleTimeout()
        }
    }

    fun selectExcuse(option: CallOption) {
        if (_uiState.value.phase != GamePhase.GAMEPLAY) return
        timerJob?.cancel()

        val timeTakenSeconds = ((System.currentTimeMillis() - startTimeMillis) / 1000).toInt().coerceIn(1, 10)

        if (option.isSuccess) {
            soundManager.playSuccess()
        } else {
            soundManager.playBuzzer()
        }

        val result = if (option.isSuccess) {
            GameResultData(
                isWin = true,
                title = "🎉 YOU DODGED THE CALL!",
                subtitle = "Smooth escape! You avoided a 45-minute interrogation about your salary & marriage plans.",
                chosenExcuse = option,
                scoreGained = 150 - (timeTakenSeconds * 10),
                timeTakenSeconds = timeTakenSeconds
            )
        } else {
            GameResultData(
                isWin = false,
                title = "💀 CAUGHT IN 4K! CRASH OUT!",
                subtitle = "Auntie heard everything on speaker! You are now trapped in a lecture for the next 1 hour.",
                chosenExcuse = option,
                scoreGained = 0,
                timeTakenSeconds = timeTakenSeconds
            )
        }

        _uiState.update {
            it.copy(
                phase = GamePhase.RESULT,
                selectedOptionId = option.id,
                resultData = result
            )
        }
    }

    private fun handleTimeout() {
        soundManager.playShutter()
        val result = GameResultData(
            isWin = false,
            title = "⏰ TIME OUT! CALL CONNECTED!",
            subtitle = "You froze in fear and didn't make up an excuse in time! Auntie started asking about your marks.",
            chosenExcuse = null,
            scoreGained = 0,
            timeTakenSeconds = 10
        )
        _uiState.update {
            it.copy(
                phase = GamePhase.RESULT,
                resultData = result
            )
        }
    }

    fun playAgain() {
        startMiniGame()
    }

    fun backToLobby() {
        soundManager.playPop()
        _uiState.update { it.copy(phase = GamePhase.LOBBY) }
    }

    fun backToHome() {
        soundManager.playPop()
        _uiState.update { it.copy(phase = GamePhase.HOME) }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
        timerJob?.cancel()
    }
}
