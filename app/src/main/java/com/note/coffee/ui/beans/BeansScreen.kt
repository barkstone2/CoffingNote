package com.note.coffee.ui.beans

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.note.coffee.data.dto.beans.BeanRequest
import com.note.coffee.data.entity.beans.Bean
import com.note.coffee.data.entity.beans.RoastDegree
import com.note.coffee.ui.common.OutlinedSelectBox
import com.note.coffee.ui.common.OutlinedText
import com.note.coffee.ui.theme.Black
import com.note.coffee.ui.theme.LightCoffee
import com.note.coffee.ui.theme.Typography


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BeanListScreen(
    beansUiState: BeansUiState,
    onNavigateToSave: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    Log.d("BeanListScreen", "start")

    Box(
        modifier = Modifier,
    ) {

        if(beansUiState.beans.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록된 원두가 없습니다.",
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                beansUiState.beans.forEach {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .clickable(onClick = { onNavigateToDetail(it.bean.id) })
                                .border(
                                    border = BorderStroke(1.dp, Black),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp),

                        ) {
                            Column() {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .padding(bottom = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = it.bean.name ?: "",
                                        style = Typography.titleMedium,
                                        modifier = Modifier.fillMaxWidth(0.5f)
                                    )
                                    Text(
                                        text = it.roastery?.name ?: "",
                                        style = Typography.bodySmall,
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = it.getOriginInfo() ?: "",
                                        style = Typography.bodySmall,
                                        modifier = Modifier.fillMaxWidth(0.5f)
                                    )
                                    Text(
                                        text = it.bean.roastDegree?.getName() ?: "",
                                        style = Typography.bodySmall,
                                    )
                                }

                                if(it.bean.cuppingNotes.isNotEmpty()) {
                                    FlowRow() {
                                        it.bean.cuppingNotes.forEach {
                                            Box(
                                                modifier = Modifier
                                                    .padding(1.dp)
                                                    .border(
                                                        border = BorderStroke(1.dp, Black),
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                            ) {
                                                Text(
                                                    text = it,
                                                    style = Typography.bodySmall,
                                                    modifier = Modifier.padding(3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                                if(!it.bean.comment.isNullOrEmpty()) {
                                    Text(
                                        text = "- \"${it.bean.comment}\"",
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,)
@Composable
fun BeanSaveScreen(
    beansUiState: BeansUiState,
    onValueChanged: (BeanRequest) -> Unit,
    onClickSave: (BeanRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("BeanSaveScreen", "start")

    var newBean by remember { mutableStateOf(beansUiState.newBean) }

    val origins = beansUiState.origins
    val roasteries = beansUiState.roasteries

    val focusManager = LocalFocusManager.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp)
    ) {
        val (lazyColumn, buttonRow) = createRefs()
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    bottom.linkTo(buttonRow.top)
                    top.linkTo(parent.top, margin = 0.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            item {
                OutlinedTextField(
                    value = newBean.name ?: "",
                    onValueChange = {
                        newBean = newBean.copy(name = it)
                        onValueChanged(newBean)
                    },
                    label = { Text("원두명") },
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

                OutlinedSelectBox(
                    items = origins,
                    currentValue = newBean.origin?.name,
                    label = { Text("원산지") },
                    onClick = {
                        newBean = newBean.copy(origin = it)
                        onValueChanged(newBean)
                    },
                    value = { it.name ?: "" }
                )

                OutlinedTextField(
                    value = newBean.specificOrigin ?: "",
                    onValueChange = {
                        newBean = newBean.copy(specificOrigin = it)
                        onValueChanged(newBean)
                    },
                    label = { Text("상세지역") },
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

                OutlinedSelectBox(
                    items = roasteries,
                    currentValue = newBean.roastery?.name,
                    label = { Text("로스터리") },
                    onClick = {
                        newBean = newBean.copy(roastery = it)
                        onValueChanged(newBean)
                    },
                    value = { it.name ?: "" }
                )

                OutlinedSelectBox(
                    items = RoastDegree.values().asList(),
                    currentValue = newBean.roastDegree?.getName(),
                    label = { Text("배전도") },
                    onClick = {
                        newBean = newBean.copy(roastDegree = it)
                        onValueChanged(newBean)
                    },
                    value = { it.getName() }
                )

                OutlinedTextField(
                    value = newBean.comment,
                    onValueChange = {
                        newBean = newBean.copy(comment = it)
                        onValueChanged(newBean)
                    },
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

                // Copping Note
                var coppingNote by remember { mutableStateOf("") }
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = coppingNote,
                        onValueChange = { coppingNote = it },
                        label = { Text("커핑노트") },
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if(coppingNote.trim().isNotEmpty()) {
                                    newBean.cuppingNotes += coppingNote
                                    coppingNote = ""
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        SmallFloatingActionButton(
                            onClick = {
                                if(coppingNote.trim().isNotEmpty()) {
                                    newBean.cuppingNotes += coppingNote
                                    coppingNote = ""
                                    onValueChanged(newBean)
                                    focusManager.clearFocus()
                                }
                            },
                            containerColor = LightCoffee,
                        ) {
                            Text(text = "추가", fontSize = 20.sp)
                        }
                    }
                }

                FlowRow() {
                    newBean.cuppingNotes.forEachIndexed { index, it ->
                        InputChip(
                            selected = false,
                            onClick = {},
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable {
                                            newBean = newBean.copy(
                                                cuppingNotes = newBean.cuppingNotes
                                                    .filterIndexed { idx, _ -> idx != index }
                                                    as MutableList<String>
                                            )
                                            onValueChanged(newBean)
                                        }
                                )
                            },
                            label = { Text(text = it, color = Black) },
                            modifier = Modifier
                                .padding(1.dp)
                                .padding(bottom = 1.dp),
                        )
                    }
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
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onClickSave(newBean)
                        onValueChanged(BeanRequest())
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("저장")
                }
                Button(
                    onClick = {
                        onValueChanged(BeanRequest())
                        onNavigateBack()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("취소")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,)
@Composable
fun BeanDetailScreen(
    beansUiState: BeansUiState,
    onNavigateToUpdate: () -> Unit,
    onClickDelete: (Bean) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("BeanDetailScreen", "start")

    val bean = beansUiState.bean?.bean
    val origin = beansUiState.bean?.origin
    val roastery = beansUiState.bean?.roastery

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp)
    ) {
        val (lazyColumn, buttonRow) = createRefs()
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    bottom.linkTo(buttonRow.top)
                    top.linkTo(parent.top, margin = 0.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            item {
                OutlinedText(
                    label = "원두명"
                ) {
                    Text(
                        text = bean?.name ?: "",
                        style = Typography.bodySmall,
                    )
                }

                OutlinedText(
                    label = "원산지"
                ) {
                    Text(
                        text = origin?.name ?: "",
                        style = Typography.bodySmall,
                    )
                }

                OutlinedText(
                    label = "상세지역"
                ) {
                    Text(
                        text = bean?.specificOrigin ?: "",
                        style = Typography.bodySmall,
                    )
                }

                OutlinedText(
                    label = "로스터리"
                ) {
                    Text(
                        text = roastery?.name ?: "",
                        style = Typography.bodySmall,
                    )
                }

                OutlinedText(
                    label = "배전도"
                ) {
                    Text(
                        text = bean?.roastDegree?.getName() ?: "",
                        style = Typography.bodySmall,
                    )
                }

                if(!bean?.comment.isNullOrEmpty()) {
                    OutlinedText(
                        label = "메모"
                    ) {
                        Text(
                            text = "${bean?.comment}",
                            style = Typography.bodySmall,
                        )
                    }
                }
                FlowRow() {
                    bean?.cuppingNotes?.forEach {
                        InputChip(
                            selected = false,
                            onClick = {},
                            label = { Text(it) },
                            modifier = Modifier
                                .padding(1.dp)
                                .padding(bottom = 1.dp)
                        )
                    }
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
                onClick = { onClickDelete(bean!!) },
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,)
@Composable
fun BeanUpdateScreen(
    beansUiState: BeansUiState,
    onClickUpdate: (BeanRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("BeanUpdateScreen", "start")

    var newBean by remember { mutableStateOf(BeanRequest(beansUiState.bean!!)) }

    val origins = beansUiState.origins
    val roasteries = beansUiState.roasteries

    val focusManager = LocalFocusManager.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 20.dp, end = 20.dp)
    ) {
        val (lazyColumn, buttonRow) = createRefs()
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .constrainAs(lazyColumn) {
                    bottom.linkTo(buttonRow.top)
                    top.linkTo(parent.top, margin = 0.dp)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            item {
                OutlinedTextField(
                    value = newBean.name ?: "",
                    onValueChange = { newBean = newBean.copy(name = it) },
                    label = { Text("원두명") },
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

                OutlinedSelectBox(
                    items = origins,
                    currentValue = newBean.origin?.name,
                    label = { Text("원산지") },
                    onClick = { newBean = newBean.copy(origin = it) },
                    value = { it.name ?: "" }
                )

                OutlinedTextField(
                    value = newBean.specificOrigin ?: "",
                    onValueChange = { newBean = newBean.copy(specificOrigin = it) },
                    label = { Text("상세지역") },
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

                OutlinedSelectBox(
                    items = roasteries,
                    currentValue = newBean.roastery?.name,
                    label = { Text("로스터리") },
                    onClick = { newBean = newBean.copy(roastery = it) },
                    value = { it.name ?: "" }
                )

                OutlinedSelectBox(
                    items = RoastDegree.values().asList(),
                    currentValue = newBean.roastDegree?.getName() ?: "",
                    label = { Text("배전도") },
                    onClick = { newBean = newBean.copy(roastDegree = it) },
                    value = { it.getName() }
                )

                OutlinedTextField(
                    value = newBean.comment,
                    onValueChange = { newBean = newBean.copy(comment = it) },
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

                // Copping Note
                var coppingNote by remember { mutableStateOf("") }
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = coppingNote,
                        onValueChange = { coppingNote = it },
                        label = { Text("커핑노트") },
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if(coppingNote.trim().isNotEmpty()) {
                                    newBean.cuppingNotes += coppingNote
                                    coppingNote = ""
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        SmallFloatingActionButton(
                            onClick = {
                                if(coppingNote.trim().isNotEmpty()) {
                                    newBean.cuppingNotes += coppingNote
                                    coppingNote = ""
                                    focusManager.clearFocus()
                                }
                            },
                            containerColor = LightCoffee,
                        ) {
                            Text(text = "추가", fontSize = 20.sp)
                        }
                    }
                }

                FlowRow() {
                    newBean.cuppingNotes.forEachIndexed { index, it ->
                        InputChip(
                            selected = false,
                            onClick = {},
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable {
                                            newBean = newBean.copy(
                                                cuppingNotes = newBean.cuppingNotes
                                                    .filterIndexed { idx, _ -> idx != index }
                                                        as MutableList<String>
                                            )
                                        }
                                )
                            },
                            label = { Text(it) },
                            modifier = Modifier
                                .padding(1.dp)
                                .padding(bottom = 1.dp)
                        )
                    }
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
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onClickUpdate(newBean)
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