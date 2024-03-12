package com.faceki.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DocumentData(
    val documentSide: DocumentSide? = null,
    val key: String? = null,
    val imagePath: String? = null,
) : Parcelable

@Parcelize
enum class DocumentSide : Parcelable {
    FRONT,
    BACK
}
