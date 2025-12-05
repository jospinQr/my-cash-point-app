package org.megamind.mycashpoint.ui.screen.admin.dash_board

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.megamind.mycashpoint.ui.component.SkeletonLoadingEffect
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme

@Composable
fun DashBoardLoadingSkeleton(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        // --- Opérateurs ---
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(4) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SkeletonLoadingEffect(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    SkeletonLoadingEffect(
                        modifier = Modifier
                            .width(70.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // --- Devise ---
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(3) {
                SkeletonLoadingEffect(
                    modifier = Modifier
                        .width(80.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
        }

        Spacer(Modifier.height(20.dp))

        // --- Pie Chart ---
        PieChartSkeleton()

        Spacer(Modifier.height(20.dp))

        // --- Solde Section ---
        SoldeCardSkeleton()

        Spacer(Modifier.height(24.dp))

        // --- Actions rapides ---
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(4),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.Center
        ) {
            items(4) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    SkeletonLoadingEffect(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SkeletonLoadingEffect(
                        modifier = Modifier
                            .width(60.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun PieChartSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(12.dp)) {

            SkeletonLoadingEffect(
                modifier = Modifier
                    .fillMaxWidth(.4f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Fake pie chart circle
            SkeletonLoadingEffect(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun SoldeCardSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(12.dp)) {

            SkeletonLoadingEffect(
                modifier = Modifier
                    .fillMaxWidth(.3f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                SkeletonLoadingEffect(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    SkeletonLoadingEffect(
                        modifier = Modifier
                            .width(140.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SkeletonLoadingEffect(
                        modifier = Modifier
                            .width(110.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            SkeletonLoadingEffect(
                modifier = Modifier
                    .fillMaxWidth(.6f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(Modifier.height(12.dp))

            SkeletonLoadingEffect(
                modifier = Modifier
                    .fillMaxWidth(.4f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SkPreView() {

    MyCashPointTheme {
        DashBoardLoadingSkeleton()

    }
}