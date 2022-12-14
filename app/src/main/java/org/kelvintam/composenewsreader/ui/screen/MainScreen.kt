@file:OptIn(ExperimentalPagerApi::class)

package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.*
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.datamodel.Tabs

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    viewModel: CNRViewModel,
    navigator: DestinationsNavigator,
) {
    val TAG = "MainScreen"

    val pagerState = rememberPagerState()
    val tabs = Tabs.values()

    TopAppBar(
        title = { Text(text = "Compose News Reader") }
    )

    TabRow(
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        // Add tabs for all of our pages
        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }

    HorizontalPager(
        count = tabs.size,
        state = pagerState,
    ) { page ->
        Log.d(TAG, "Main Screen")
        when (tabs[page]) {
            Tabs.HEADLINES -> HeadlineNews(navigator, viewModel)
            Tabs.NEWS_SEARCH -> NewsSearch(navigator, viewModel)
            Tabs.RECENT_READ -> RecentRead(navigator, viewModel)
        }

    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.currentTab = tabs[pagerState.currentPage]
    }

}