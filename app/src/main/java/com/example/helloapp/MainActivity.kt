package com.example.helloapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           Main()
        }
    }
}

@Composable
fun Main() {

    val navController = rememberNavController()
    var name by remember { mutableStateOf("") }

    Column(Modifier.padding(8.dp)) {
        NavHost(navController, startDestination = NavRoutes.Home.route,
            modifier = Modifier.weight(1f)) {
            composable(NavRoutes.Home.route) {
                Home(name = name, onNameChange = { name = it })
            }
            composable(NavRoutes.Lists.route) { Lists() }
        }
        BottomNavigationBar(navController = navController)
    }

}
@Composable
fun  BottomNavigationBar(navController: NavController){
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute==navItem.route,
            onClick = {
                navController.navigate(navItem.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    {saveState = true }
                    launchSingleTop=true
                    restoreState=true
                }
            },
            icon = {
               Icon(imageVector = navItem.image,
                   contentDescription = navItem.title)
            },
            label = {
                Text(text=navItem.title)
            }

                )

        }
    }
}

sealed class NavRoutes(val route: String){
    object  Home: NavRoutes("home")
    object Lists: NavRoutes("lists")
}

@Composable
fun Lists(){
    Text("Lists Page", fontSize = 30.sp, modifier =  Modifier.padding(top = 20.dp, start = 2.dp))
}

@Composable
fun Home(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        var text = "Канаш Алексей Васильевич"

        Text(
            text = name, // Используем переданное состояние
            modifier = modifier,
            fontSize = 24.sp,
            color = Color.Blue
        )

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Button(
                onClick = { onNameChange(text) }, // Меняем состояние через переданную функцию
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF3CBD18)
                )
            ) {
                Text(text = "Вывести имя", fontSize = 24.sp, color = Color.Green)
            }
            Button(
                onClick = { onNameChange("") }, // Сбрасываем состояние через переданную функцию
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF3CBD18)
                )
            ) {
                Text(text = "X", fontSize = 24.sp, color = Color.Green)
            }
        }
    }
}

data class BarItem(
    val title : String,
    val image : ImageVector,
    val route : String
)

object NavBarItems{
    val BarItems= listOf(
        BarItem(
            title = "Home",
            image = Icons.Filled.Home,
            route="home"
        ),
        BarItem(
            title="Lists",
            image = Icons.Filled.Menu,
            route="lists"
        )
    )
}

