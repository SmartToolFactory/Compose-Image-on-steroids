package com.smarttoolfactory.image.beforeafter

import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.image.R

/**
 * Default overlay for [BeforeAfterImage] and [BeforeAfterLayout] that draws line and
 * thumb with properties provided.
 *
 * @param width of the [BeforeAfterImage] or [BeforeAfterLayout]. You should get width from
 * scope of these Composables and pass to calculate bounds correctly
 * @param height of the [BeforeAfterImage] or [BeforeAfterLayout]. You should get height from
 * scope of these Composables and pass to calculate bounds correctly
 * @param position current position or progress of before/after
 * @param verticalThumbMove whether thumb should move vertically
 * @param lineColor color if divider line
 * @param thumbResource drawable resource that should be used with thumb
 * @param thumbSize size of the thumb in dp
 * @param thumbPositionPercent vertical position of thumb if [verticalThumbMove] is false.
 * It's betweem [0f-100f] to set thumb's vertical position in layout
 */
@Composable
fun DefaultOverlay(
    width:Dp,
    height: Dp,
    position: Offset,
    verticalThumbMove: Boolean = false,
    lineColor: Color = Color.White,
    @DrawableRes thumbResource: Int = R.drawable.baseline_swap_horiz_24,
    thumbSize: Dp = 36.dp,
    @FloatRange(from = 0.0, to = 100.0) thumbPositionPercent: Float = 85f,
) {


    var positionX = position.x
    var positionY = position.y

    val linePosition: Float

    val density = LocalDensity.current

    with(density) {
        val thumbRadius = (thumbSize / 2).toPx()
        val imageWidthInPx = width.toPx()
        val imageHeightInPx = height.toPx()

        linePosition = positionX.coerceIn(0f, imageWidthInPx)
        positionX -= width.toPx() / 2

        positionY = if (verticalThumbMove) {
            (positionY - imageHeightInPx / 2)
                .coerceIn(
                    -imageHeightInPx / 2 + thumbRadius,
                    imageHeightInPx / 2 - thumbRadius
                )
        } else {
            val thumbPosition = thumbPositionPercent.coerceIn(0f, 100f)
            ((imageHeightInPx * thumbPosition / 100f - thumbRadius) - imageHeightInPx / 2)
        }
    }

    Canvas(modifier = Modifier.size(width, height)) {

        drawLine(
            lineColor,
            strokeWidth = 1.5.dp.toPx(),
            start = Offset(linePosition, 0f),
            end = Offset(linePosition, size.height)
        )
    }

    Icon(
        painter = painterResource(id = thumbResource),
        contentDescription = null,
        tint = Color.Gray,
        modifier = Modifier
            .offset {
                IntOffset(positionX.toInt(), positionY.toInt())
            }
            .shadow(2.dp, CircleShape)
            .background(Color.White)
            .size(thumbSize)
            .padding(4.dp)
    )
}