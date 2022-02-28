package com.example.nammametromvvm.utility.language

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.utility.language.LocaleManager.LANGUAGEEnum.*
import java.util.*
import javax.inject.Inject

class LocaleManager @Inject constructor(private val dataStoreRepository: DataStoreRepository) {


    suspend fun setNewLocale(mContext: Context, language: String): Context {
        saveCurrentLanguage(language)
        setLocale(mContext, language)
        return updateResources(mContext, language)
    }


    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }


    private var languages: ArrayList<Language> = ArrayList()
    fun setUpLanguages(): ArrayList<Language> {
        if (languages.isEmpty()) {
            languages.add(Language(SYSTEM.langCode, SYSTEM.langName))
            languages.add(Language(ENGLISH.langCode, ENGLISH.langName))
            languages.add(Language(KANNADA.langCode, KANNADA.langName))
        }
        return languages
    }

    data class Language(
        val code: String,
        val name: String,
        var id: Int = -1
    )

    enum class LANGUAGEEnum(val langCode: String, val langName: String) {
        SYSTEM("", "System"),
        ENGLISH("en", "English"),
        KANNADA("kn", "ಕನ್ನಡ")
    }

    suspend fun getCurrentLanguage() =
        dataStoreRepository.getCurrentLanguage()

    private suspend fun saveCurrentLanguage(languageCode: String) =
        dataStoreRepository.saveCurrentLanguage(languageCode)

    private fun setLocale(activity: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        // resources.updateConfiguration(config, resources.displayMetrics)
    }
}