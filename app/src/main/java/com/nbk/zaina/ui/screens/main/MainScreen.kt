package com.nbk.rise.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nbk.rise.data.dtos.UserRole
import com.nbk.rise.navigation.Screen
import com.nbk.rise.ui.screens.dashboard.ApplicantDashboardScreen
import com.nbk.rise.ui.screens.dashboard.ParticipantDashboardScreen
import com.nbk.rise.ui.screens.dashboard.AlumnaDashboardScreen
import com.nbk.rise.ui.screens.dashboard.MentorDashboardScreen
import com.nbk.rise.ui.screens.events.EventsScreen
import com.nbk.rise.ui.screens.resources.ResourcesScreen
import com.nbk.rise.ui.screens.directory.DirectoryScreen
import com.nbk.rise.ui.screens.messages.MessagesScreen
import com.nbk.rise.ui.theme.PrimaryColor
import com.nbk.rise.ui.theme.TextMuted
import com.nbk.rise.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authUiState by authViewModel.uiState
    val userRole = authUiState.userRole
    
    // Create a separate nav controller for bottom navigation
    val bottomNavController = rememberNavController()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text(
                            text = "RISE",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "| ${userRole?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: ""}",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextMuted
                        )
                    }
                },
                actions = {
                    // Notifications Bell
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Notifications.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = PrimaryColor
                        )
                    }
                    
                    // Settings/Logout
                    IconButton(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Logout",
                            tint = PrimaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = bottomNavController,
                parentNavController = navController,
                userRole = userRole
            )
        }
    ) { paddingValues ->
        // Bottom navigation content
        NavHost(
            navController = bottomNavController,
            startDestination = getStartDestinationForRole(userRole),
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.ApplicantDashboard.route) {
                ApplicantDashboardScreen(
                    onNavigateToEvents = { 
                        bottomNavController.navigate(Screen.Events.route) 
                    },
                    onNavigateToResources = { 
                        bottomNavController.navigate(Screen.Resources.route) 
                    }
                )
            }
            
            composable(Screen.ParticipantDashboard.route) {
                ParticipantDashboardScreen(
                    onNavigateToEvents = { 
                        bottomNavController.navigate(Screen.Events.route) 
                    },
                    onNavigateToResources = { 
                        bottomNavController.navigate(Screen.Resources.route) 
                    },
                    onNavigateToDirectory = { 
                        bottomNavController.navigate(Screen.Directory.route) 
                    }
                )
            }
            
            composable(Screen.AlumnaDashboard.route) {
                AlumnaDashboardScreen(
                    onNavigateToEvents = { 
                        bottomNavController.navigate(Screen.Events.route) 
                    },
                    onNavigateToResources = { 
                        bottomNavController.navigate(Screen.Resources.route) 
                    },
                    onNavigateToDirectory = { 
                        bottomNavController.navigate(Screen.Directory.route) 
                    }
                )
            }

            composable(Screen.MentorDashboard.route) {
                MentorDashboardScreen(
                    onNavigateToEvents = { 
                        bottomNavController.navigate(Screen.Events.route) 
                    },
                    onNavigateToResources = { 
                        bottomNavController.navigate(Screen.Resources.route) 
                    },
                    onNavigateToDirectory = { 
                        bottomNavController.navigate(Screen.Directory.route) 
                    },
                    onNavigateToMessages = {
                        bottomNavController.navigate(Screen.Messages.route)
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Screen.Notifications.route)
                    }
                )
            }
            
            composable(Screen.Events.route) {
                EventsScreen(
                    onEventClick = { eventId ->
                        navController.navigate(Screen.EventDetail.createRoute(eventId))
                    }
                )
            }
            
            composable(Screen.Resources.route) {
                ResourcesScreen(
                    onResourceClick = { resourceId ->
                        navController.navigate(Screen.ResourceDetail.createRoute(resourceId))
                    }
                )
            }
            
            composable(Screen.Directory.route) {
                DirectoryScreen(
                    onProfileClick = { userId ->
                        navController.navigate(Screen.ViewProfile.createRoute(userId))
                    }
                )
            }
            
            composable(Screen.Messages.route) {
                MessagesScreen(
                    onConversationClick = { otherUserId ->
                        navController.navigate(Screen.Chat.createRoute(otherUserId))
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    parentNavController: NavHostController,
    userRole: UserRole?
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Define navigation items based on user role
    val navigationItems = when (userRole) {
        UserRole.APPLICANT -> listOf(
            BottomNavItem("Dashboard", Icons.Default.Dashboard, Screen.ApplicantDashboard.route),
            BottomNavItem("Events", Icons.Default.Event, Screen.Events.route),
            BottomNavItem("Resources", Icons.Default.Folder, Screen.Resources.route),
            BottomNavItem("Directory", Icons.Default.People, Screen.Directory.route)
        )
        UserRole.PARTICIPANT -> listOf(
            BottomNavItem("Dashboard", Icons.Default.Dashboard, Screen.ParticipantDashboard.route),
            BottomNavItem("Events", Icons.Default.Event, Screen.Events.route),
            BottomNavItem("Resources", Icons.Default.Folder, Screen.Resources.route),
            BottomNavItem("Directory", Icons.Default.People, Screen.Directory.route),
            BottomNavItem("Messages", Icons.Default.Message, Screen.Messages.route)
        )
        UserRole.ALUMNA -> listOf(
            BottomNavItem("Dashboard", Icons.Default.Dashboard, Screen.AlumnaDashboard.route),
            BottomNavItem("Events", Icons.Default.Event, Screen.Events.route),
            BottomNavItem("Resources", Icons.Default.Folder, Screen.Resources.route),
            BottomNavItem("Directory", Icons.Default.People, Screen.Directory.route),
            BottomNavItem("Messages", Icons.Default.Message, Screen.Messages.route)
        )
        UserRole.MENTOR -> listOf(
            BottomNavItem("Dashboard", Icons.Default.Dashboard, Screen.MentorDashboard.route),
            BottomNavItem("Events", Icons.Default.Event, Screen.Events.route),
            BottomNavItem("Resources", Icons.Default.Folder, Screen.Resources.route),
            BottomNavItem("Directory", Icons.Default.People, Screen.Directory.route),
            BottomNavItem("Messages", Icons.Default.Message, Screen.Messages.route)
        )
        null -> emptyList()
    }
    
    if (navigationItems.isNotEmpty()) {
        NavigationBar(
            containerColor = PrimaryColor.copy(alpha = 0.05f)
        ) {
            navigationItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryColor,
                        selectedTextColor = PrimaryColor,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted,
                        indicatorColor = PrimaryColor.copy(alpha = 0.1f)
                    )
                )
            }
        }
    }
}

private fun getStartDestinationForRole(role: UserRole?): String {
    return when (role) {
        UserRole.APPLICANT -> Screen.ApplicantDashboard.route
        UserRole.PARTICIPANT -> Screen.ParticipantDashboard.route
        UserRole.ALUMNA -> Screen.AlumnaDashboard.route
        UserRole.MENTOR -> Screen.MentorDashboard.route
        null -> Screen.ApplicantDashboard.route // Default fallback
    }
}

data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
) 