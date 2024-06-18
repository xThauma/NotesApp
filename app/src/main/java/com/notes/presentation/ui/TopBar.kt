package com.notes.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.notes.data.event.NoteUiEvent
import com.notes.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
        title: String,
        navController: NavHostController,
        onEvent: (NoteUiEvent) -> Unit,
        navigateToAddNote: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var searchVisible by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }

    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = if (navController.previousBackStackEntry != null) {
                Icons.AutoMirrored.Filled.ArrowBack
            } else {
                Icons.AutoMirrored.Filled.Assignment
            },
                    contentDescription = "Navigation Icon",
                    modifier = Modifier.clickable {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        }
                    })
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title)
        }
    },
            actions = {
                when (currentRoute) {
                    Routes.NOTES -> {
                        NotesRouteTopBar(searchVisible = searchVisible,
                                searchText = searchText,
                                onEvent = onEvent,
                                navigateToAddNote = navigateToAddNote,
                                onSearchTextChanged = {
                                    searchText = it
                                    onEvent(NoteUiEvent.FilterEvent.TextChange(it))
                                },
                                onSearchClosed = {
                                    searchVisible = false
                                    searchText = ""
                                    onEvent(NoteUiEvent.FilterEvent.TextChange(searchText))
                                },
                                onSearchClicked = { searchVisible = true })
                    }
                }
            })
}

@Composable
fun NotesRouteTopBar(
        searchVisible: Boolean,
        searchText: String,
        onEvent: (NoteUiEvent) -> Unit,
        navigateToAddNote: () -> Unit,
        onSearchTextChanged: (String) -> Unit,
        onSearchClosed: () -> Unit,
        onSearchClicked: () -> Unit
) {
    if (searchVisible) {
        NoteSearchBar(searchText = searchText,
                onSearchTextChanged = {
                    onSearchTextChanged(it)
                },
                onSearchClose = {
                    onSearchClosed()
                })
    } else {
        NoteSearchIcon(onSearchClick = { onSearchClicked() })
        NoteAddIcon { navigateToAddNote.invoke() }
        NoteSortIcon { onEvent(it) }
        NoteAuthIcon {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteSearchBar(
        searchText: String,
        onSearchTextChanged: (String) -> Unit,
        onSearchClose: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Box(
            Modifier
                .fillMaxSize()
                .padding(
                        top = 6.dp,
                        end = 12.dp,
                        start = 12.dp,
                        bottom = 6.dp
                )
                .semantics { isTraversalGroup = true }) {
        SearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .semantics { traversalIndex = 0f },
                inputField = {
                    SearchBarDefaults.InputField(
                            modifier = Modifier.focusRequester(focusRequester),
                            query = searchText,
                            onQueryChange = {
                                onSearchTextChanged(it)
                            },
                            onSearch = {},
                            expanded = false,
                            onExpandedChange = { expanded = it },
                            placeholder = { Text("Type to search ...") },
                            leadingIcon = {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            onSearchClose()
                                        })
                            },
                    )
                },
                expanded = false,
                onExpandedChange = { expanded = false },
        ) {}
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun NoteAuthIcon(
        onAuthClick: () -> Unit,
) {
    Icon(imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Auth Icon",
            modifier = Modifier
                .padding(12.dp)
                .clickable {
                    onAuthClick()
                })
}

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

@Composable
fun NoteAddIcon(
        navigateToAddNote: () -> Unit,
) {
    Icon(imageVector = Icons.Filled.Add,
            contentDescription = "Add Icon",
            modifier = Modifier
                .padding(12.dp)
                .clickable {
                    navigateToAddNote.invoke()
                })
}

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

@Preview(
        showBackground = true,
        showSystemUi = true
)
@Composable
fun AppTopBarPreview() {
    Row {


        NotesRouteTopBar(searchVisible = false,
                searchText = "",
                onEvent = {},
                navigateToAddNote = { /*TODO*/ },
                onSearchTextChanged = {},
                onSearchClosed = { /*TODO*/ }) {}
    }
}