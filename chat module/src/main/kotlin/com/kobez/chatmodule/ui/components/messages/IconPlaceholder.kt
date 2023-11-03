package com.kobez.chatmodule.ui.components.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
public fun IconPlaceholder(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = modifier.size(32.dp)
    ) {
        val avatarOffset = DpOffset(8.dp, 8.dp)
        Box(
            modifier = modifier
                .clip(shape)
                .background(color = Color(0xFF0068EF)),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(avatarOffset.x, avatarOffset.y)
                    .fillMaxSize(),
                text = "P+",
                fontWeight = FontWeight.Bold,
                color = Color.White,

                )
        }
    }
}