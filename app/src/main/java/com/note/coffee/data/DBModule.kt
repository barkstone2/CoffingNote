package com.note.coffee.data

import android.content.Context
import com.note.coffee.data.dao.beans.BeanDao
import com.note.coffee.data.dao.drippers.DripperDao
import com.note.coffee.data.dao.handmills.HandMillDao
import com.note.coffee.data.dao.origin.OriginDao
import com.note.coffee.data.dao.recipes.RecipeDao
import com.note.coffee.data.dao.roastery.RoasteryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideBeanDao(appDatabase: AppDatabase): BeanDao = appDatabase.beanDao()

    @Singleton
    @Provides
    fun provideOriginDao(appDatabase: AppDatabase): OriginDao = appDatabase.originDao()

    @Singleton
    @Provides
    fun provideRoasteryDao(appDatabase: AppDatabase): RoasteryDao = appDatabase.roasteryDao()

    @Singleton
    @Provides
    fun provideHandMillDao(appDatabase: AppDatabase): HandMillDao = appDatabase.handMillDao()

    @Singleton
    @Provides
    fun provideDripperDao(appDatabase: AppDatabase): DripperDao = appDatabase.dripperDao()

    @Singleton
    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase): RecipeDao = appDatabase.recipeDao()


}