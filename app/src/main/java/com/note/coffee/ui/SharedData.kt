package com.note.coffee.ui

import android.util.Log
import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.dto.recipes.RecipeResponse
import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.data.entity.beans.Roastery
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.data.entity.handmills.HandMill
import com.note.coffee.data.entity.water.Water
import com.note.coffee.data.repository.beans.BeanRepository
import com.note.coffee.data.repository.beans.OriginRepository
import com.note.coffee.data.repository.beans.RoasteryRepository
import com.note.coffee.data.repository.drippers.DripperRepository
import com.note.coffee.data.repository.handmills.HandMillRepository
import com.note.coffee.data.repository.recipes.RecipeRepository
import com.note.coffee.data.repository.water.WaterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedData @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val beanRepository: BeanRepository,
    private val originRepository: OriginRepository,
    private val roasteryRepository: RoasteryRepository,
    private val handMillRepository: HandMillRepository,
    private val dripperRepository: DripperRepository,
    private val waterRepository: WaterRepository,
) {

    private val _beans: MutableStateFlow<List<BeanResponse>> = MutableStateFlow(listOf())
    val beans: StateFlow<List<BeanResponse>> = _beans.asStateFlow()

    private val _origins: MutableStateFlow<List<Origin>> = MutableStateFlow(listOf())
    val origins: StateFlow<List<Origin>> = _origins.asStateFlow()

    private val _roasteries: MutableStateFlow<List<Roastery>> = MutableStateFlow(listOf())
    val roasteries: StateFlow<List<Roastery>> = _roasteries.asStateFlow()

    private val _handMills: MutableStateFlow<List<HandMill>> = MutableStateFlow(listOf())
    val handMills: StateFlow<List<HandMill>> = _handMills.asStateFlow()

    private val _drippers: MutableStateFlow<List<Dripper>> = MutableStateFlow(listOf())
    val drippers: StateFlow<List<Dripper>> = _drippers.asStateFlow()

    private val _recipes: MutableStateFlow<MutableList<RecipeResponse>> = MutableStateFlow(mutableListOf())
    val recipes: StateFlow<List<RecipeResponse>> = _recipes.asStateFlow()

    private val _waters: MutableStateFlow<List<Water>> = MutableStateFlow(listOf())
    val waters: StateFlow<List<Water>> = _waters.asStateFlow()

    suspend fun loadBeans() {
        CoroutineScope(Dispatchers.IO).launch {
            _beans.value = beanRepository.getAll()
        }
    }

    suspend fun loadOrigins() {
        CoroutineScope(Dispatchers.IO).launch {
            _origins.value = originRepository.getAll()
        }
    }

    suspend fun loadRoasteries() {
        CoroutineScope(Dispatchers.IO).launch {
            _roasteries.value = roasteryRepository.getAll()
        }
    }

    suspend fun loadHandMills() {
        CoroutineScope(Dispatchers.IO).launch {
            _handMills.value = handMillRepository.getAll()
        }
    }

    suspend fun loadDrippers() {
        CoroutineScope(Dispatchers.IO).launch {
            _drippers.value = dripperRepository.getAll()
        }
    }

    suspend fun loadRecipes(beanId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            _recipes.value = recipeRepository.getAllOfBean(beanId).toMutableList()
        }
    }

    suspend fun loadWaters() {
        CoroutineScope(Dispatchers.IO).launch {
            _waters.value = waterRepository.getAll()
        }
    }

    init {
        Log.d(this::class.simpleName, "init")

        CoroutineScope(Dispatchers.IO).launch {
            loadBeans()
            loadOrigins()
            loadRoasteries()
            loadHandMills()
            loadDrippers()
            loadWaters()
        }

    }

}