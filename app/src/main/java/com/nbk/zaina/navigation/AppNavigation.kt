package com.nbk.rise.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.ui.screens.auth.LoginScreen
import com.nbk.rise.ui.screens.events.EventDetailScreen
import com.nbk.rise.ui.screens.resources.ResourceDetailScreen
import com.nbk.rise.ui.screens.directory.ProfileDetailScreen
import com.nbk.rise.ui.screens.main.MainScreen
import com.nbk.rise.ui.screens.messages.ChatScreen
import com.nbk.rise.ui.screens.notifications.NotificationsScreen
import com.nbk.rise.viewmodels.AuthViewModel
import com.nbk.rise.ui.screens.profile.MyProfileScreen
import com.nbk.rise.ui.screens.profile.ViewProfileScreen
import com.nbk.zaina.ui.screens.auth.GuestIntroScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authUiState by authViewModel.uiState
    
    NavHost(
        navController = navController,
        startDestination = if (authUiState.isAuthenticated) {
            Screen.Main.route
        } else {
            Screen.Login.route
        }
    ) {
        // Authentication
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { userRole ->
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onExploreAsGuest = {
                    navController.navigate(Screen.GuestIntro.route)
                }
            )
        }

        // Guest Intro
        composable(Screen.GuestIntro.route) {
            GuestIntroScreen(
                onFinish = {
                    navController.popBackStack() // Or navigate elsewhere if needed
                }
            )
        }
        
        // Main Screen with Bottom Navigation (handles dashboard screens internally)
        composable(Screen.Main.route) {
            MainScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        
        // Full-screen detail screens
        composable(
            route = Screen.EventDetail.route,
            arguments = Screen.EventDetail.arguments
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventDetailScreen(
                eventId = eventId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.ResourceDetail.route,
            arguments = Screen.ResourceDetail.arguments
        ) { backStackEntry ->
            val resourceId = backStackEntry.arguments?.getString("resourceId") ?: ""
            ResourceDetailScreen(
                resourceId = resourceId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Profile screens
        composable(Screen.MyProfile.route) {
            MyProfileScreen()
        }
        
        composable(
            route = Screen.ViewProfile.route,
            arguments = Screen.ViewProfile.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ViewProfileScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = { otherUserId ->
                    navController.navigate(Screen.Chat.createRoute(otherUserId))
                }
            )
        }
        
        // Chat screen (full-screen)
        composable(
            route = Screen.Chat.route,
            arguments = Screen.Chat.arguments
        ) { backStackEntry ->
            val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
            ChatScreen(
                otherUserId = otherUserId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Notifications screen (full-screen)
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onProfileClick = { userId ->
                    navController.navigate(Screen.ViewProfile.createRoute(userId))
                }
            )
        }
    }
}

private fun getStartDestinationForRole(role: UserRole?): String {
    return when (role) {
        UserRole.APPLICANT -> Screen.ApplicantDashboard.route
        UserRole.PARTICIPANT -> Screen.ParticipantDashboard.route
        UserRole.ALUMNA -> Screen.AlumnaDashboard.route
        UserRole.MENTOR -> Screen.MentorDashboard.route
        null -> Screen.Login.route
    }
}