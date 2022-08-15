package org.kelvintam.composenewsreader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import org.kelvintam.composenewsreader.datamodel.NewsModel
import org.kelvintam.composenewsreader.ui.screen.MainScreen
import org.kelvintam.composenewsreader.ui.screen.NavGraphs
import org.kelvintam.composenewsreader.ui.screen.destinations.MainScreenDestination
import org.kelvintam.composenewsreader.ui.theme.ComposeNewsReaderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNewsReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root){
                        composable(MainScreenDestination) {
                            MainScreen(CNRViewModel())
                        }
                    }
                }
            }
        }
    }

}

class CNRViewModel: ViewModel() {
    init {
        Log.d("CNRViewModel", "View Model init")
    }
    var newsList by mutableStateOf(emptyList<NewsModel.Article>())
}