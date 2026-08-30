package com.example.model

enum class GameRole(val displayName: String, val title: String, val mission: String) {
    NORMIE(
        displayName = "NORMIE",
        title = "The Innocent Survivor",
        mission = "Stay calm, pick the sensible excuse, and dodge the awkward relative without crashing out!"
    ),
    INSTIGATOR(
        displayName = "INSTIGATOR",
        title = "The Chaos Agent",
        mission = "Pick the most unhinged excuse, disrupt the call, and see if the squad catches you in 4K!"
    )
}

enum class GamePhase {
    HOME,
    LOBBY,
    GAMEPLAY,
    RESULT
}

data class CallOption(
    val id: Int,
    val text: String,
    val emoji: String,
    val subtext: String,
    val isSuccess: Boolean
)

data class Player(
    val id: String,
    val name: String,
    val avatarEmoji: String,
    val isLocal: Boolean = false,
    val isHost: Boolean = false,
    val isReady: Boolean = false,
    val role: GameRole = GameRole.NORMIE,
    val score: Int = 0,
    val selectedExcuse: CallOption? = null,
    val isWinner: Boolean = false
)

data class GameResultData(
    val isWin: Boolean,
    val title: String,
    val subtitle: String,
    val chosenExcuse: CallOption?,
    val scoreGained: Int,
    val timeTakenSeconds: Int
)

