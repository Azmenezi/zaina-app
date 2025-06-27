package com.nbk.rise.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    // Authentication
    object Login : Screen("login")

    // Guest Intro
    object GuestIntro : Screen("guest_intro")

    // Guest Registration
    object Register : Screen("register")

    // Main Screen with Bottom Navigation
    object Main : Screen("main")
    
    // Dashboard Screens (role-specific)
    object ApplicantDashboard : Screen("applicant_dashboard")
    object ParticipantDashboard : Screen("participant_dashboard")
    object AlumnaDashboard : Screen("alumna_dashboard")
    object MentorDashboard : Screen("mentor_dashboard")
    
    // Events
    object Events : Screen("events")
    object EventDetail : Screen(
        route = "event_detail/{eventId}",
        arguments = listOf(
            navArgument("eventId") { type = NavType.StringType }
        )
    ) {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }
    
    // Resources
    object Resources : Screen("resources")
    object ResourceDetail : Screen(
        route = "resource_detail/{resourceId}",
        arguments = listOf(
            navArgument("resourceId") { type = NavType.StringType }
        )
    ) {
        fun createRoute(resourceId: String) = "resource_detail/$resourceId"
    }
    
    // Directory
    object Directory : Screen("directory")
    
    // Profile
    object MyProfile : Screen("profile/me")
    object ViewProfile : Screen(
        route = "profile/{userId}",
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    ) {
        fun createRoute(userId: String) = "profile/$userId"
    }
    
    // Messages
    object Messages : Screen("messages")
    object Chat : Screen(
        route = "chat/{otherUserId}",
        arguments = listOf(
            navArgument("otherUserId") { type = NavType.StringType }
        )
    ) {
        fun createRoute(otherUserId: String) = "chat/$otherUserId"
    }
    
    // Notifications
    object Notifications : Screen("notifications")
    
    // Settings
    object Settings : Screen("settings")
}
