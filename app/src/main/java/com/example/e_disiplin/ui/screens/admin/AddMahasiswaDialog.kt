package com.example.e_disiplin.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private val TextNavy = Color(0xFF1B2154)
private val TextGray = Color(0xFF8A8D9E)
private val ButtonActiveBg = Color(0xFF2A3F7B)
private val CardBorder = Color(0xFFE5E5EA)

@Composable
fun AddMahasiswaDialog(
    viewModel: AdminMahasiswaViewModel,
    onDismissRequest: () -> Unit
) {
    var nim by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }

    val addState by viewModel.addState.collectAsState()

    LaunchedEffect(addState) {
        if (addState is AddMahasiswaState.Success) {
            viewModel.resetAddState()
            onDismissRequest()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = "Tambah Mahasiswa",
                    color = TextNavy,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(value = nim, onValueChange = { nim = it }, label = "NIM")
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = name, onValueChange = { name = it }, label = "Nama Lengkap")
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = email, onValueChange = { email = it }, label = "Email")
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = noHp, onValueChange = { noHp = it }, label = "No HP")
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = major, onValueChange = { major = it }, label = "Program Studi")
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = semester, onValueChange = { semester = it }, label = "Semester")
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = tanggalLahir, onValueChange = { tanggalLahir = it }, label = "Tanggal Lahir (DDMMYYYY)")

                Spacer(modifier = Modifier.height(16.dp))

                if (addState is AddMahasiswaState.Error) {
                    Text(
                        text = (addState as AddMahasiswaState.Error).message,
                        color = Color(0xFFE95B5B),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextNavy),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { viewModel.addMahasiswa(nim, name, email, noHp, major, semester, tanggalLahir) },
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonActiveBg),
                        enabled = addState !is AddMahasiswaState.Loading
                    ) {
                        if (addState is AddMahasiswaState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Simpan")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = TextGray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = ButtonActiveBg,
            unfocusedBorderColor = CardBorder,
            focusedTextColor = TextNavy,
            unfocusedTextColor = TextNavy
        ),
        singleLine = true
    )
}
