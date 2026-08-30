package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    playerName: String,
    playerAvatar: String,
    onUpdateProfile: (String, String) -> Unit,
    onCreateRoom: () -> Unit,
    onJoinRoom: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showJoinDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var joinCodeInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // App Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CAUGHT IN ",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "4K 📸",
                    color = NeonLime,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Social Dilemma & Chaos Mini-Games",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Player Profile Card
            NeonCard(
                borderColor = ElectricPurple,
                backgroundColor = DarkSurfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showProfileDialog = true }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF261245))
                                .border(2.dp, NeonLime, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(playerAvatar, fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = playerName,
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap to customize vibe",
                                color = ElectricCyan,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = { showProfileDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Vibe",
                            tint = NeonLime
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Feature Highlights Box
            NeonCard(
                borderColor = DarkPurpleBorder,
                backgroundColor = DarkSurface
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "⚡ QUICK PLAY PROTOTYPE",
                        color = WarningGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Scenario: Dodging Awkward Relative's Call 📞\n• 10-Second Panic Countdown ⏱️\n• 3 Hilarious Excuses (Safe vs Disaster)",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Action Buttons: Create Room & Join Room
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NeonButton(
                text = "CREATE ROOM 🚀",
                onClick = onCreateRoom,
                accentColor = NeonLime,
                textColor = Color(0xFF0C071E),
                modifier = Modifier.testTag("create_room_button")
            )

            NeonButton(
                text = "JOIN ROOM 🔑",
                onClick = { showJoinDialog = true },
                accentColor = ElectricCyan,
                textColor = Color(0xFF0C071E),
                modifier = Modifier.testTag("join_room_button")
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    // Join Room Dialog
    if (showJoinDialog) {
        AlertDialog(
            onDismissRequest = { showJoinDialog = false },
            containerColor = DarkSurfaceVariant,
            title = {
                Text(
                    text = "Enter Room Code",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Ask your host for the 4-letter code (e.g. WOLF):",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    OutlinedTextField(
                        value = joinCodeInput,
                        onValueChange = { joinCodeInput = it.uppercase().take(6) },
                        placeholder = { Text("e.g. WOLF", color = TextTertiary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonLime,
                            unfocusedBorderColor = DarkPurpleBorder
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("room_code_input")
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showJoinDialog = false
                        onJoinRoom(joinCodeInput)
                    },
                    modifier = Modifier.testTag("submit_join_button")
                ) {
                    Text("JOIN SQUAD", color = NeonLime, fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showJoinDialog = false }) {
                    Text("CANCEL", color = TextTertiary)
                }
            }
        )
    }

    // Profile Dialog
    if (showProfileDialog) {
        ProfileCustomizerDialog(
            currentName = playerName,
            currentAvatar = playerAvatar,
            onDismiss = { showProfileDialog = false },
            onSave = { name, avatar ->
                onUpdateProfile(name, avatar)
                showProfileDialog = false
            }
        )
    }
}
