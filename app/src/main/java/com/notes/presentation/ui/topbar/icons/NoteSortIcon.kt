package com.notes.presentation.ui.topbar.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.notes.data.event.NoteUiEvent

@Composable
fun NoteSortIcon(
    onUiSortEvent: (NoteUiEvent) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Icon(imageVector = Icons.Filled.FilterList,
        contentDescription = "Filter Icon",
        modifier = Modifier
            .padding(12.dp)
            .clickable {
                expanded = true
            })
    DropdownMenu(expanded = expanded,
        onDismissRequest = { expanded = false }) {
        DropdownMenuItem(text = { Text("Sort Ascending") },
            onClick = {
                onUiSortEvent(NoteUiEvent.SortEvent.Ascending)
                expanded = false
            })
        DropdownMenuItem(text = { Text("Sort Descending") },
            onClick = {
                onUiSortEvent(NoteUiEvent.SortEvent.Descending)
                expanded = false
            })
    }
}