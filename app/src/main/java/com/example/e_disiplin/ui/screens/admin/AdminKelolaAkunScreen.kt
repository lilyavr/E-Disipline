package com.example.e_disiplin.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val BgCream = Color(0xFFF9F9F4)
private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val GoldAccent = Color(0xFFF2B705)
private val InputBorder = Color(0xFFE5E5EA)
private val ButtonActiveBg = Color(0xFF485885)

@Composable
fun AdminKelolaAkunScreen(
    viewModel: AdminKelolaAkunViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val adminData by viewModel.adminData.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Pre-fill form when data is loaded
    LaunchedEffect(adminData) {
        adminData?.let {
            name = it.name
            email = it.email
            username = it.username
            password = it.password
        }
    }

    // Success dialog handling
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(updateState) {
        if (updateState is KelolaAkunState.Success) {
            showSuccessDialog = true
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCream)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextNavy
                        )
                    }
                    Text(
                        text = "Kelola Akun Admin",
                        color = TextNavy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ubah data personal dan kredensial akun",
                    color = TextGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    if (adminData == null) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ButtonActiveBg)
                        }
                    } else {
                        // Nama Lengkap
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nama Lengkap", color = TextGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ButtonActiveBg,
                                unfocusedBorderColor = InputBorder
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = TextGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ButtonActiveBg,
                                unfocusedBorderColor = InputBorder
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Username
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username", color = TextGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ButtonActiveBg,
                                unfocusedBorderColor = InputBorder
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password", color = TextGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ButtonActiveBg,
                                unfocusedBorderColor = InputBorder
                            ),
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val iconText = if (passwordVisible) "🙈" else "👁️"
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Text(iconText, fontSize = 18.sp)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Error Message
                        if (updateState is KelolaAkunState.Error) {
                            Text(
                                text = (updateState as KelolaAkunState.Error).message,
                                color = Color(0xFFE95B5B),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        // Submit Button
                        Button(
                            onClick = {
                                viewModel.updateAdminData(name, email, username, password)
                            },
                            enabled = updateState !is KelolaAkunState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonActiveBg,
                                contentColor = Color.White
                            )
                        ) {
                            if (updateState is KelolaAkunState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Simpan Perubahan",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Text(text = "Sukses", fontWeight = FontWeight.Bold, color = TextNavy)
            },
            text = {
                Text(text = "Data admin berhasil diperbarui.", color = TextGray)
            },
            confirmButton = {
                TextButton(
                    onClick = { showSuccessDialog = false }
                ) {
                    Text("OK", color = ButtonActiveBg, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
