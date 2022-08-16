package org.kelvintam.composenewsreader

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import org.kelvintam.composenewsreader.datamodel.CNRViewModel
import org.kelvintam.composenewsreader.ui.screen.MainScreen
import org.kelvintam.composenewsreader.ui.screen.NavGraphs
import org.kelvintam.composenewsreader.ui.screen.destinations.MainScreenDestination
import org.kelvintam.composenewsreader.ui.theme.ComposeNewsReaderTheme


class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = this.getSharedPreferences("preference_key", Context.MODE_PRIVATE)
        val viewModel = CNRViewModel(sharedPreferences)
        setContent {
            ComposeNewsReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root) {
                        composable(MainScreenDestination) {
                            Column(Modifier.fillMaxSize()) {
                                MainScreen(viewModel, destinationsNavigator)
                            }
                        }
                    }
                }
            }
        }
    }
}