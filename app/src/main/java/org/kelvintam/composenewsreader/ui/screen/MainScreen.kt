@file:OptIn(ExperimentalPagerApi::class)

package org.kelvintam.composenewsreader.ui.screen

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.*
import org.kelvintam.composenewsreader.CNRViewModel
import org.kelvintam.composenewsreader.datamodel.NewsModel
import org.kelvintam.composenewsreader.datamodel.RetrofitHelper
import org.kelvintam.composenewsreader.datamodel.Tabs
import org.kelvintam.composenewsreader.ui.screen.destinations.NewsDetailDestination

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(viewModel: CNRViewModel, navigator: DestinationsNavigator) {
    val TAG = "Main Screen"
    val pagerState = rememberPagerState()
    val tabs = Tabs.values()
    //val newsList = remember { viewModel.newsList }

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
        when (tabs[page]) {
            Tabs.HEADLINES -> HeadlineNews(navigator, viewModel)
            Tabs.ALL_NEWS -> NewsList(navigator, viewModel)
            else -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(tabs[page].title)
                }
            }
        }

    }

}