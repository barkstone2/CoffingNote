package com.note.coffee.ui.water

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.note.coffee.data.dto.water.WaterRequest
import com.note.coffee.data.entity.water.Water
import com.note.coffee.ui.common.OutlinedText
import com.note.coffee.ui.theme.Black
import com.note.coffee.ui.theme.LightCoffee
import com.note.coffee.ui.theme.Typography


@Composable
fun WaterListScreen(
    waterUiState: WaterUiState,
    onNavigateToSave: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    Log.d("WaterListScreen", "start")

    Box(
        modifier = Modifier,
    ) {

        if(waterUiState.waters.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록된 물 정보가 없습니다.",
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                waterUiState.waters.forEach {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .clickable(onClick = { onNavigateToDetail(it.id) })
                                .border(
                                    border = BorderStroke(1.dp, Black),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp),

                            ) {
                                Column() {
                                    Text(
                                        text = it.name ?: "",
                                        style = Typography.titleMedium,
                                        modifier = Modifier.fillMaxWidth(1f)
                                            .padding(bottom = 6.dp)
                                    )
                                if(!it.comment.isNullOrEmpty()) {
                                    Text(
                                        text = "- \"${it.comment}\"",
                                        style = Typography.titleSmall,
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .padding(3.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.size(10.dp))
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(0.95f)
                .padding(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
        ) {
            SmallFloatingActionButton(
                onClick = onNavigateToSave,
                containerColor = LightCoffee,
            ) {
                Text(text = "+", fontSize = 20.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterSaveScreen(
    onClickRegistration: (WaterRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("WaterSaveScreen", "start")

    var request by remember { mutableStateOf(WaterRequest()) }

    val focusManager = LocalFocusManager.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp)
    ) {
        val (lazyColumn, buttonRow) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    bottom.linkTo(buttonRow.top)
                    top.linkTo(parent.top, margin = 0.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            OutlinedTextField(
                value = request.name ?: "",
                onValueChange = { request = request.copy(name = it) },
                label = { Text("물 이름") },
                modifier = Modifier
                    .fillMaxWidth(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )

            OutlinedTextField(
                value = request.comment ?: "",
                onValueChange = { request = request.copy(comment = it) },
                label = { Text("메모") },
                modifier = Modifier.fillMaxWidth(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )

        }


        // Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttonRow) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
            ,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onClickRegistration(request)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("등록")
                }
                Button(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("취소")
                }
            }
        }
    }
}

@Composable
fun WaterDetailScreen(
    waterUiState: WaterUiState,
    onNavigateToUpdate: () -> Unit,
    onClickDelete: (Water) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("WaterDetailScreen", "start")

    val water = waterUiState.water

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp)
    ) {
        val (lazyColumn, buttonRow) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    bottom.linkTo(buttonRow.top)
                    top.linkTo(parent.top, margin = 0.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            OutlinedText(
                label = "물 이름",
            ) {
                Text(
                    text = water?.name ?: "",
                    style = Typography.bodySmall,
                )
            }
            if(!water?.comment.isNullOrEmpty()) {
                OutlinedText(
                    label = "메모"
                ) {
                    Text(
                        text = "${water?.comment}",
                        style = Typography.bodySmall,
                    )
                }
            }
        }

        // Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttonRow) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
            ,
        ) {
            Button(
                onClick = { onNavigateToUpdate() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(3.dp)
            ) {
                Text("수정")
            }
            Button(
                onClick = { onClickDelete(water!!) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(3.dp)
            ) {
                Text("삭제")
            }
            Button(
                onClick = onNavigateBack,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(3.dp)
            ) {
                Text("취소")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterUpdateScreen(
    waterUiState: WaterUiState,
    onClickUpdate: (WaterRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("HandMillUpdateScreen", "start")

    var request by remember { mutableStateOf(WaterRequest(waterUiState.water!!)) }

    val focusManager = LocalFocusManager.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp)
    ) {
        val (lazyColumn, buttonRow) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    bottom.linkTo(buttonRow.top)
                    top.linkTo(parent.top, margin = 0.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            OutlinedTextField(
                value = request.name ?: "",
                onValueChange = { request = request.copy(name = it) },
                label = { Text("물 이름") },
                modifier = Modifier
                    .fillMaxWidth(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
            OutlinedTextField(
                value = request.comment ?: "",
                onValueChange = { request = request.copy(comment = it) },
                label = { Text("메모") },
                modifier = Modifier.fillMaxWidth(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
            )
        }

        // Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttonRow) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
            ,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onClickUpdate(request)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("저장")
                }
                Button(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("취소")
                }
            }
        }
    }
}