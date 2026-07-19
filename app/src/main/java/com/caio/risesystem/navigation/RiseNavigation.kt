package com.caio.risesystem.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.caio.risesystem.RiseApplication
import com.caio.risesystem.domain.model.MissionStatus
import com.caio.risesystem.domain.model.ValidationType
import com.caio.risesystem.ui.dashboard.DashboardScreen
import com.caio.risesystem.ui.home.HomeScreen
import com.caio.risesystem.ui.missions.MissionsScreen
import com.caio.risesystem.ui.profile.ProfileScreen
import com.caio.risesystem.ui.validation.TimerValidationScreen
import com.caio.risesystem.ui.validation.ValidationSetupScreen
import com.caio.risesystem.ui.viewmodel.RiseViewModel
import com.caio.risesystem.ui.viewmodel.RiseViewModelFactory
import com.caio.risesystem.ui.validation.CameraValidationScreen

@Composable
fun RiseNavigation() {
    val application =
        LocalContext.current.applicationContext as RiseApplication

    val riseViewModel: RiseViewModel = viewModel(
        factory = RiseViewModelFactory(
            repository = application.repository
        )
    )

    val uiState by
    riseViewModel.uiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    val mainScreens = listOf(
        Screen.Home,
        Screen.Dashboard,
        Screen.Missions,
        Screen.Profile
    )

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    val showBottomBar =
        currentRoute != Screen.Validation.route &&
                currentRoute != Screen.TimerValidation.route &&
                currentRoute != Screen.CameraValidation.route

    Scaffold(
        containerColor = Color(0xFF020711),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF030A14)
                ) {
                    mainScreens.forEach { screen ->
                        val selected =
                            currentRoute == screen.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(
                                    screen.route
                                ) {
                                    popUpTo(
                                        navController.graph
                                            .findStartDestination()
                                            .id
                                    ) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (screen) {
                                        Screen.Home ->
                                            Icons.Default.Home

                                        Screen.Dashboard ->
                                            Icons.Default.BarChart

                                        Screen.Missions ->
                                            Icons.Default.Assignment

                                        Screen.Profile ->
                                            Icons.Default.Person

                                        else ->
                                            Icons.Default.Home
                                    },
                                    contentDescription =
                                        screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title
                                )
                            },
                            colors =
                                NavigationBarItemDefaults.colors(
                                    selectedIconColor =
                                        Color(0xFF2AF5F5),

                                    selectedTextColor =
                                        Color(0xFF2AF5F5),

                                    indicatorColor =
                                        Color(0xFF0A2931),

                                    unselectedIconColor =
                                        Color(0xFF667080),

                                    unselectedTextColor =
                                        Color(0xFF667080)
                                )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // HOME
            composable(
                route = Screen.Home.route
            ) {
                HomeScreen(
                    player = uiState.player,
                    missions = uiState.missions,
                    isMissionVisible =
                        uiState::isMissionVisible,
                    onOpenValidation = { missionId ->
                        navController.navigate(
                            Screen.Validation.createRoute(
                                missionId
                            )
                        )
                    },
                    onCompleteMission =
                        riseViewModel::completeMission
                )
            }

            // DASHBOARD
            composable(
                route = Screen.Dashboard.route
            ) {
                DashboardScreen(
                    player = uiState.player,
                    completedMissions =
                        uiState.completedMissions
                )
            }

            // MISSÕES
            composable(
                route = Screen.Missions.route
            ) {
                MissionsScreen()
            }

            // PERFIL
            composable(
                route = Screen.Profile.route
            ) {
                ProfileScreen(
                    player = uiState.player
                )
            }

            // PREPARAÇÃO DA VALIDAÇÃO
            composable(
                route = Screen.Validation.route,
                arguments = listOf(
                    navArgument("missionId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val missionId =
                    backStackEntry.arguments
                        ?.getInt("missionId")

                val mission =
                    uiState.missions.find {
                        it.id == missionId
                    }

                if (mission != null) {
                    ValidationSetupScreen(
                        mission = mission,
                        onBack = {
                            navController.popBackStack()
                        },
                        onStartValidation = {
                            when (mission.validationType) {
                                ValidationType.TIMER -> {
                                    navController.navigate(
                                        Screen.TimerValidation
                                            .createRoute(
                                                mission.id
                                            )
                                    )
                                }

                                ValidationType.CAMERA_REPS -> {
                                    navController.navigate(
                                        Screen.CameraValidation.createRoute(
                                            mission.id
                                        )
                                    )
                                }

                                ValidationType.GPS_DISTANCE -> {
                                    // Será implementado depois.
                                }

                                ValidationType.MANUAL -> {
                                    // Sem validação manual.
                                }
                            }
                        }
                    )
                }
            }

            // CRONÔMETRO
            composable(
                route = Screen.TimerValidation.route,
                arguments = listOf(
                    navArgument("missionId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val missionId =
                    backStackEntry.arguments
                        ?.getInt("missionId")

                val mission =
                    uiState.missions.find {
                        it.id == missionId
                    }

                if (mission != null) {
                    TimerValidationScreen(
                        mission = mission,
                        onMissionStarted = {
                            riseViewModel.startMission(
                                mission.id
                            )
                        },
                        onMissionCompleted = {
                            riseViewModel.completeMission(
                                mission.id
                            )
                        },
                        onCancel = {
                            if (
                                mission.status !=
                                MissionStatus.COMPLETED
                            ) {
                                riseViewModel.cancelMission(
                                    mission.id
                                )
                            }

                            navController.navigate(
                                Screen.Home.route
                            ) {
                                popUpTo(
                                    Screen.Home.route
                                ) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

            //Câmera
            composable(
                    route = Screen.CameraValidation.route,
            arguments = listOf(
                navArgument("missionId") {
                    type = NavType.IntType
                }
            )
            ) { backStackEntry ->

            val missionId =
                backStackEntry.arguments
                    ?.getInt("missionId")

            val mission =
                uiState.missions.find {
                    it.id == missionId
                }

            if (mission != null) {
                CameraValidationScreen(
                    mission = mission,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
        }
    }
}