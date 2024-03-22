package com.learn.splashlearn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun MainContent() {
    val navController = Navigation.navController
    var sidebarVisible by remember { mutableStateOf(false) }
        Box() {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "Background",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 25.dp, vertical = 10.dp)
                ) {
                    IconButton(
                        onClick = { sidebarVisible = !sidebarVisible },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_menu_24),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize()
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Text(text = "Hi", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        Text(text = "John!", fontSize = 30.sp)
                    }
                }
                Divider(color = Color.Black, thickness = 2.dp)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 4.dp)
                ) {
                    ElevatedButton(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text(text = "Plumber", color = Color.Black)
                    }
                    ElevatedButton(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text(text = "Tailor", color = Color.Black)
                    }
                    ElevatedButton(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        )
                    ) {
                        Text(text = "Carpenter", color = Color.Black)
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1000.dp)
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(5) { _ ->
                        Box(
                            modifier = Modifier
                                .coloredShadow(
                                    color = Color.Black,
                                    alpha = 0.8f,
                                    borderRadius = 20.dp,
                                    shadowRadius = 5.dp,
                                    offsetY = 5.dp
                                )
                                .clip(RoundedCornerShape(15.dp))
                                .size(400.dp, 200.dp)
                                .padding(5.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.wood),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .fillMaxSize()

                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(15.dp,),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(20.dp)
                                    .align(Alignment.BottomStart)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.hannahnelson),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .coloredShadow(
                                            color = Color.Black,
                                            alpha = 0.8f,
                                            shadowRadius = 20.dp,
                                            offsetY = 10.dp,
                                            offsetX = 6.dp
                                        )
                                        .clip(CircleShape)
                                        .width(60.dp)
                                        .height(60.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier
                                        .coloredShadow(
                                            color = Color.Black,
                                            alpha = 0.8f,
                                            shadowRadius = 20.dp,
                                            offsetY = 10.dp,
                                            offsetX = 6.dp
                                        )
                                        .clip(RoundedCornerShape(10.dp))
                                        .size(350.dp, 60.dp)
                                        .background(Color.White)
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Hannah Nelson",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text("+254712345678")
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Tailor"
                                        )
                                        Row() {
                                            for (i in 1..5) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = "Review star"
                                                )
                                            }
                                        }
                                        Text("115 reviews")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    if(sidebarVisible){
        Sidebar(navController = navController, onClose = { sidebarVisible = false })
    }

}

@Composable
fun Sidebar(navController: NavController, onClose: () -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
                .background(Color.White)
        ) {
            ProfileSection()
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            MenuItem(icon = Icons.Default.Notifications, text = "Assigned Jobs")
            MenuItem(icon = Icons.Default.Star, text = "Reviews")
            Spacer(modifier = Modifier.weight(1f))
            Divider(color = Color.LightGray, thickness = 1.dp)
            MenuItemClickable(icon = Icons.Default.ExitToApp, text = "Logout") {
                navController.navigate("home_Screen")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .align(Alignment.CenterEnd)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startX = 16f,
                        endX = 0f
                    )
                )
        )
    }
    CloseButton(onClose)

}

@Composable
fun ProfileSection() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_1),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "John", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(text = "View Profile", textAlign = TextAlign.Center, color = Color.Gray)
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}
@Composable
fun MenuItemClickable(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}

@Composable
fun CloseButton(onClose: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth( fraction = 0.7f)
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Gray
            )
        }
    }
}







fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = composed {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha= 0f).toArgb()
    this.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun contPrev() {
    MainContent()
}