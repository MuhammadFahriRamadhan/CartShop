package com.bankmandiri.cartshop.core.util

import android.graphics.Color
import android.view.View
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.android.material.snackbar.Snackbar

@Composable
fun ShowSnackBar(snackbarHostState: SnackbarHostState, title: String, statusColor: Color) {
    LaunchedEffect(title) {
        snackbarHostState.showSnackbar(
            message = title,
            duration = SnackbarDuration.Long
        )
    }
}