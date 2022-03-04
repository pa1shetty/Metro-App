package com.example.nammametromvvm.utility.interfaces

import com.example.nammametromvvm.utility.ui.BottomSheet

interface BottomSheetDialogueCallBackListener {
    fun onNegativeButtonClick(requestedFrom: BottomSheet.BottomSheetCalledFrom)
    fun onPositiveButtonClick(requestedFrom: BottomSheet.BottomSheetCalledFrom)
}