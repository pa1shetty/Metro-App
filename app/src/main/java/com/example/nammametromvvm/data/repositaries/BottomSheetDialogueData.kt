package com.example.nammametromvvm.data.repositaries


import com.google.android.material.bottomsheet.BottomSheetBehavior

data class BottomSheetDialogueData(
    val showHeader: Boolean = true,
    val headerText: String,
    val showInfo: Boolean = true,
    val infoText: String,
    val showPositiveButton: Boolean = true,
    val positiveButtonText: String,
    val showNegativeButton: Boolean = false,
    val negativeButtonText: String,
    val setCancelable: Boolean = false,
    val setCanceledOnTouchOutside: Boolean = false,
    val state: Int = BottomSheetBehavior.STATE_EXPANDED
)

