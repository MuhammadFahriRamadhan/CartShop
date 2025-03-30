package com.bankmandiri.cartshop.ui.product

import android.annotation.SuppressLint
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bankmandiri.cartshop.R
import com.bankmandiri.cartshop.core.domain.model.Product
import com.bankmandiri.cartshop.core.util.formatPrice
import com.bankmandiri.cartshop.ui.common.ProfileBottomSheet
import com.bankmandiri.cartshop.ui.main.MainViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: Product,navController : NavController) {
    val viewModel: MainViewModel = koinViewModel()
    LaunchedEffect(Unit) {
        viewModel.getProductCartList()
    }
    var quantity by remember { mutableStateOf(1) }
    val focusManager = LocalFocusManager.current
    val productCarts = viewModel.productCartLiveEvent.collectAsState(emptyList()).value
    var showSheet by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            TopAppBar(
                title = { Text("Cart Shop") },
                navigationIcon = {
                    IconButton(onClick = { backDispatcher?.onBackPressed() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!viewModel.islogin()) {
                        Button(
                            modifier = Modifier.padding(end = 16.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF2AB3A6)),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                navController.navigate("login")
                            }) {
                            Text("Login")
                        }
                    }else{
                        Row {
                            Box(
                                contentAlignment = Alignment.TopEnd
                            ) {
                                IconButton(onClick = {
                                    navController.navigate("cart")
                                } , modifier = Modifier.height(30.dp).width(30.dp)) {
                                    Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping", tint = Color(0xFF2AB3A6))
                                }
                               if (productCarts.isNotEmpty()) {
                                   val sumOfProduct = productCarts.mapNotNull { it?.quantity }.sum()
                                   Box(
                                       modifier = Modifier
                                           .offset(x = (12).dp, y = (-6).dp)
                                           .size(24.dp)
                                           .clip(CircleShape)
                                           .background(Color(0xFF1ABC9C)), // Green background
                                       contentAlignment = Alignment.Center
                                   ) {
                                       Text(
                                           text = sumOfProduct.toString(),
                                           color = Color.White,
                                           fontSize = 12.sp,
                                           fontWeight = FontWeight.Bold
                                       )
                                   }
                               }
                            }
                            Spacer(Modifier.width(25.dp))
                            IconButton(onClick = {
                                showSheet = true
                            }, modifier = Modifier.height(30.dp).width(30.dp)) {
                                Icon(Icons.Default.Person, contentDescription = "Person",tint = Color(0xFF2AB3A6))
                            }
                            Spacer(Modifier.width(16.dp))
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())) {
                // Product Image
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        modifier = Modifier.fillMaxWidth().heightIn( max = 350.dp),
                        painter = rememberAsyncImagePainter(product.image),
                        contentDescription = product.name,
                        contentScale = ContentScale.FillWidth
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Product Details
                Text(
                    text = product.category.orEmpty(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color(0xFF2AB3A6), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Product Name
                Text(
                    text = product.name.orEmpty(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Text(
                    text =  formatPrice(product.price),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2AB3A6)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = product.description.orEmpty(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Quantity and Button Section
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.wrapContentHeight().border(0.2.dp, Color.Gray, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentHeight() // Ensures Column takes only needed height
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(), // Ensures full width
                            horizontalArrangement = Arrangement.SpaceBetween, // Places elements at the ends
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Jumlah", fontWeight = FontWeight.Bold)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Minus Button with Border
                                IconButton(
                                    onClick = {
                                        if (quantity > 1) quantity--
                                        focusManager.clearFocus()
                                    },
                                    modifier = Modifier
                                        .size(15.dp)
                                        .border(0.1.dp, Color.Gray,shape = RoundedCornerShape(8.dp))
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.minus),
                                        contentDescription = "Decrease"
                                    )
                                }

                                Spacer( Modifier.width(5.dp) )

                                // Editable Quantity TextField with Border
                                OutlinedTextField(
                                    value = quantity.toString(),
                                    onValueChange = {
                                        val newValue = it.toIntOrNull() ?: quantity
                                        if (newValue > 0) quantity = newValue
                                    },
                                    modifier = Modifier.height(45.dp).width(80.dp),
                                    textStyle = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )

                                Spacer( Modifier.width(5.dp) )

                                // Plus Button with Border
                                IconButton(
                                    onClick = {
                                        quantity++
                                        focusManager.clearFocus()
                                    },
                                    modifier = Modifier
                                        .size(20.dp)
                                        .border(0.1.dp, Color.Gray,shape = RoundedCornerShape(8.dp))

                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }

                                Spacer( Modifier.width(8.dp) )
                            }
                        }
                        Spacer( modifier = Modifier.height(8.dp) )
                        Button(
                            onClick = {
                                if (viewModel.islogin()) {
                                    viewModel.updateProduct(product, quantity)
                                }else{
                                    navController.navigate("login")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF2AB3A6)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.wrapContentHeight().fillMaxWidth() // Ensure button does not force extra height
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tambahkan ke Keranjang")
                        }
                    }
                }
            }
        }
    )

    if (showSheet) {
        ProfileBottomSheet(viewModel.getUserName().toString(),onDismiss = { showSheet = false })
    }
}
