package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CallOption
import com.example.ui.components.NeonCard
import com.example.ui.theme.*

@Composable
fun PhoneCallScenarioScreen(
    timerSecondsRemaining: Int,
    options: List<CallOption>,
    selectedOptionId: Int?,
    onSelectOption: (CallOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Timer Bar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ChipBg,
                    border = BorderStroke(1.dp, if (timerSecondsRemaining <= 3) HotCoral else ElectricCyan)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = if (timerSecondsRemaining <= 3) HotCoral else ElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${timerSecondsRemaining}s PANIC TIMER",
                            color = if (timerSecondsRemaining <= 3) HotCoral else ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Text(
                    text = "DODGE THE CALL! 📞",
                    color = WarningGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Timer Progress Bar
            val progress = (timerSecondsRemaining / 10f).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (timerSecondsRemaining <= 3) HotCoral else NeonLime,
                trackColor = DarkSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Incoming Phone Call Simulation UI
            NeonCard(
                borderColor = if (timerSecondsRemaining <= 3) HotCoral else ElectricPurple,
                backgroundColor = DarkSurfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Pulsing Avatar
                    Box(
                        modifier = Modifier
                            .scale(pulseScale)
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFFE91E63), Color(0xFF673AB7))
                                )
                            )
                            .border(3.dp, if (timerSecondsRemaining <= 3) HotCoral else NeonLime, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👵", fontSize = 38.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "INCOMING CALL...",
                        color = HotCoral,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Sharma Chachi (Auntie)",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "\"Beta rishta photo bheji hai WhatsApp pe, aur package kitna hua?\"",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        // 3 Excuse Options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "CHOOSE YOUR EXCUSE BEFORE 0s:",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            options.forEachIndexed { index, option ->
                val isSelected = (selectedOptionId == option.id)
                val cardBorder = when {
                    isSelected && option.isSuccess -> NeonLime
                    isSelected && !option.isSuccess -> HotCoral
                    else -> DarkPurpleBorder
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFF281944) else DarkSurface,
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectOption(option) }
                        .testTag("excuse_option_${option.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ChipBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(option.emoji, fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option.text,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.subtext,
                                color = if (option.isSuccess) ElectricCyan else TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
