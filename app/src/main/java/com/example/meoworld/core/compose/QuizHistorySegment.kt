package com.example.meoworld.core.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.meoworld.data.database.entities.ResultDbModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Box


@Composable
fun QuizHistorySegment(
    quizResults: List<ResultDbModel>,
    maxVisibleRows: Int
) {
    Text(
        text = "Quiz History",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
    )

    InformationRow(
        title = "Games played",
        value = quizResults.size.toString(),
    )

    Spacer(modifier = Modifier.height(16.dp))
    QuizHistoryTable(
        quizResults = quizResults,
        maxVisibleRows = maxVisibleRows
            )
}



@Composable
fun QuizHistoryTable(
    quizResults: List<ResultDbModel>,
    maxVisibleRows: Int
) {
    val borderWidth = 1.dp
    val headerBackgroundColor = Color(0xFFA2A2BD)

    val rowHeightDp = 42.dp  // Approximate height per data row (including padding)
    val headerHeightDp = 48.dp // Approximate height for header row

    val maxHeight = headerHeightDp + (rowHeightDp * maxVisibleRows)

    val scrollState = rememberScrollState()

    Box(
        modifier = if (quizResults.size > maxVisibleRows) {
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .verticalScroll(scrollState)
        } else {
            Modifier.fillMaxWidth()
        }
    ) {
        QuizHistoryTableContent(
            quizResults = quizResults,
            borderWidth = borderWidth,
            headerBackgroundColor = headerBackgroundColor
        )
    }
}


@Composable
fun QuizHistoryTableContent(
    quizResults: List<ResultDbModel>,
    borderWidth: Dp,
    headerBackgroundColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuizHeaderCell("Result")
            QuizHeaderCell("Created At")
            QuizHeaderCell("Published")
        }

        quizResults.forEachIndexed { index, result ->
            val backgroundColor = MaterialTheme.colorScheme.surface

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuizCell(result.result.toString())
                QuizCell(formatTimestamp(result.createdAt))
                QuizCell(if (result.published) "Yes" else "No")
            }

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.5.dp)
        }
    }
}

@Composable
fun QuizHeaderCell(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        modifier = Modifier
            .padding(horizontal = 4.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun QuizCell(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(horizontal = 4.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )
}



fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
    val date = java.util.Date(timestamp)
    return sdf.format(date)
}