package com.example.helloapp


import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
        NavHost(navController, startDestination = NavRoutes.Home.route, modifier = Modifier.weight(1f)) {
            composable(NavRoutes.Home.route) {
                Home(name = name, onNameChange = { name = it })
            }
            composable(NavRoutes.Lists.route) { TrainListScreen() }
            composable(NavRoutes.Image.route) { ImageScreen()}
        }
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = navItem.image, contentDescription = navItem.title)
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Lists : NavRoutes("lists")
    object Image : NavRoutes("image")
}


data class Train(val destination: String, val number: String)

val trainList = listOf(
    Train("Москва", "123A"),
    Train("Санкт-Петербург", "456B"),
    Train("Казань", "789C"),
    Train("Новосибирск", "321D"),
    Train("Владивосток", "654E"),
    Train("Сочи", "987F"),
    Train("Екатеринбург", "432G"),
    Train("Челябинск", "876H"),
    Train("Самара", "543I"),
    Train("Уфа", "210J"),
    Train("Нижний Новгород", "654K"),
    Train("Ростов-на-Дону", "321L"),
    Train("Красноярск", "987M"),
    Train("Пермь", "543N"),
    Train("Омск", "210O"),
    Train("Воронеж", "876P"),
    Train("Волгоград", "432Q"),
    Train("Тюмень", "123R"),
    Train("Иркутск", "456S"),
    Train("Барнаул", "789T"),
    Train("Хабаровск", "654U"),
    Train("Мурманск", "321V"),
    Train("Астрахань", "987W"),
    Train("Тула", "543X"),
    Train("Калининград", "210Y")
)


@Composable
fun TrainListScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Пункт назначения",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trainList) { train ->
                TrainItem(train)
            }
        }
    }
}

@Composable
fun TrainItem(train: Train) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Пункт назначения: ${train.destination}", fontSize = 18.sp, color = Color.Black)
        Text(text = "Номер поезда: ${train.number}", fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
fun Home(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

@Composable
fun ImageScreen() {
    var rotationAngle by remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 600)
    )

    val configuration = LocalConfiguration.current

    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageContent(animatedRotation, onRotate = { rotationAngle += 180f })
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ImageContent(animatedRotation, onRotate = { rotationAngle += 180f })
        }
    }
}

@Composable
fun ImageContent(rotation: Float, onRotate: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.students),
        contentDescription = "Avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(16.dp)
            .size(150.dp)
            .clip(CircleShape)
            .rotate(rotation)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = { onRotate() },
        colors = ButtonDefaults.buttonColors(Color(0xFF6200EE))
    ) {
        Text(text = "Повернуть", color = Color.White)
    }
}

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: String
)

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Home",
            image = Icons.Filled.Home,
            route = "home"
        ),
        BarItem(
            title = "Lists",
            image = Icons.Filled.Menu,
            route = "lists"
        ),
        BarItem(
            title = "Image",
            image = Icons.Filled.Face,
            route = "image"
        )
    )
}

