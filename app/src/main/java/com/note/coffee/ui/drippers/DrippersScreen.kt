package com.note.coffee.ui.drippers

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.note.coffee.data.dto.drippers.DripperRequest
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.ui.common.OutlinedText
import com.note.coffee.ui.theme.Black
import com.note.coffee.ui.theme.LightCoffee
import com.note.coffee.ui.theme.Typography
import kotlinx.coroutines.delay


@Composable
fun DripperListScreen(
    drippersUiState: DrippersUiState,
    onNavigateToSave: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onClickReorder: (Int, Int) -> Unit,
) {
    Log.d("DripperListScreen", "start")

    Box(
        modifier = Modifier,
    ) {

        if(drippersUiState.drippers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록된 드리퍼가 없습니다.",
                )
            }
        } else {
            var reorderedId by remember { mutableStateOf(0L) }

            LaunchedEffect(reorderedId) {
                if(reorderedId != 0L) {
                    delay(200)
                    reorderedId = 0L
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                drippersUiState.drippers.forEachIndexed { idx, it ->
                    item {
                        val targetColor = if (it.id == reorderedId) Color.LightGray else Color.White
                        val backgroundColor by animateColorAsState(
                            targetColor,
                            TweenSpec(500),
                            label = "reorderAnimation"
                        )

                        Box(modifier = Modifier
                            .fillMaxWidth(1f)
                            .background(backgroundColor, shape = RoundedCornerShape(8.dp))) {
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
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                ) {
                                    Text(
                                        text = it.name ?: "",
                                        style = Typography.titleMedium,
                                        modifier = Modifier
                                            .padding(bottom = 6.dp)
                                    )
                                    Text(
                                        text = it.createdBy ?: "",
                                        style = Typography.bodySmall,
                                        modifier = Modifier
                                    )
                                    if (!it.comment.isNullOrEmpty()) {
                                        Text(
                                            text = "- \"${it.comment}\"",
                                            style = Typography.titleSmall,
                                            modifier = Modifier
                                                .fillMaxWidth(1f)
                                                .padding(3.dp)
                                        )
                                    }
                                }
                                Column() {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(0.dp)
                                            .clickable(
                                                onClick = {
                                                    onClickReorder(idx, idx - 1)
                                                    reorderedId = it.id
                                                },
                                                indication = null,
                                                interactionSource = MutableInteractionSource()
                                            )
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(0.dp)
                                            .clickable(
                                                onClick = {
                                                    onClickReorder(idx, idx + 1)
                                                    reorderedId = it.id
                                                },
                                                indication = null,
                                                interactionSource = MutableInteractionSource()
                                            )
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
fun DripperSaveScreen(
    onClickRegistration: (DripperRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("DripperSaveScreen", "start")

    var request by remember { mutableStateOf(DripperRequest()) }

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
                label = { Text("드리퍼명") },
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
                value = request.createdBy ?: "",
                onValueChange = { request = request.copy(createdBy = it) },
                label = { Text("제조사") },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DripperDetailScreen(
    drippersUiState: DrippersUiState,
    onNavigateToUpdate: () -> Unit,
    onClickDelete: (Dripper) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("DripperDetailScreen", "start")

    val dripper = drippersUiState.dripper

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
                label = "드리퍼명"
            ) {
                Text(
                    text = "${dripper?.name}",
                    style = Typography.bodySmall,
                )
            }

            OutlinedText(
                label = "제조사"
            ) {
                Text(
                    text = "${dripper?.createdBy}",
                    style = Typography.bodySmall,
                )
            }

            if(!dripper?.comment.isNullOrEmpty()) {
                OutlinedText(
                    label = "메모"
                ) {
                    Text(
                        text = "${dripper?.comment}",
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
                onClick = { onClickDelete(dripper!!) },
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
                Text("닫기")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DripperUpdateScreen(
    drippersUiState: DrippersUiState,
    onClickUpdate: (DripperRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("DripperUpdateScreen", "start")

    var request by remember { mutableStateOf(DripperRequest(drippersUiState.dripper!!)) }
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
                label = { Text("드리퍼명") },
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
                value = request.createdBy ?: "",
                onValueChange = { request = request.copy(createdBy = it) },
                label = { Text("제조사") },
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
