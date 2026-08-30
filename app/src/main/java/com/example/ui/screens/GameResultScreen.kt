package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameResultData
import com.example.ui.components.NeonButton
import com.example.ui.components.NeonCard
import com.example.ui.theme.*

@Composable
fun GameResultScreen(
    resultData: GameResultData?,
    onPlayAgain: () -> Unit,
    onBackToLobby: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isWin = resultData?.isWin ?: false
    val headerColor = if (isWin) NeonLime else HotCoral

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
            Spacer(modifier = Modifier.height(16.dp))

            // Result Hero Avatar
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = if (isWin) listOf(Color(0xFF00E676), Color(0xFF0D47A1))
                            else listOf(Color(0xFFFF5252), Color(0xFF4A148C))
                        )
                    )
                    .border(4.dp, headerColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isWin) "😎" else "💀",
                    fontSize = 50.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Status Tag
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ChipBg,
                border = BorderStroke(1.dp, headerColor)
            ) {
                Text(
                    text = if (isWin) "VICTORY • JEET GAYE! 🏆" else "DEFEAT • HAAR GAYE! 💀",
                    color = headerColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Result Title
            Text(
                text = resultData?.title ?: "ROUND ENDED",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Result Description
            Text(
                text = resultData?.subtitle ?: "The situation concluded.",
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Stats Card
            NeonCard(
                borderColor = if (isWin) NeonLime else DarkPurpleBorder,
                backgroundColor = DarkSurfaceVariant
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ROUND STATS",
                        color = TextTertiary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("XP GAINED", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = "+${resultData?.scoreGained ?: 0} pts",
                                color = if (isWin) NeonLime else TextTertiary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("REACTION TIME", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = "${resultData?.timeTakenSeconds ?: 0}s",
                                color = ElectricCyan,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SOCIAL BATTERY", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = if (isWin) "100% ⚡" else "0% 🪫",
                                color = if (isWin) WarningGold else HotCoral,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    if (resultData?.chosenExcuse != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = DarkPurpleBorder)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "YOUR EXCUSE:",
                            color = TextTertiary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${resultData.chosenExcuse.emoji} ${resultData.chosenExcuse.text}",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NeonButton(
                text = "PLAY AGAIN 🔄",
                onClick = onPlayAgain,
                accentColor = NeonLime,
                textColor = Color(0xFF0C071E),
                modifier = Modifier.testTag("play_again_button")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onBackToLobby,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("back_to_lobby_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ElectricCyan),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ChipBg)
                ) {
                    Text("LOBBY 🎮", color = ElectricCyan, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onBackToHome,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("back_to_home_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ElectricPurple),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = ChipBg)
                ) {
                    Text("HOME 🏠", color = ElectricPurple, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
