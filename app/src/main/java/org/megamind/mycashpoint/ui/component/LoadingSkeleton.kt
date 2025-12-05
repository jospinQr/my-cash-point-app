package org.megamind.mycashpoint.ui.component


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp


@Composable
fun SkeletonLoadingEffect(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.onBackground.copy(.1f),
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.onBackground.copy(.1f),
    )
    val shimmerAnimation = rememberInfiniteTransition(label = "")
    val translateAnim by shimmerAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1700, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim),
    )

    Box(
        modifier = modifier
            .background(brush)
    ) {

    }
}


@Composable
fun DetailLoadingSkeleton(modifier: Modifier = Modifier) {

    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth(.3f)
                .height(6.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth(.6f)
                .height(6.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .height(6.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .height(6.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth(.3f)
                .height(6.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }


}


@Composable
fun HomeLoadingSkeleton(modifier: Modifier = Modifier) {

    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        Spacer(modifier = Modifier.height(4.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .height(10.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth(.4f)
                .height(10.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            SkeletonLoadingEffect(
                modifier = Modifier
                    .width(85.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(100))
            )
            Spacer(modifier = Modifier.width(18.dp))
            SkeletonLoadingEffect(
                modifier = Modifier
                    .width(200.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(100))
            )
            Spacer(modifier = Modifier.width(18.dp))
            SkeletonLoadingEffect(
                modifier = Modifier
                    .width(200.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(100))
            )

        }
        Spacer(modifier = Modifier.height(24.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .width(200.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(100))
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SkeletonLoadingEffect(
                modifier = Modifier
                    .width(60.dp)
                    .height(230.dp)
                    .clip(RoundedCornerShape(10))
            )
            SkeletonLoadingEffect(
                modifier = Modifier
                    .width(200.dp)
                    .height(230.dp)
                    .clip(RoundedCornerShape(10))
            )


            SkeletonLoadingEffect(
                modifier = Modifier
                    .width(60.dp)
                    .height(230.dp)
                    .clip(RoundedCornerShape(10))
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(10))
        )
        Spacer(modifier = Modifier.height(20.dp))
        SkeletonLoadingEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(10))
        )


    }


}