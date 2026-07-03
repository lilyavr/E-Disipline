package com.example.e_disiplin.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_disiplin.R

// Colors
private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val GoldAccent = Color(0xFFF2B705)
private val InputBorder = Color(0xFFE5E5EA)
private val ButtonActiveBg = Color(0xFF485885)

@Composable
fun AdminLoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateForgotPassword: () -> Unit,
    viewModel: AdminLoginViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("AuthPrefs", android.content.Context.MODE_PRIVATE) }

    LaunchedEffect(Unit) {
        if (sharedPrefs.getBoolean("rememberMe", false) && sharedPrefs.getString("role", "") == "admin") {
            username = sharedPrefs.getString("username", "") ?: ""
            password = sharedPrefs.getString("password", "") ?: ""
            rememberMe = true
        }
    }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            val adminId = (loginState as LoginState.Success).adminId
            viewModel.resetState()
            
            // Always save adminId and role for the active session
            val editor = sharedPrefs.edit()
            editor.putString("adminId", adminId)
            editor.putString("role", "admin")

            if (rememberMe) {
                editor.putBoolean("rememberMe", true)
                editor.putString("username", username)
                editor.putString("password", password)
            } else {
                editor.remove("rememberMe")
                editor.remove("username")
                editor.remove("password")
            }
            editor.apply()
            
            onLoginSuccess()
        }
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgCream)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                // TOP HERO SECTION
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.45f) // Take up 45% of the screen
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    // Background Asset
                    Image(
                        painter = painterResource(id = R.drawable.bg_select_role),
                        contentDescription = "Background Admin",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Hero Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Bar (Back Button)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                                    .clickable { onNavigateBack() }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Kembali",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Kembali", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Logo Circle
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .border(2.dp, Color(0xFF5D6B9B), CircleShape)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_onboarding_bear),
                                contentDescription = "Logo",
                                modifier = Modifier.size(56.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // E-DISIPLIN Text
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = GoldAccent)) {
                                    append("E-")
                                }
                                withStyle(style = SpanStyle(color = Color.White)) {
                                    append("DISIPLIN")
                                }
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Portal Admin Subtitle
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Portal Admin",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.weight(1.5f))
                        
                        // Selamat Datang
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Selamat Datang",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Masuk dengan username dan password Anda",
                                color = Color(0xFFD9D9E3),
                                fontSize = 14.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }

                // BOTTOM CONTENT SECTION
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = screenHeight * 0.55f)
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    // Username Label
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = TextNavy, fontWeight = FontWeight.Bold)) {
                                append("Username ")
                            }
                            withStyle(style = SpanStyle(color = Color(0xFFE95B5B))) {
                                append("*")
                            }
                        },
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Username Input
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan username", color = TextGray) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ButtonActiveBg,
                            unfocusedBorderColor = InputBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = TextNavy,
                            unfocusedTextColor = TextNavy
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Password Label
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = TextNavy, fontWeight = FontWeight.Bold)) {
                                append("Password ")
                            }
                            withStyle(style = SpanStyle(color = Color(0xFFE95B5B))) {
                                append("*")
                            }
                        },
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Masukkan password", color = TextGray) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ButtonActiveBg,
                            unfocusedBorderColor = InputBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = TextNavy,
                            unfocusedTextColor = TextNavy
                        ),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val text = if (passwordVisible) "🙈" else "👁️"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(text, fontSize = 18.sp)
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Remember Me and Forgot Password Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = ButtonActiveBg,
                                    uncheckedColor = InputBorder
                                ),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingat Saya", color = TextNavy, fontSize = 14.sp)
                        }
                        
                        Text(
                            text = "Lupa Password?",
                            color = TextNavy,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateForgotPassword() }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Error Message
                    if (loginState is LoginState.Error) {
                        Text(
                            text = (loginState as LoginState.Error).message,
                            color = Color(0xFFE95B5B),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // CTA Button
                    Button(
                        onClick = {
                            viewModel.login(username, password)
                        },
                        enabled = loginState !is LoginState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonActiveBg,
                            contentColor = Color.White
                        )
                    ) {
                        if (loginState is LoginState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Masuk sebagai Admin",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Security Note
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("🛡️", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Koneksi terenkripsi & aman",
                            color = Color(0xFFA0A3B1),
                            fontSize = 12.sp
                        )
                    }
                    
                    // Extra padding for the bottom of the scroll
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdminLoginScreen() {
    AdminLoginScreen(
        onLoginSuccess = {},
        onNavigateBack = {},
        onNavigateForgotPassword = {}
    )
}
