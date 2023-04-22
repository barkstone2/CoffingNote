package com.note.coffee

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.note.coffee.ui.beans.*
import com.note.coffee.ui.common.BannerAdView
import com.note.coffee.ui.drippers.*
import com.note.coffee.ui.handmills.*
import com.note.coffee.ui.origin.*
import com.note.coffee.ui.recipes.*
import com.note.coffee.ui.roastery.*
import com.note.coffee.ui.theme.*
import com.note.coffee.ui.water.WaterDetailScreen
import com.note.coffee.ui.water.WaterListScreen
import com.note.coffee.ui.water.WaterSaveScreen
import com.note.coffee.ui.water.WaterUpdateScreen
import com.note.coffee.ui.water.WaterViewModel


enum class CoffingNoteScreen(
    @StringRes val title: Int,
    val hasTopBar: Boolean = false,
    val canNavigateBack: Boolean = false,
    val hasNavBar: Boolean = true,
    val group: Int? = null,
) {

    RecipeBeanList(
        title = R.string.recipes_list, group = 0, hasTopBar = true
    ),
    RecipeList(
        title = R.string.recipes_detail_list, group = 0, hasTopBar = true, canNavigateBack = true
    ),
    RecipeSave(
        title = R.string.recipes_save, group = 0, hasTopBar = true, canNavigateBack = true
    ),
    RecipeDetail(
        title = R.string.recipes_detail, group = 0, hasTopBar = true, canNavigateBack = true
    ),
    RecipeUpdate(
        title = R.string.recipes_update, group = 0, hasTopBar = true, canNavigateBack = true
    ),

    BeanList(
        title = R.string.beans_list, group = 1, hasTopBar = true
    ),
    BeanSave(
        title = R.string.beans_save, group = 1, hasTopBar = true, canNavigateBack = true
    ),
    BeanDetail(
        title = R.string.beans_detail, group = 1, hasTopBar = true, canNavigateBack = true
    ),
    BeanUpdate(
        title = R.string.beans_update, group = 1, hasTopBar = true, canNavigateBack = true
    ),

    HandMillList(
        title = R.string.hand_mills_list, group = 2, hasTopBar = true
    ),
    HandMillSave(
        title = R.string.hand_mills_save, group = 2, hasTopBar = true, canNavigateBack = true
    ),
    HandMillDetail(
        title = R.string.hand_mills_detail, group = 2, hasTopBar = true, canNavigateBack = true
    ),
    HandMillUpdate(
        title = R.string.hand_mills_update, group = 2, hasTopBar = true, canNavigateBack = true
    ),

    DripperList(
        title = R.string.drippers_list, group = 3, hasTopBar = true
    ),
    DripperSave(
        title = R.string.drippers_save, group = 3, hasTopBar = true, canNavigateBack = true
    ),
    DripperDetail(
        title = R.string.drippers_detail, group = 3, hasTopBar = true, canNavigateBack = true
    ),
    DripperUpdate(
        title = R.string.drippers_update, group = 3, hasTopBar = true, canNavigateBack = true
    ),

    OriginList(
        title = R.string.origin_list, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    OriginSave(
        title = R.string.origin_save, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    OriginDetail(
        title = R.string.origin_detail, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    OriginUpdate(
        title = R.string.origin_update, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),

    RoasteryList(
        title = R.string.roastery_list, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    RoasterySave(
        title = R.string.roastery_save, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    RoasteryDetail(
        title = R.string.roastery_detail, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    RoasteryUpdate(
        title = R.string.roastery_update, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),

    WaterList(
        title = R.string.water_list, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    WaterSave(
        title = R.string.water_save, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    WaterDetail(
        title = R.string.water_detail, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
    WaterUpdate(
        title = R.string.water_update, hasTopBar = true, canNavigateBack = true, hasNavBar = false
    ),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CoffingNoteAppBar(
    currentScreen: CoffingNoteScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onClickNavMenu: (CoffingNoteScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (currentScreen.hasTopBar) {
        Column() {
            TopAppBar(
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                },
                backgroundColor = LightCoffee,
                contentColor = White,
                modifier = modifier.padding(start = 0.dp),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(currentScreen.title),
                            style = Typography.headlineLarge
                        )
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End,
                        ) {
                            var expanded by remember { mutableStateOf(false) }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { },
                            ) {
                                IconButton(
                                    onClick = { expanded = !expanded },
                                    modifier = Modifier.padding(0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = ""
                                    )
                                }

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                ) {
                                    DropdownMenuItem(
                                        onClick = {
                                            onClickNavMenu(CoffingNoteScreen.OriginList)
                                            expanded = false
                                        },
                                        text = {
                                            Text(
                                                text = "원산지 관리",
                                                style = Typography.labelMedium
                                            )
                                        },
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            onClickNavMenu(CoffingNoteScreen.RoasteryList)
                                            expanded = false
                                        },
                                        text = {
                                            Text(
                                                text = "로스터리 관리",
                                                style = Typography.labelMedium
                                            )
                                        },
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            onClickNavMenu(CoffingNoteScreen.WaterList)
                                            expanded = false
                                        },
                                        text = {
                                            Text(
                                                text = "물 관리",
                                                style = Typography.labelMedium
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            )
            BannerAdView()
        }
    }
}

@Composable
fun CoffingNoteBottomNavigation(
    onClicks: List<() -> Unit>,
    currentScreen: CoffingNoteScreen
) {

    if (currentScreen.hasNavBar) {
        var selectedItem by rememberSaveable { mutableStateOf(currentScreen.group ?: 0) }
        val icons = listOf(
            painterResource(R.drawable.ic_launcher_background),
            //        painterResource(R.drawable.navbar_setting),
            //        painterResource(R.drawable.navbar_checklist),
            //        painterResource(R.drawable.navbar_setting),
            //        painterResource(R.drawable.navbar_checklist),
        )

        val temps = listOf(
            "레시피",
            "원두",
            "핸드밀",
            "드리퍼"
        )

        Column() {
            NavigationBar(
                containerColor = MaterialTheme.colors.surface,
                modifier = Modifier
                    .height(60.dp),
            ) {

                temps.forEachIndexed { index, icon ->
                    NavigationBarItem(
                        icon = {
                            //                    Image(icon,
                            //                        contentDescription = "test"
                            //                    )
                        },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            onClicks[index]()
                        },
                        label = {
                            Text(
                                text = icon,
                                style = Typography.headlineMedium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = LightCoffee)
                    )
                }
            }
        }
    }

}

@Composable
fun CoffingNoteApp(
    beansViewModel: BeansViewModel,
    handMillsViewModel: HandMillsViewModel,
    drippersViewModel: DrippersViewModel,
    recipesViewModel: RecipesViewModel,
    originViewModel: OriginViewModel,
    roasteryViewModel: RoasteryViewModel,
    waterViewModel: WaterViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CoffingNoteScreen.valueOf(
        backStackEntry?.destination?.route ?: CoffingNoteScreen.RecipeBeanList.name
    )
    val onClicks = listOf(
        { navController.navigate(CoffingNoteScreen.RecipeBeanList.name) { popUpTo(0) } },
        { navController.navigate(CoffingNoteScreen.BeanList.name) { popUpTo(0) } },
        { navController.navigate(CoffingNoteScreen.HandMillList.name) { popUpTo(0) } },
        { navController.navigate(CoffingNoteScreen.DripperList.name) { popUpTo(0) } },
    )

    val beansUiState by beansViewModel.uiState.collectAsStateWithLifecycle()
    val handMillsUiState by handMillsViewModel.uiState.collectAsStateWithLifecycle()
    val drippersUiState by drippersViewModel.uiState.collectAsStateWithLifecycle()
    val recipesUiState by recipesViewModel.uiState.collectAsStateWithLifecycle()
    val originUiState by originViewModel.uiState.collectAsStateWithLifecycle()
    val roasteryUiState by roasteryViewModel.uiState.collectAsStateWithLifecycle()
    val waterUiState by waterViewModel.uiState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            CoffingNoteAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null
                        && CoffingNoteScreen.valueOf(currentScreen.name).canNavigateBack,
                navigateUp = { navController.navigateUp() },
                onClickNavMenu = {
                    navController.navigate(it.name) {
                        popUpTo(it.name) { inclusive = true }
                    }
                }
            )
        },
        bottomBar = {
            CoffingNoteBottomNavigation(
                onClicks = onClicks,
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CoffingNoteScreen.RecipeBeanList.name,
            modifier = modifier.padding(innerPadding)
        ) {

            /** Recipe Screen **/
            composable(route = CoffingNoteScreen.RecipeBeanList.name) {
                RecipeBeanListScreen(
                    recipesUiState = recipesUiState,
                    onNavigateToSave = {
                        recipesViewModel.selectRecipeBean(null)
                        navController.navigate(CoffingNoteScreen.RecipeSave.name)
                    },
                    onNavigateToRecipeList = {
                        recipesViewModel.selectRecipeBean(it)
                        navController.navigate(CoffingNoteScreen.RecipeList.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.RecipeList.name) {
                RecipeListScreen(
                    recipesUiState = recipesUiState,
                    onNavigateToSave = { navController.navigate(CoffingNoteScreen.RecipeSave.name) },
                    onNavigateToDetail = {
                        recipesViewModel.getRecipe(it)
                        navController.navigate(CoffingNoteScreen.RecipeDetail.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.RecipeSave.name) {
                RecipeSaveScreen(
                    recipesUiState = recipesUiState,
                    onClickSave = {

                        if(it.bean == null) {
                            recipesViewModel.displayError("원두는 반드시 선택해야 해요.")
                            return@RecipeSaveScreen
                        }

                        recipesViewModel.saveRecipe(it.mapToEntity())
                        var destination = CoffingNoteScreen.RecipeBeanList.name
                        if(recipesUiState.bean != null) {
                            destination = CoffingNoteScreen.RecipeList.name
                        }
                        navController.navigate(destination) {
                            popUpTo(destination) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(route = CoffingNoteScreen.RecipeDetail.name) {
                RecipeDetailScreen(
                    recipesUiState = recipesUiState,
                    onNavigateToUpdate = { navController.navigate(CoffingNoteScreen.RecipeUpdate.name) },
                    onClickDelete = {
                        recipesViewModel.deleteRecipe(it)
                        navController.navigate(CoffingNoteScreen.RecipeList.name) {
                            popUpTo(CoffingNoteScreen.RecipeList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(route = CoffingNoteScreen.RecipeUpdate.name) {
                RecipeUpdateScreen(
                    recipesUiState = recipesUiState,
                    onClickUpdate = {
                        recipesViewModel.updateRecipe(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.RecipeList.name) {
                            popUpTo(CoffingNoteScreen.RecipeList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            /** Bean Screen **/
            composable(route = CoffingNoteScreen.BeanList.name) {
                BeanListScreen(
                    beansUiState = beansUiState,
                    onNavigateToSave = { navController.navigate(CoffingNoteScreen.BeanSave.name) },
                    onNavigateToDetail = {
                        beansViewModel.getBean(it)
                        navController.navigate(CoffingNoteScreen.BeanDetail.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.BeanSave.name) {
                BeanSaveScreen(
                    beansUiState = beansUiState,
                    onValueChanged = { beansViewModel.valueChanged(it) },
                    onClickSave = {
                        beansViewModel.saveBean(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.BeanList.name) { popUpTo(0) }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                )
            }
            composable(route = CoffingNoteScreen.BeanDetail.name) {
                BeanDetailScreen(
                    beansUiState = beansUiState,
                    onNavigateToUpdate = { navController.navigate(CoffingNoteScreen.BeanUpdate.name) },
                    onClickDelete = {
                        beansViewModel.deleteBean(it)
                        navController.navigate(CoffingNoteScreen.BeanList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.BeanUpdate.name) {
                BeanUpdateScreen(
                    beansUiState = beansUiState,
                    onClickUpdate = {
                        beansViewModel.updateBean(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.BeanList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }


            /** HandMill Screen **/
            composable(route = CoffingNoteScreen.HandMillList.name) {
                HandMillListScreen(
                    handMillsUiState = handMillsUiState,
                    onNavigateToSave = { navController.navigate(CoffingNoteScreen.HandMillSave.name) },
                    onNavigateToDetail = {
                        handMillsViewModel.getHandMill(it)
                        navController.navigate(CoffingNoteScreen.HandMillDetail.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.HandMillSave.name) {
                HandMillSaveScreen(
                    onClickRegistration = {
                        handMillsViewModel.saveHandMill(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.HandMillList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.HandMillDetail.name) {
                HandMillDetailScreen(
                    handMillsUiState = handMillsUiState,
                    onNavigateToUpdate = { navController.navigate(CoffingNoteScreen.HandMillUpdate.name) },
                    onClickDelete = {
                        handMillsViewModel.deleteHandMill(it)
                        navController.navigate(CoffingNoteScreen.HandMillList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.HandMillUpdate.name) {
                HandMillUpdateScreen(
                    handMillsUiState = handMillsUiState,
                    onClickUpdate = {
                        handMillsViewModel.updateHandMill(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.HandMillList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }


            /** Dripper Screen **/
            composable(route = CoffingNoteScreen.DripperList.name) {
                DripperListScreen(
                    drippersUiState = drippersUiState,
                    onNavigateToSave = { navController.navigate(CoffingNoteScreen.DripperSave.name) },
                    onNavigateToDetail = {
                        drippersViewModel.getDripper(it)
                        navController.navigate(CoffingNoteScreen.DripperDetail.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.DripperSave.name) {
                DripperSaveScreen(
                    onClickRegistration = {
                        drippersViewModel.saveDripper(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.DripperList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.DripperDetail.name) {
                DripperDetailScreen(
                    drippersUiState = drippersUiState,
                    onNavigateToUpdate = { navController.navigate(CoffingNoteScreen.DripperUpdate.name) },
                    onClickDelete = {
                        drippersViewModel.deleteDripper(it)
                        navController.navigate(CoffingNoteScreen.DripperList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.DripperUpdate.name) {
                DripperUpdateScreen(
                    drippersUiState = drippersUiState,
                    onClickUpdate = {
                        drippersViewModel.updateDripper(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.DripperList.name) { popUpTo(0) }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            /** Origin Screen **/
            composable(route = CoffingNoteScreen.OriginList.name) {
                OriginListScreen(
                    originUiState = originUiState,
                    onNavigateToSave = { navController.navigate(CoffingNoteScreen.OriginSave.name) },
                    onNavigateToDetail = {
                        originViewModel.getOrigin(it)
                        navController.navigate(CoffingNoteScreen.OriginDetail.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.OriginSave.name) {
                OriginSaveScreen(
                    onClickRegistration = {
                        originViewModel.saveOrigin(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.OriginList.name) {
                            popUpTo(CoffingNoteScreen.OriginList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.OriginDetail.name) {
                OriginDetailScreen(
                    originUiState = originUiState,
                    onNavigateToUpdate = { navController.navigate(CoffingNoteScreen.OriginUpdate.name) },
                    onClickDelete = {
                        originViewModel.deleteOrigin(it)
                        navController.navigate(CoffingNoteScreen.OriginList.name) {
                            popUpTo(CoffingNoteScreen.OriginList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.OriginUpdate.name) {
                OriginUpdateScreen(
                    originUiState = originUiState,
                    onClickUpdate = {
                        originViewModel.updateOrigin(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.OriginList.name) {
                            popUpTo(CoffingNoteScreen.OriginList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }

            /** Roastery Screen **/
            composable(route = CoffingNoteScreen.RoasteryList.name) {
                RoasteryListScreen(
                    roasteryUiState = roasteryUiState,
                    onNavigateToSave = { navController.navigate(CoffingNoteScreen.RoasterySave.name) },
                    onNavigateToDetail = {
                        roasteryViewModel.getRoastery(it)
                        navController.navigate(CoffingNoteScreen.RoasteryDetail.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.RoasterySave.name) {
                RoasterySaveScreen(
                    onClickRegistration = {
                        roasteryViewModel.saveRoastery(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.RoasteryList.name) {
                            popUpTo(CoffingNoteScreen.RoasteryList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.RoasteryDetail.name) {
                RoasteryDetailScreen(
                    roasteryUiState = roasteryUiState,
                    onNavigateToUpdate = { navController.navigate(CoffingNoteScreen.RoasteryUpdate.name) },
                    onClickDelete = {
                        roasteryViewModel.deleteRoastery(it)
                        navController.navigate(CoffingNoteScreen.RoasteryList.name) {
                            popUpTo(CoffingNoteScreen.RoasteryList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.RoasteryUpdate.name) {
                RoasteryUpdateScreen(
                    roasteryUiState = roasteryUiState,
                    onClickUpdate = {
                        roasteryViewModel.updateRoastery(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.RoasteryList.name) {
                            popUpTo(CoffingNoteScreen.RoasteryList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }


            /** Water Screen **/
            composable(route = CoffingNoteScreen.WaterList.name) {
                WaterListScreen(
                    waterUiState = waterUiState,
                    onNavigateToSave = { navController.navigate(CoffingNoteScreen.WaterSave.name) },
                    onNavigateToDetail = {
                        waterViewModel.getWater(it)
                        navController.navigate(CoffingNoteScreen.WaterDetail.name)
                    }
                )
            }
            composable(route = CoffingNoteScreen.WaterSave.name) {
                WaterSaveScreen(
                    onClickRegistration = {
                        waterViewModel.saveWater(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.WaterList.name) {
                            popUpTo(CoffingNoteScreen.WaterList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.WaterDetail.name) {
                WaterDetailScreen(
                    waterUiState = waterUiState,
                    onNavigateToUpdate = { navController.navigate(CoffingNoteScreen.WaterUpdate.name) },
                    onClickDelete = {
                        waterViewModel.deleteWater(it)
                        navController.navigate(CoffingNoteScreen.WaterList.name) {
                            popUpTo(CoffingNoteScreen.WaterList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(route = CoffingNoteScreen.WaterUpdate.name) {
                WaterUpdateScreen(
                    waterUiState = waterUiState,
                    onClickUpdate = {
                        waterViewModel.updateWater(it.mapToEntity())
                        navController.navigate(CoffingNoteScreen.WaterList.name) {
                            popUpTo(CoffingNoteScreen.WaterList.name) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }


        }
    }
}