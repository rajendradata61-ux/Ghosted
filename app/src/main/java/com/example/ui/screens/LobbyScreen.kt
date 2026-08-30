package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.GameUiState
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonCard
import com.example.ui.components.PlayerAvatar
import com.example.ui.theme.*

@Composable
fun LobbyScreen(
    uiState: GameUiState,
    onUpdateProfile: (String, String) -> Unit,
    onAddBot: () -> Unit,
    onRemovePlayer: (String) -> Unit,
    onStartGame: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showProfileDialog by remember { mutableStateOf(false) }
    val localPlayer = uiState.players.find { it.isLocal } ?: uiState.players.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackToHome) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextSecondary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SQUAD LOBBY ",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "🎮",
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Room Code Display Card
            NeonCard(
                borderColor = ElectricCyan,
                backgroundColor = DarkSurfaceVariant
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ROOM CODE",
                            color = TextTertiary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = uiState.roomCode,
                            color = ElectricCyan,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp
                        )
                    }

                    // Share Button
                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "🔥 Join my Caught in 4K match! Room Code: [${uiState.roomCode}]. Don't crash out!"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Invite Squad via"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ChipBg),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ElectricCyan),
                        modifier = Modifier.testTag("invite_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = ElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "INVITE",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Squad Lobby Roster Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PLAYERS IN LOBBY (${uiState.players.size})",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (uiState.players.size < 5) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, NeonLime.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable { onAddBot() }
                                .testTag("add_bot_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Player", tint = NeonLime, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+ BOT", color = NeonLime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkSurfaceVariant,
                        border = BorderStroke(1.dp, ElectricPurple.copy(alpha = 0.5f)),
                        modifier = Modifier.clickable { showProfileDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Edit Profile", tint = ElectricPurple, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("EDIT VIBE", color = ElectricPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Player Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.players) { player ->
                    PlayerAvatar(
                        player = player,
                        showScore = false,
                        onClick = {
                            if (!player.isLocal && uiState.players.size > 2) {
                                onRemovePlayer(player.id)
                            } else if (player.isLocal) {
                                showProfileDialog = true
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scenario Preview
            NeonCard(
                borderColor = WarningGold,
                backgroundColor = Color(0xFF1E1535)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📞", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "NEXT SCENARIO:",
                            color = WarningGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Dodging Awkward Relative's Call",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "10s Panic Timer • Pick safe excuse",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Bottom CTA: Start Game
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NeonButton(
                text = "START MINI-GAME ⚡",
                onClick = onStartGame,
                accentColor = HotCoral,
                textColor = Color.White,
                modifier = Modifier.testTag("start_mini_game_button")
            )
        }
    }

    // Profile Customizer Dialog
    if (showProfileDialog) {
        ProfileCustomizerDialog(
            currentName = localPlayer.name,
            currentAvatar = localPlayer.avatarEmoji,
            onDismiss = { showProfileDialog = false },
            onSave = { name, avatar ->
                onUpdateProfile(name, avatar)
                showProfileDialog = false
            }
        )
    }
}


@Composable
fun ProfileCustomizerDialog(
    currentName: String,
    currentAvatar: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var selectedAvatar by remember { mutableStateOf(currentAvatar) }
    val avatarOptions = listOf("👾", "🧢", "💀", "💅", "🗣️", "⚡", "🥑", "🚀", "👑", "🔥", "🐱", "👀")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceVariant,
        title = {
            Text(
                text = "Customize Your Vibe",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.take(15) },
                    label = { Text("Display Name / Tag") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonLime,
                        unfocusedBorderColor = DarkPurpleBorder
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Pick Your Avatar Emoji:",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(140.dp)
                ) {
                    items(avatarOptions) { emoji ->
                        val isSelected = (selectedAvatar == emoji)
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) NeonLime.copy(alpha = 0.3f) else DarkSurface)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) NeonLime else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedAvatar = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 22.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name.ifBlank { "Player" }, selectedAvatar) }
            ) {
                Text("SAVE VIBE", color = NeonLime, fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = TextTertiary)
            }
        }
    )
}
