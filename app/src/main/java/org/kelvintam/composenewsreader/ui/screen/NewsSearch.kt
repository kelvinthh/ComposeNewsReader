@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.ui.component.NewsCard
import org.kelvintam.composenewsreader.R
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper
import org.kelvintam.composenewsreader.datamodel.SortBy
import org.kelvintam.composenewsreader.ui.theme.StickyHeaderBg
import org.kelvintam.composenewsreader.ui.theme.StickyHeaderText
import org.kelvintam.composenewsreader.ui.theme.TextStyle
import java.net.URLEncoder

@Destination
@Composable
fun NewsSearch(navigator: DestinationsNavigator, viewModel: CNRViewModel) {

    val TAG = "NewsSearch"

    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current


    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = { SheetBody(viewModel, modalBottomSheetState, coroutineScope) }

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Log.d(TAG, "News Search")
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            top = 10.dp, bottom = 10.dp
                        )
                        .weight(4f),
                    placeholder = { Text(text = "Enter any keyword") },
                    value = viewModel.searchBoxText,
                    onValueChange = { viewModel.searchBoxText = it },
                    singleLine = true,
                    shape = RoundedCornerShape(5.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            "Search Box"
                        )
                    },
                    trailingIcon = {
                        if (viewModel.searchBoxText.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_cancel_24),
                                    contentDescription = "Clear Search",
                                    Modifier.clickable { viewModel.searchBoxText = "" }
                                )
                                TextButton(onClick = { search(viewModel, focusManager) }) {
                                    Text(text = "Search")
                                }
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { search(viewModel, focusManager) }
                    ),
                )

                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    modifier = Modifier.weight(0.75f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                        "Search Settings"
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .background(StickyHeaderBg)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(36.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Found ${viewModel.newsList.size} result(s)",
                            fontWeight = FontWeight.W500,
                            fontSize = 16.sp,
                            color = StickyHeaderText
                        )
                    }
                }
                items(items = viewModel.newsList, key = { it.url }) { article ->
                    NewsCard(navigator, article) { viewModel.writeRecentReadListToPref(it) }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.SheetBody(
    viewModel: CNRViewModel,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    Divider(
        color = Color(0xFFDCDCDC),
        thickness = 4.dp,
        modifier = Modifier
            .fillMaxWidth(0.2f)
            .padding(vertical = 12.dp)
            .align(Alignment.CenterHorizontally)
    )
    Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)) {
        Text("Search Settings", style = TextStyle.header)
        Spacer(modifier = Modifier.height(20.dp))
        ////////////////////////////
        Text("Sorting", style = TextStyle.subHeader)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SortBy.values().forEach {
                if (viewModel.sortBy == it) {
                    OutlinedButton(
                        onClick = { viewModel.sortBy = it }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primarySurface,
                            contentColor = Color.White
                        )
                    ) {
                        Text(it.title)
                    }
                    return@forEach
                }

                OutlinedButton(
                    onClick = { viewModel.sortBy = it },
                ) {
                    Text(it.title)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { coroutineScope.launch { state.hide() } }) {
            Text("Close")
        }

    }
}

fun search(viewModel: CNRViewModel, focusManager: FocusManager) {
    focusManager.clearFocus()
    val encodedString =
        URLEncoder.encode(viewModel.searchBoxText, "utf-8")
    CoroutineScope(Dispatchers.IO).launch {
        val result =
            RetrofitHelper.getNewsFromTopicCall(encodedString, viewModel.sortBy)
        if (result != null) {
            viewModel.newsList = result
        }
    }
}
