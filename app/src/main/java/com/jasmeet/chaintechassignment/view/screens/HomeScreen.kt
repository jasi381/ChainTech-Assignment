package com.jasmeet.chaintechassignment.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jasmeet.chaintechassignment.R
import com.jasmeet.chaintechassignment.model.data.AuthData
import com.jasmeet.chaintechassignment.utils.PasswordStrength
import com.jasmeet.chaintechassignment.utils.Utils
import com.jasmeet.chaintechassignment.view.appComponents.PasswordFieldComponent
import com.jasmeet.chaintechassignment.view.appComponents.PasswordStrengthIndicator
import com.jasmeet.chaintechassignment.view.appComponents.PasswordText
import com.jasmeet.chaintechassignment.view.appComponents.TextComponent
import com.jasmeet.chaintechassignment.view.appComponents.TextFieldComponent
import com.jasmeet.chaintechassignment.viewModel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(vm: AuthViewModel = hiltViewModel()) {

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var openDetailsBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedAuthData by rememberSaveable { mutableStateOf<AuthData?>(null) }


    val allData = vm.allAuthData.observeAsState(initial = emptyList())

    LaunchedEffect(true) {
        vm.getAllData()
    }


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            Modifier
                .background(Color(0xfff3f5fa))
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextComponent(
                text = "Password Manager",
                modifier = Modifier
                    .padding(start = 18.dp, bottom = 10.dp)
                    .statusBarsPadding(),
                fontWeight = FontWeight.SemiBold,
                textSize = 20.sp
            )
            HorizontalDivider(color = Color(0xffe8e8e8), thickness = 3.dp)

            AnimatedVisibility(visible = allData.value.isEmpty(), modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.weight(1f)) {
                    TextComponent(
                        text = "Add your password by clicking + button",
                        modifier = Modifier.align(Alignment.Center),
                        textSize = 22.sp
                    )
                }

            }

            AnimatedVisibility(
                visible = allData.value.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Box(Modifier.weight(1f)) {
                    LazyColumn(
                        Modifier
                            .padding(top = 15.dp)
                            .fillMaxSize()
                            .align(Alignment.TopCenter),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(allData.value) { authData ->

                            CardItem(authData = authData) {
                                selectedAuthData = it
                                openBottomSheet = false
                                openDetailsBottomSheet = true

                            }

                        }

                    }

                }


            }

            Box(Modifier.align(Alignment.End)) {
                FloatingActionButton(
                    onClick = { openBottomSheet = !openBottomSheet },
                    containerColor = Color(0xff3F7DE3),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 10.dp),
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )

                }

            }


        }
        AnimatedVisibility(
            openBottomSheet,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
        ) {

            var accountName by remember {
                mutableStateOf("")
            }
            var email by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }

            val strength = remember {
                mutableStateOf(Utils.getPasswordStrength(password))
            }

            LaunchedEffect(password) {
                strength.value = Utils.getPasswordStrength(password)

            }


            Box(
                Modifier
                    .clickable {
                        openBottomSheet = false
                    }
                    .fillMaxSize()
                    .background(Color(0x4D000000))
            ) {

                Column(
                    Modifier
                        .background(
                            Color(0xfff9f9f9),
                            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter)

                ) {

                    HorizontalDivider(
                        Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.CenterHorizontally)
                            .width(80.dp)
                            .clip(RoundedCornerShape(50)),
                        color = Color(0xffe3e3e3),
                        thickness = 5.dp
                    )

                    TextFieldComponent(
                        value = accountName,
                        onValueChange = { name ->
                            accountName = name
                        },
                        modifier = Modifier
                            .padding(start = 14.dp, end = 14.dp, top = 12.dp)
                            .fillMaxWidth(),
                        labelValue = "Account Name"
                    )
                    TextFieldComponent(
                        value = email,
                        onValueChange = { em ->
                            email = em
                        },
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                        labelValue = "UserName/ Email"
                    )
                    PasswordFieldComponent(
                        value = password,
                        onValueChange = { pass ->
                            password = pass

                        },
                        modifier = Modifier
                            .padding(start = 14.dp, end = 14.dp)
                            .fillMaxWidth(),
                        labelValue = "Password",
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()

                            }
                        )
                    )

                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            password = Utils.generateStrongPassword()

                        }) {
                        Text(text = "Generate a password")

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    AnimatedVisibility(visible = password.trim().isNotEmpty()) {
                        val text = "Your password strength is ${strength.value}"
                        val color = when (strength.value) {
                            PasswordStrength.WEAK -> Color.Red
                            PasswordStrength.MEDIUM -> Color.Blue
                            PasswordStrength.STRONG -> Color.Black
                        }

                        Column {
                            PasswordStrengthIndicator(strength = strength.value)
                            Spacer(modifier = Modifier.height(5.dp))
                            TextComponent(
                                text = text,
                                textSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 14.dp),
                                textColor = color
                            )

                        }


                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (accountName.trim().isEmpty() || email.trim()
                                    .isEmpty() || password.trim().isEmpty()
                            ) {
                                scope.launch {

                                    snackbarHostState.showSnackbar(
                                        message = "Please fill all the fields !",
                                        duration = SnackbarDuration.Short,
                                    )
                                }
                            } else {
                                vm.insert(
                                    AuthData(
                                        name = accountName,
                                        username = email,
                                        password = Utils.encrypt(password)
                                    )
                                )
                                vm.getAllData()
                                openBottomSheet = false
                            }

                        },
                        Modifier
                            .padding(horizontal = 14.dp)
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff2c2c2c)
                        )
                    ) {
                        TextComponent(
                            text = "Add New Account",
                            textColor = Color.White,
                            modifier = Modifier.padding(vertical = 6.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.captionBar))
                }


            }
        }

        AnimatedVisibility(
            visible = openDetailsBottomSheet,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
        ) {

            Box(
                Modifier
                    .clickable {
                        openDetailsBottomSheet = false
                    }
                    .fillMaxSize()
                    .background(Color(0x4D000000))) {

                Column(
                    Modifier
                        .background(
                            Color(0xfff9f9f9),
                            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter)

                ) {

                    HorizontalDivider(
                        Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.CenterHorizontally)
                            .width(80.dp)
                            .clip(RoundedCornerShape(50)),
                        color = Color(0xffe3e3e3),
                        thickness = 5.dp
                    )

                    TextComponent(
                        text = "Account Details",
                        modifier = Modifier
                            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 25.dp)
                            .fillMaxWidth(),
                        textColor = Color(0xff3F7DE3),
                        fontWeight = FontWeight.SemiBold
                    )

                    TextComponent(
                        text = "Account Type",
                        textColor = Color(0xffcccccc),
                        textSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    selectedAuthData?.let {
                        TextComponent(
                            text = it.name,
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .fillMaxWidth(),

                            )
                    }
                    Spacer(modifier = Modifier.height(15.dp))


                    TextComponent(
                        text = "UserName/ Email",
                        textColor = Color(0xffcccccc),
                        textSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    selectedAuthData?.let {
                        TextComponent(
                            text = it.username,
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .fillMaxWidth(),

                            )
                    }
                    Spacer(modifier = Modifier.height(15.dp))

                    TextComponent(
                        text = "Password",
                        textColor = Color(0xffcccccc),
                        textSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )

                    val passwordVisible = remember {
                        mutableStateOf(false)
                    }
                    val iconImage =
                        if (passwordVisible.value)
                            R.drawable.ic_show
                        else
                            R.drawable.ic_hide

                    Row(
                        Modifier
                            .padding(horizontal = 14.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        val orgPassword = selectedAuthData?.let { Utils.decrypt(it.password) }

                        if (!passwordVisible.value) {
                            orgPassword?.let { it1 ->
                                PasswordText(
                                    it1

                                )
                            }
                        } else {
                            orgPassword?.let {
                                TextComponent(
                                    text = it,
                                )
                            }
                        }


                        Icon(
                            imageVector = ImageVector.vectorResource(id = iconImage),
                            contentDescription = null,
                            tint = Color(0xffd7d7d7),
                            modifier = Modifier.clickable {
                                passwordVisible.value = !passwordVisible.value
                            }
                        )


                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                    ) {

                        Button(
                            onClick = {},
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff2c2c2c)
                            )
                        ) {
                            TextComponent(
                                text = "Edit",
                                textColor = Color.White,
                                modifier = Modifier.padding(vertical = 2.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }


                        Button(
                            onClick = {
                                vm.delete(selectedAuthData!!)
                                openDetailsBottomSheet = false
                                scope.launch {

                                    snackbarHostState.showSnackbar(
                                        message = "Password deleted successfully ",
                                        duration = SnackbarDuration.Short,
                                    )
                                }

                            },
                            modifier = Modifier
                                .padding(horizontal = 14.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xfff04646)
                            )
                        ) {
                            TextComponent(
                                text = "Delete",
                                textColor = Color.White,
                                modifier = Modifier.padding(vertical = 2.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.captionBar))
                }


            }

        }


    }
}

@Composable
fun CardItem(authData: AuthData, onclick: (AuthData) -> Unit) {
    Surface(
        border = BorderStroke(.8.dp, Color(0xffededed)),
        onClick = {
            onclick.invoke(authData)
        },
        color = Color.White,
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 5.dp

    ) {
        Row(
            Modifier
                .padding(vertical = 18.dp, horizontal = 14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            TextComponent(
                text = Utils.capitalizeFirstLetter(authData.name),
                fontWeight = FontWeight.SemiBold,
                textSize = 20.sp
            )

            Spacer(modifier = Modifier.width(15.dp))

            PasswordText(password = authData.password.take(8))
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null
            )


        }

    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}
