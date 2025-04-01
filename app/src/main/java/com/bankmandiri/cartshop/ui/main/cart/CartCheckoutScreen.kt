package com.bankmandiri.cartshop.ui.main.cart

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bankmandiri.cartshop.R
import com.bankmandiri.cartshop.core.di.viewModel
import com.bankmandiri.cartshop.core.domain.model.ProductCart
import com.bankmandiri.cartshop.core.local.ProductCartEntity
import com.bankmandiri.cartshop.core.util.formatPrice
import com.bankmandiri.cartshop.ui.common.ProfileBottomSheet
import com.bankmandiri.cartshop.ui.main.MainViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCheckoutScreen(navController : NavController) {
    val viewModel: MainViewModel = koinViewModel()
    LaunchedEffect(Unit) {
        viewModel.getProductCartList()
    }
    var productCartsData =  remember { mutableStateListOf<ProductCartEntity?>() }
    val focusManager = LocalFocusManager.current
    val quantities = remember { mutableStateMapOf<Int, Int>() }
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.productCartLiveEvent.collect{
            Log.i("EDEW", "CartCheckoutScreen: "+it.size)
            productCartsData.clear()
            productCartsData.addAll(it)
        }
    }

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

                                } , modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)) {
                                    Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping", tint = Color(0xFF2AB3A6))
                                }
                                if (productCartsData.isNotEmpty()) {
                                    Log.i("TAGED", "CartCheckoutScreen: ")
                                    val sumOfProduct = productCartsData.mapNotNull { it?.quantity }.sum()
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
                            }, modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Keranjang Belanja", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                productCartsData.forEach { productCart ->
                    quantities[productCart!!.id] = productCart.quantity ?: 1
                    val quantity = quantities[productCart.id] ?: 1
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 150.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 350.dp),
                                    painter = rememberAsyncImagePainter(productCart?.image),
                                    contentDescription = productCart?.name,
                                    contentScale = ContentScale.FillWidth
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    productCart?.name.orEmpty().take(20),
                                    fontWeight = FontWeight.Bold
                                )
                                Text("$ ${productCart?.price}", color = Color(0xFF00A86B), fontWeight = FontWeight.Bold)
                            }

                            Text(productCart?.category.orEmpty(), color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 16.dp)
                                ) {
                                    // Decrease Button
                                    IconButton(
                                        onClick = {
                                            if (quantity > 1) {
                                                focusManager.clearFocus()
                                                quantities[productCart.id] = quantity - 1
                                                viewModel.updateProductCart(productCart.apply {
                                                    this.quantity = quantities.filter { it.key == productCart.id }.values.first()
                                                })
                                            }
                                        },
                                        modifier = Modifier
                                            .size(15.dp)
                                            .border(
                                                0.1.dp,
                                                Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Icon(painterResource(id = R.drawable.minus), contentDescription = "Decrease")
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))

                                    // Editable Quantity TextField
                                    OutlinedTextField(
                                        value = quantity.toString(),
                                        onValueChange = { newValue ->
                                            newValue.toIntOrNull()?.let { newQuantity ->
                                                if (newQuantity > 0) {
                                                    quantities[productCart.id] = quantity + newQuantity
                                                    viewModel.updateProductCart(productCart)
                                                }
                                            }
                                        },
                                        readOnly = true,
                                        modifier = Modifier
                                            .height(45.dp)
                                            .width(80.dp),
                                        textStyle = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))

                                    // Increase Button

                                    IconButton (
                                        onClick = {
                                            focusManager.clearFocus()
                                            quantities[productCart.id] = quantity + 1
                                            viewModel.updateProductCart(productCart.apply {
                                                this.quantity = quantities.filter { it.key == productCart.id }.values.first()
                                            })
                                        },
                                        modifier = Modifier
                                            .size(20.dp)
                                            .border(
                                                0.1.dp,
                                                Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase")
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.deleteTransaction(productCart.id)
                                    },
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(60.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "delete",tint = Color.Red)
                                }
                            }


                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                if (productCartsData.isNotEmpty()) {
                    OrderSummary(productCartsData, viewModel, navController)
                }else{
                    EmptyCartScreenWithCard(navController)
                }
            }
        }
    )
    if (showSheet) {
        ProfileBottomSheet(viewModel.getUserName().toString(),onDismiss = {
            showSheet = false
            navController.navigate("home") {
                popUpTo("cart") { inclusive = true }
            }
        })
    }
}

@Composable
fun EmptyCartScreenWithCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Add padding for spacing
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // This centers content vertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Empty Cart Icon",
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Keranjang Kosong",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Keranjang belanja Anda masih kosong.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                navController.navigate("home")
            }, colors = ButtonDefaults.buttonColors(Color(0xFF2AB3A6))) {
                Text(text = "Mulai Belanja")
            }
        }
    }

}

@Composable
fun OrderSummary( productCart  : SnapshotStateList<ProductCartEntity?>,viewModel: MainViewModel,navController: NavController) {
    val subTotal = productCart.map { it?.quantity?.times((it.price ?: 0.0)) ?: 0.0 }.sum()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.isCheckoutSuccessEvent.collect{
            Toast.makeText(context, "Success Membeli Barang", Toast.LENGTH_SHORT).show()
           navController.navigate("home")
        }
    }
    Card(modifier = Modifier
        .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation( 4.dp),
        shape = RoundedCornerShape(6.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ringkasan Pesanan",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Subtotal")
                Text(text = formatPrice(subTotal))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatPrice(subTotal),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.checkoutTransaction(true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF2AB3A6))
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Checkout")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Checkout")
            }
        }
    }

}
