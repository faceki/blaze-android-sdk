package com.faceki.android.di

import com.faceki.android.FaceKi
import com.faceki.android.KycResponseHandler
import com.faceki.android.domain.model.DocumentData
import com.faceki.android.util.FileManager

object AppConfig {

    const val TAG = "FaceKi-SDK"

    @JvmStatic
    var workflowId: String? = null

    @JvmStatic
    var verificationLink: String? = null

    @JvmStatic
    var recordIdentifier: String? = null


    @JvmStatic
    var selfieImagePath: String? = null

    @JvmStatic
    var kycResponseHandler: KycResponseHandler? = null


    @JvmStatic
    private var colorMap: HashMap<FaceKi.ColorElement, FaceKi.ColorValue>? = null

    @JvmStatic
    private var iconMap: HashMap<FaceKi.IconElement, FaceKi.IconValue>? = null

    @JvmStatic
    private var documents: MutableList<DocumentData>? = null

    @JvmStatic
    @Synchronized
    fun addDocument(documentData: DocumentData) {
        if (documents == null) {
            documents = ArrayList()
        }
        documents?.add(
            documentData
        )
    }

    @JvmStatic
    fun getDocuments() = documents

    @JvmStatic
    fun removeLastDocument() {
        documents?.lastOrNull()?.let {
            FileManager.deleteFile(it.imagePath)
        }
        documents?.removeLastOrNull()
    }

    @JvmStatic
    fun clearAllDocuments() {
        documents?.clear()
        documents = null
    }


    @JvmStatic
    @Synchronized
    fun setCustomColors(newColorMap: HashMap<FaceKi.ColorElement, FaceKi.ColorValue>) {
        colorMap = newColorMap
    }

    @JvmStatic
    @Synchronized
    fun setCustomIcons(newIconMap: HashMap<FaceKi.IconElement, FaceKi.IconValue>) {
        iconMap = newIconMap
    }

    @JvmStatic
    fun getCustomColor(element: FaceKi.ColorElement): FaceKi.ColorValue? {
        return colorMap?.get(element)
    }

    @JvmStatic
    fun getCustomIcon(element: FaceKi.IconElement): FaceKi.IconValue? {
        return iconMap?.get(element)
    }

    @JvmStatic
    fun clear() {
        verificationLink = null
        recordIdentifier = null
        selfieImagePath = null
        kycResponseHandler = null
        colorMap = null
        iconMap = null
        documents = null
    }

}
