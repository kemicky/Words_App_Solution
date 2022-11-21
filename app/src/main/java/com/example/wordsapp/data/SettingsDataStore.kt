package com.example.wordsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


/**
Author: Kemmy MO Jones
Project: Words
Date: 2022/11/21
Email: mjkonceptz@gmail.com
Copyright (c) 2022 MJKonceptz. All rights reserved.
 */



/**
 *  Create a DataStore instance using the preferencesDataStore delegate, with the Context as receiver.
 *  MARK: If DataStore<Preference> prompt any error : Go to Code -> Code CleanUp & then Go to File -> Invalidate cache & Restart
 *  */

private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as receiver.

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
) //end: datastore

class SettingsDataStore(preference_datastore: DataStore<Preferences>) {
    private val IS_LINEAR_LAYOUT_MANAGER = booleanPreferencesKey("is_linear_layout_manager")

    val preferenceFlow: Flow<Boolean> = preference_datastore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        } //end: PreferenceFlow
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[IS_LINEAR_LAYOUT_MANAGER] ?: true

        } //end: map

    suspend fun saveLayoutToPreferencesStore(isLinearLayoutManager: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager
        }

    } //end: saveLayoutToPreferencesStore

}//end:SettingsDataStore