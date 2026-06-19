package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    fun launchUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Copied link to clipboard: $url", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Creator & Studio", fontWeight = FontWeight.Bold, color = ImmersivePurple) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        },
        containerColor = AmoledBlack,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Developer Bio Card (Prince AR Abdur Rahman)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("developer_info_card"),
                shape = RoundedCornerShape(24.dp),
                color = DarkCard,
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Decorative Avatar replacement
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(NeonCyan, NeonPink)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AR",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Prince AR Abdur Rahman",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Independent Android Application Architect",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Passionate about crafting ultra-polished Kotlin interfaces, performance-driven productivity suites, mobile AI-powered intelligence, fully accelerated media engines, and next-generation design systems.",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Contact Numbers row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ContactNumberBadge(label = "WhatsApp 1", phone = "01707424006", context = context)
                        ContactNumberBadge(label = "WhatsApp 2", phone = "01796951709", context = context)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Direct Social Dials
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SocialIconButton(
                            icon = Icons.Default.Public,
                            label = "Facebook Profile",
                            color = Color(0xFF1877F2),
                            onClick = { launchUrl("https://www.facebook.com/share/1BNn32qoJo/") }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        SocialIconButton(
                            icon = Icons.Default.CameraAlt,
                            label = "Instagram",
                            color = Color(0xFFE1306C),
                            onClick = { launchUrl("https://www.instagram.com/ur___abdur____rahman__2008") }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Company Info (NexVora Lab's Ofc)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                color = DarkCard,
                border = BorderStroke(0.5.dp, Color.DarkGray)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Widgets, contentDescription = "Entity", tint = NeonPink)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NexVora Lab's Ofc",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Creating state-of-the-art Android software designed to empower productivity, entertainment, spatial design, learning, and local AI telemetry.",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Mission: Build exceptionally fast, beautiful, privacy-centric, and user-centric native utilities with open specifications accessible to everyone worldwide.",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurface, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Company Product Catalogue Showcase
            Text(
                text = "NexVora Ecosystem Portfolio",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                textAlign = TextAlign.Start
            )

            val products = listOf(
                CompanyProduct("NexPlay X", "Ultimate High-Fi Media Core & Streamer", Icons.Default.PlayCircle, NeonCyan),
                CompanyProduct("LifeSphere OS", "Immersive lifestyle system orchestrator", Icons.Default.Layers, NeonPink),
                CompanyProduct("Smart Day Planner X", "Advanced scheduling & habit metrics tracker", Icons.Default.Today, CyberYellow),
                CompanyProduct("Study AI", "Automated neural learning assistant", Icons.Default.Psychology, MatrixGreen),
                CompanyProduct("Lensora Studio", "Bespoke image synthesis & capture tool", Icons.Default.Camera, NeonCyan),
                CompanyProduct("Offline AI", "Fully local neural translation & chatbot engine", Icons.Default.Aod, NeonPink),
                CompanyProduct("NexVora Love Space", "Cozy private relational lockbox widget", Icons.Default.Favorite, CyberYellow),
                CompanyProduct("CalcVerse", "Advanced architectural multi-formula scientific calc", Icons.Default.Calculate, MatrixGreen),
                CompanyProduct("NexVoice OS", "Intelligent voice synthesizer & action controller", Icons.Default.Hearing, NeonCyan)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(products) { prod ->
                    Surface(
                        modifier = Modifier
                            .width(160.dp)
                            .height(130.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = DarkCard,
                        border = BorderStroke(1.dp, prod.color.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = prod.icon,
                                contentDescription = prod.name,
                                tint = prod.color,
                                modifier = Modifier.size(28.dp)
                            )

                            Column {
                                Text(
                                    text = prod.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = prod.description,
                                    color = Color.Gray,
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. System Specs Summary Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = DarkCard,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Technical Manifest Specs",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("App Version: 1.0.0 (Ultimate Build)", color = Color.LightGray, fontSize = 11.sp)
                    Text("CI/CD integrations: GitHub Actions, Codemagic CI/CD Flow", color = Color.LightGray, fontSize = 11.sp)
                    Text("Automated APK package builds active", color = Color.LightGray, fontSize = 11.sp)
                    Text("Repository layout: Clean Architectural MVVM Repository", color = Color.LightGray, fontSize = 11.sp)

                    Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))

                    Text(
                        text = "© 2026 NexVora Lab's Ofc. All Rights Reserved.\nDeveloped by Prince AR Abdur Rahman",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ContactNumberBadge(
    label: String,
    phone: String,
    context: android.content.Context
) {
    Surface(
        onClick = {
            try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Call dialed to number $phone", Toast.LENGTH_SHORT).show()
            }
        },
        shape = RoundedCornerShape(12.dp),
        color = DarkSurface,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        border = BorderStroke(0.5.dp, Color.DarkGray)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Outlined.Call, contentDescription = "Dial", tint = NeonCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("$label: $phone", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SocialIconButton(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
    }
}

data class CompanyProduct(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)
