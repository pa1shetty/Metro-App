package com.example.nammametromvvm.utility.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

var themeSetupDone = false
private var themes: ArrayList<HandleTheme.Theme> = ArrayList()

class HandleTheme @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend fun init() {
        if (!themeSetupDone) {
            dataStoreRepository.getCurrentThemeAsFlow().collect { currentTheme ->
                when (currentTheme) {
                    THEMEEnum.DARK.theme -> AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                    THEMEEnum.LIGHT.theme -> AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                    )
                    THEMEEnum.SYSTEM.theme -> {
                        AppCompatDelegate.setDefaultNightMode(
                            MODE_NIGHT_FOLLOW_SYSTEM
                        )
                    }
                }
            }
            themeSetupDone = true
        }
    }


    fun getThemes(): ArrayList<Theme> {
        if (themes.isEmpty()) {
            themes.add(Theme(THEMEEnum.SYSTEM.theme))
            themes.add(Theme(THEMEEnum.LIGHT.theme))
            themes.add(Theme(THEMEEnum.DARK.theme))
        }
        return themes
    }

    suspend fun getCurrentTheme() =
        dataStoreRepository.getCurrentTheme()

    suspend fun saveCurrentTheme(themeName: String) =
        dataStoreRepository.saveCurrentTheme(themeName)


    data class Theme(
        val name: String,
        var id: Int = -1,
    )

    enum class THEMEEnum(val theme: String) {
        DARK("Dark"),
        LIGHT("Light"),
        SYSTEM("System")
    }
}