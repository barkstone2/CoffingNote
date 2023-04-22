package com.note.coffee.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.note.coffee.R
import com.note.coffee.ui.theme.Black
import com.note.coffee.ui.theme.Typography
import com.note.coffee.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> OutlinedSelectBox(
    items: List<T>,
    currentValue: String? = null,
    label: @Composable () -> Unit,
    onClick: (T) -> Unit,
    value: (T) -> String,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {

    var selectedItem by remember { mutableStateOf(currentValue ?: "") }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth(1f)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {
            var modifier = Modifier.fillMaxWidth(1f)
            if(!readOnly) {
                modifier = modifier.menuAnchor()
            }

            OutlinedTextField(
                value = selectedItem,
                onValueChange = { },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                label = label,
                modifier = modifier,
                readOnly = true
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach {
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = value(it)
                            onClick(it)
                            expanded = false
                        },
                        text = { Text(value(it)) },
                    )
                }
            }
        }
    }
}


@Composable
fun OutlinedText(
    label: String,
    outerPadding: PaddingValues? = null,
    innerPadding: PaddingValues? = null,
    value: @Composable () -> Unit,
) {

    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(outerPadding ?: PaddingValues())
    ) {
        // label
        Text(
            text = " $label ",
            color = Black,
            style = Typography.labelSmall,
            modifier = Modifier
                .zIndex(1f)
                .wrapContentHeight()
                .padding(start = 12.dp, top = 0.dp, bottom = 0.dp)
                .background(color = White)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height / 2) {
                        placeable.placeRelative(0, -placeable.height / 2)
                    }
                }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(BorderStroke(1.dp, Black), RoundedCornerShape(4.dp))
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                .padding(innerPadding ?: PaddingValues())
        ) {
            value()
        }
    }
}

@SuppressLint("VisibleForTests")
@Composable
fun BannerAdView() {
    val unitId = stringResource(id = R.string.ad_mob_banner_id)
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AndroidView(
            factory = { context ->

                AdView(context).apply {
                    setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidth))
                    adUnitId = unitId
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
