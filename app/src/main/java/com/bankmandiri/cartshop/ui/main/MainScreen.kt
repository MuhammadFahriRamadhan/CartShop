package com.bankmandiri.cartshop.ui.main

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.bankmandiri.cartshop.core.domain.model.Product
import com.bankmandiri.cartshop.core.local.ProductCartEntity
import com.bankmandiri.cartshop.core.util.NetworkConnectionManager
import com.bankmandiri.cartshop.core.util.formatPrice
import com.bankmandiri.cartshop.ui.common.ProfileBottomSheet
import com.bankmandiri.cartshop.ui.product.ProductDetailScreen
import com.bankmandiri.cartshop.ui.login.LoginScreen
import com.bankmandiri.cartshop.ui.main.cart.CartCheckoutScreen
import com.bankmandiri.cartshop.ui.theme.CartShopTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContent {
            CartShopTheme {
               AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { CartNinjaApp(navController) }
        composable("cart")  {  CartCheckoutScreen(navController)  }
        composable(
            "productDetail"
        ) {
            val product = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
            product?.let { ProductDetailScreen(it,navController) }
        }
        composable("login") {  LoginScreen(navController)  }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartNinjaApp(navController: NavController) {
    var networkConnectionManager: NetworkConnectionManager? = null
    val viewModel: MainViewModel = koinViewModel()
    var showSheet by remember { mutableStateOf(false) }
    var context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.getProductCartList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkConnectionManager = NetworkConnectionManager(context)
            networkConnectionManager?.isConnected?.observe(lifecycleOwner) { isConnected ->
                if (isConnected) {
                    viewModel.getProducts()
                } else {
                }
            }
        }
    }
    var productCarts = viewModel.productCartLiveEvent.collectAsState(emptyList()).value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart Shop") },
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
            HomeScreen(viewModel,paddingValues,navController)
        }
    )

    if (showSheet) {
        ProfileBottomSheet(viewModel.getUserName().toString(),onDismiss = {
            showSheet = false
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        })
    }
}


@Composable
fun HomeScreen(viewModel: MainViewModel,paddingValues: PaddingValues,navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }
    val categories = viewModel.productsLiveEvent.collectAsState(emptyList()).value?.map { it.category }?.distinct()
    val products = viewModel.productsLiveEvent.collectAsState(emptyList()).value?.filter { it.name?.isNotEmpty() == true }
    val isLoading = viewModel.isLoadingLiveData.asFlow().collectAsState(false).value
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    // Auto-select first category when categories are available
    LaunchedEffect(categories) {
        if (!categories.isNullOrEmpty() && selectedCategory == null) {
            selectedCategory = categories.first()
        }
    }
    val filteredProducts = if (selectedCategory == "Semua") {
        products
    } else {
        products?.filter { it.category == selectedCategory }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading == true) {
            CircularProgressIndicator() // ✅ Correct place to show loading
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CategoryChips(categories,selectedCategory.toString()) { category ->
                    selectedCategory = category
                }
                ProductList(filteredProducts,selectedCategory.toString(),navController)
            }
        }
    }
}

@Composable
fun CategoryChips(
    categories: List<String?>?,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Kategori",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            categories?.forEach { category ->
                CategoryChip(
                    text = category.orEmpty(),
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category.orEmpty()) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF2AB3A6) else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black
    val borderColor = if (isSelected) Color.Transparent else Color.LightGray

    Box( // ✅ Wrap in Box to control background without affecting the button
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(2.dp) // Small padding to avoid clipping
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.height(40.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor) // ✅ Set text color
        ) {
            Text(text = text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProductList(products : List<Product>?,selectedProduct : String,navController: NavController){
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (selectedProduct == "Semua") "$selectedProduct Produk" else "Produk $selectedProduct",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(products?.isNotEmpty() == true) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp), // Adaptive column size
            ) {
                items(products) { product ->
                    ProductCard(product,navController)
                }
            }
        }else{
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation( 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Product Empty....",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun ProductCard(product: Product,navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                navController.navigate("productDetail")
            },
        elevation = CardDefaults.cardElevation( 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name.orEmpty(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = formatPrice(product.price),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2AB3A6),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CartShopTheme {
        AppNavigation()
    }
}