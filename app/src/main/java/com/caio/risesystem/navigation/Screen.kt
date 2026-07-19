package com.caio.risesystem.navigation

sealed class Screen(
    val route: String,
    val title: String
) {
    data object Home : Screen(
        route = "home",
        title = "Home"
    )

    data object Dashboard : Screen(
        route = "dashboard",
        title = "Dashboard"
    )

    data object Missions : Screen(
        route = "missions",
        title = "Missões"
    )

    data object Profile : Screen(
        route = "profile",
        title = "Perfil"
    )

    data object Validation : Screen(
        route = "validation/{missionId}",
        title = "Validação"
    ) {
        fun createRoute(missionId: Int): String {
            return "validation/$missionId"
        }
    }

    data object TimerValidation : Screen(
        route = "timer_validation/{missionId}",
        title = "Cronômetro"
    ) {
        fun createRoute(missionId: Int): String {
            return "timer_validation/$missionId"
        }
    }

    data object CameraValidation : Screen(
            route = "camera_validation/{missionId}",
            title = "Câmera"
        ) {
            fun createRoute(
                missionId: Int
            ): String {
                return "camera_validation/$missionId"
            }
        }

}

