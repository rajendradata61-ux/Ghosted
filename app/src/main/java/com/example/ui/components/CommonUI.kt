package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Player
import com.example.ui.theme.*

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accentColor: Color = NeonLime,
    textColor: Color = Color(0xFF0C071E),
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = textColor,
            disabledContainerColor = Color(0xFF2C224D),
            disabledContentColor = Color(0xFF7B6D9E)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .border(
                width = if (enabled) 1.5.dp else 0.dp,
                color = if (enabled) accentColor.copy(alpha = 0.8f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("neon_button_${text.replace(" ", "_").lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            leadingIcon?.invoke()
            if (leadingIcon != null) Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    borderColor: Color = DarkCardBorder,
    backgroundColor: Color = DarkSurface,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, borderColor, RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}

@Composable
fun PlayerAvatar(
    player: Player,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    showScore: Boolean = true,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            // Avatar circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                if (isSelected) HotCoralDark else DarkSurfaceVariant,
                                DarkSurface
                            )
                        )
                    )
                    .border(
                        width = if (isSelected) 2.5.dp else 1.5.dp,
                        color = if (isSelected) HotCoral else if (player.isHost) NeonLime else DarkPurpleBorder,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = player.avatarEmoji,
                    fontSize = (size.value * 0.45).sp
                )
            }

            // Host crown / checkmark
            if (player.isHost) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(WarningGold)
                ) {
                    Text("👑", fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = player.name,
            color = if (player.isLocal) NeonLime else TextPrimary,
            fontSize = 12.sp,
            fontWeight = if (player.isLocal) FontWeight.ExtraBold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (showScore) {
            Text(
                text = "${player.score} pts",
                color = ElectricCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

