package com.note.coffee

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.note.coffee.data.AppDatabase
import com.note.coffee.ui.beans.BeansViewModel
import com.note.coffee.ui.drippers.DrippersViewModel
import com.note.coffee.ui.handmills.HandMillsViewModel
import com.note.coffee.ui.origin.OriginViewModel
import com.note.coffee.ui.recipes.RecipesViewModel
import com.note.coffee.ui.roastery.RoasteryViewModel
import com.note.coffee.ui.theme.CoffingNoteTheme
import com.note.coffee.ui.water.WaterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = application as MainApplication

        Log.e("MainActivity", "start")

        setContent {
            val adShowCompleted = remember { mutableStateOf(false) }

            val focusManager = LocalFocusManager.current

            val beansViewModel: BeansViewModel = hiltViewModel()
            val handMillsViewModel: HandMillsViewModel = hiltViewModel()
            val drippersViewModel: DrippersViewModel = hiltViewModel()
            val recipesViewModel: RecipesViewModel = hiltViewModel()
            val originViewModel: OriginViewModel = hiltViewModel()
            val roasteryViewModel: RoasteryViewModel = hiltViewModel()
            val waterViewModel: WaterViewModel = hiltViewModel()

            AppDatabase.databaseCreated.observe(this) {
                if (it) {
                    Log.d("init", "databaseCreated")
                    originViewModel.loadOrigins()
                }
            }

            if(!adShowCompleted.value) {

                application.showAdIfAvailable(
                    this@MainActivity,
                    object : MainApplication.OnShowAdCompleteListener {
                        override fun onShowAdComplete() {
                            adShowCompleted.value = true
                        }
                    }
                ) { adShowCompleted.value = true }
            }


            if(adShowCompleted.value) {
                CoffingNoteTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                        ,
                        color = MaterialTheme.colors.background
                    ) {
                        Column() {
                            CoffingNoteApp(
                                beansViewModel = beansViewModel,
                                handMillsViewModel = handMillsViewModel,
                                drippersViewModel = drippersViewModel,
                                recipesViewModel = recipesViewModel,
                                roasteryViewModel = roasteryViewModel,
                                originViewModel = originViewModel,
                                waterViewModel = waterViewModel,
                            )
                        }
                    }
                }
            }
        }
    }



}
