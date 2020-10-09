package com.example.zomatoclone.CallBacks;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.zomatoclone.Comman.LockableBottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class BottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback{
    private BottomSheetBehavior mBottomSheetBehavior;

    public BottomSheetCallback(BottomSheetBehavior mBottomSheetBehavior) {
        this.mBottomSheetBehavior = mBottomSheetBehavior;
    }

    @Override
    public void onStateChanged(@NonNull View view, int newState) {
        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
            if (mBottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                ((LockableBottomSheetBehavior) mBottomSheetBehavior).setLocked(true);
            }
        }
    }

    @Override
    public void onSlide(@NonNull View view, float v) {

    }
}
