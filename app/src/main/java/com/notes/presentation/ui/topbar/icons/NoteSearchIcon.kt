package com.notes.presentation.ui.topbar.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoteSearchIcon(
    onSearchClick: () -> Unit,
) {
    Icon(imageVector = Icons.Filled.Search,
        contentDescription = "Search Icon",
        modifier = Modifier
            .padding(12.dp)
            .clickable {
                onSearchClick()
            })
}

