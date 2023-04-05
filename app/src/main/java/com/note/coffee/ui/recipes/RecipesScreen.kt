package com.note.coffee.ui.recipes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.dto.recipes.RecipeRequest
import com.note.coffee.data.entity.recipes.Recipe
import com.note.coffee.ui.common.OutlinedSelectBox
import com.note.coffee.ui.common.OutlinedText
import com.note.coffee.ui.theme.Black
import com.note.coffee.ui.theme.LightCoffee
import com.note.coffee.ui.theme.Typography

@Composable
fun RecipeBeanListScreen(
    recipesUiState: RecipesUiState,
    onNavigateToSave: () -> Unit,
    onNavigateToRecipeList: (BeanResponse) -> Unit,
) {
    Log.d("RecipeBeanListScreen", "start")

    val beans = recipesUiState.beans
    val recipes = recipesUiState.recipes

    Box(
        modifier = Modifier,
    ) {

        if(beans.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록된 레시피가 없습니다.",
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                beans.forEach {
                    val matchedRecipes = recipes.filter { recipe -> recipe.bean == it }
                    if (matchedRecipes.isNotEmpty()) {
                        item {
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .clickable(onClick = { onNavigateToRecipeList(it) })
                                    .border(
                                        border = BorderStroke(1.dp, Black),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(10.dp),
                            ) {
                                Column() {
                                    Text(
                                        text = it.bean.name ?: "",
                                        style = Typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                    Text(
                                        text = "원산지 : ${it.getOriginInfo() ?: ""}",
                                        style = Typography.bodySmall,
                                    )
                                    Text(
                                        text = "로스터리 : ${it.roastery?.name ?: ""}",
                                        style = Typography.bodySmall,
                                    )
                                    Text(
                                        text = "배전도 : ${it.bean.roastDegree?.getName() ?: ""}",
                                        style = Typography.bodySmall,
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .padding(end = 10.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "",
                                        modifier = Modifier.size(30.dp).padding(0.dp)
                                    )

                                }
                            }
                            Spacer(Modifier.size(10.dp))
                        }
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

@Composable
fun RecipeListScreen(
    recipesUiState: RecipesUiState,
    onNavigateToSave: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    Log.d("RecipeListScreen", "start")

    val beans = recipesUiState.bean
    val recipes = recipesUiState.recipes
    val matchedRecipes = recipes.filter { recipe -> recipe.bean == beans }

    Box(
        modifier = Modifier,
    ) {
        if(matchedRecipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록된 레시피가 없습니다.",
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                matchedRecipes.forEach {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .clickable(onClick = { onNavigateToDetail(it.recipe.id) })
                                .border(
                                    border = BorderStroke(1.dp, Black),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp),

                            ) {
                            Column() {
                                Text(
                                    text = it.bean?.bean?.name ?: "",
                                    style = Typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Text(
                                    text = "로스터리 : ${it.bean?.roastery?.name ?: ""}",
                                    style = Typography.bodySmall,
                                )
                                Text(
                                    text = "분쇄도 : ${it.recipe.grindingDegree ?: ""} 클릭",
                                    style = Typography.bodySmall,
                                )
                                Text(
                                    text = "물 온도 : ${it.recipe.temperature ?: ""}°C",
                                    style = Typography.bodySmall,
                                )
                                Text(
                                    text = "핸드밀 : ${it.handMill?.name ?: ""}",
                                    style = Typography.bodySmall,
                                )
                                Text(
                                    text = "드리퍼 : ${it.dripper?.name ?: ""}",
                                    style = Typography.bodySmall,
                                )
                                if (!it.recipe.comment.isNullOrEmpty()) {
                                    Text(
                                        text = "- \"${it.recipe.comment}\"",
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
}


@OptIn(ExperimentalMaterial3Api::class,)
@Composable
fun RecipeSaveScreen(
    recipesUiState: RecipesUiState,
    onClickSave: (RecipeRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("RecipeSaveScreen", "start")

    var newRecipe by remember { mutableStateOf(RecipeRequest()) }

    val bean = recipesUiState.bean
    var beanName = bean?.bean?.name
    if (!bean?.roastery?.name.isNullOrEmpty()) {
        beanName += "(${bean?.roastery?.name})"
    }
    if(bean != null) newRecipe = newRecipe.copy(bean = bean.bean)

    val beans = recipesUiState.beans
    val handMills = recipesUiState.handMills
    val drippers = recipesUiState.drippers

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
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column() {
                        OutlinedSelectBox(
                            items = beans,
                            label = { Text("원두") },
                            onClick = { newRecipe = newRecipe.copy(bean = it.bean) },
                            value = {
                                var result = it.bean.name
                                if (!it.roastery?.name.isNullOrEmpty()) {
                                    result += "(${it.roastery?.name})"
                                }
                                result ?: ""
                            },
                            currentValue = beanName ?: "",
                            readOnly = bean != null
                        )

                        OutlinedTextField(
                            value = newRecipe.temperature?.toString() ?: "",
                            onValueChange = {
                                val newText = it.filter { v -> v.isDigit() }
                                val newTemperature = if (newText.isEmpty()) null else newText.toInt()
                                newRecipe = newRecipe.copy(temperature = newTemperature)
                            },
                            label = { Text("물 온도") },
                            modifier = Modifier.fillMaxWidth(1f),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )
                        )

                        OutlinedTextField(
                            value = newRecipe.grindingDegree?.toString() ?: "",
                            onValueChange = {
                                val newText = it.filter { v -> v.isDigit() }
                                val newDegree = if (newText.isEmpty()) null else newText.toInt()
                                newRecipe = newRecipe.copy(grindingDegree = newDegree)
                            },
                            label = { Text("분쇄도") },
                            modifier = Modifier.fillMaxWidth(1f),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )
                        )

                        OutlinedSelectBox(
                            items = handMills,
                            label = { Text("핸드밀") },
                            onClick = { newRecipe = newRecipe.copy(handMill = it) },
                            value = { it.name ?: "" }
                        )

                        OutlinedSelectBox(
                            items = drippers,
                            label = { Text("드리퍼") },
                            onClick = { newRecipe = newRecipe.copy(dripper = it) },
                            value = { it.name ?: "" }
                        )

                        OutlinedTextField(
                            value = newRecipe.comment,
                            onValueChange = { newRecipe = newRecipe.copy(comment = it) },
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
                            )
                        )
                    }

                    AnimatedVisibility(
                        visible = !recipesUiState.errorMessage.isNullOrEmpty(),
                        Modifier
                            .fillMaxWidth(0.75f)
                            .padding(bottom = 50.dp)
                    ) {
                        Snackbar() {
                            Text(
                                text = recipesUiState.errorMessage ?: "",
                                Modifier.wrapContentWidth()
                            )
                        }
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
                        onClickSave(newRecipe)
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

@Composable
fun RecipeDetailScreen(
    recipesUiState: RecipesUiState,
    onNavigateToUpdate: () -> Unit,
    onClickDelete: (Recipe) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("RecipeDetailScreen", "start")

    val recipe = recipesUiState.recipe?.recipe
    val beanResponse = recipesUiState.recipe?.bean
    val bean = recipesUiState.recipe?.bean?.bean
    val roastery = recipesUiState.recipe?.bean?.roastery
    val origin = recipesUiState.recipe?.bean?.origin
    val handMill = recipesUiState.recipe?.handMill
    val dripper = recipesUiState.recipe?.dripper

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
                    label = "원두 정보",
                ) {
                    Text(
                        text = bean?.name ?: "",
                        style = Typography.titleMedium,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = "원산지 : ${beanResponse?.getOriginInfo() ?: ""}",
                        style = Typography.bodySmall,
                    )
                    Text(
                        text = "로스터리 : ${roastery?.name ?: ""}",
                        style = Typography.bodySmall,
                    )
                    Text(
                        text = "배전도 : ${bean?.roastDegree?.getName() ?: ""}",
                        style = Typography.bodySmall,
                    )
                }

                OutlinedText(
                    label = "물 온도",
                ) {
                    Text(
                        text = "${recipe?.temperature ?: ""}°C",
                        style = Typography.bodySmall,
                    )
                }
                OutlinedText(
                    label = "분쇄도",
                ) {
                    Text(
                        text = "${recipe?.grindingDegree ?: ""} 클릭",
                        style = Typography.bodySmall,
                    )
                }

                OutlinedText(
                    label = "핸드밀",
                ) {
                    Text(
                        text = handMill?.getFullName() ?: "",
                        style = Typography.bodySmall,
                    )
                }

                OutlinedText(
                    label = "드리퍼",
                ) {
                    Text(
                        text = dripper?.getFullName() ?: "",
                        style = Typography.bodySmall,
                    )
                }

                if(!recipe?.comment.isNullOrEmpty()) {
                    OutlinedText(
                        label = "메모",
                    ) {
                        Text(
                            text = recipe?.comment ?: "",
                            style = Typography.bodySmall,
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
                onClick = { onClickDelete(recipe!!) },
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
fun RecipeUpdateScreen(
    recipesUiState: RecipesUiState,
    onClickUpdate: (RecipeRequest) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Log.d("RecipeUpdateScreen", "start")

    var newRecipe by remember { mutableStateOf(RecipeRequest(recipesUiState.recipe!!)) }

    val beans = recipesUiState.beans
    val handMills = recipesUiState.handMills
    val drippers = recipesUiState.drippers

    val bean = recipesUiState.recipe?.bean?.bean
    val roastery = recipesUiState.recipe?.bean?.roastery
    val handMill = recipesUiState.recipe?.handMill
    val dripper = recipesUiState.recipe?.dripper

    var beanName = bean?.name
    if (!roastery?.name.isNullOrEmpty()) {
        beanName += "(${roastery?.name})"
    }

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
                OutlinedSelectBox(
                    items = beans,
                    currentValue = beanName ?: "",
                    label = { Text("원두") },
                    onClick = { newRecipe = newRecipe.copy(bean = it.bean) },
                    value = {
                        var result = it.bean.name
                        if(!it.roastery?.name.isNullOrEmpty()) {
                            result += "(${it.roastery?.name})"
                        }
                        result ?: ""
                    }
                )

                OutlinedTextField(
                    value = newRecipe.temperature?.toString() ?: "",
                    onValueChange = {
                        val newText = it.filter { v -> v.isDigit() }
                        val newTemperature = if (newText.isEmpty()) null else newText.toInt()
                        newRecipe = newRecipe.copy(temperature = newTemperature)
                    },
                    label = { Text("물 온도") },
                    modifier = Modifier.fillMaxWidth(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                OutlinedTextField(
                    value = newRecipe.grindingDegree?.toString() ?: "",
                    onValueChange = {
                        val newText = it.filter { v -> v.isDigit() }
                        val newDegree = if (newText.isEmpty()) null else newText.toInt()
                        newRecipe = newRecipe.copy(grindingDegree = newDegree)
                    },
                    label = { Text("분쇄도") },
                    modifier = Modifier.fillMaxWidth(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )

                OutlinedSelectBox(
                    items = handMills,
                    currentValue = handMill?.name,
                    label = { Text("핸드밀") },
                    onClick = { newRecipe = newRecipe.copy(handMill = it) },
                    value = { it.name ?: "" }
                )

                OutlinedSelectBox(
                    items = drippers,
                    currentValue = dripper?.name,
                    label = { Text("드리퍼") },
                    onClick = { newRecipe = newRecipe.copy(dripper = it) },
                    value = { it.name ?: "" }
                )

                OutlinedTextField(
                    value = newRecipe.comment,
                    onValueChange = { newRecipe = newRecipe.copy(comment = it) },
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
                    )
                )

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
                        onClickUpdate(newRecipe)
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
