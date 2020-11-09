package com.example.zomatoclone.UI.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.zomatoclone.Interface.BottomSheetCloseListener;
import com.example.zomatoclone.R;
import com.example.zomatoclone.UI.PermissionActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "BottomSheetFragment";
    private ImageView closeButton;
    private RelativeLayout currentLocationButton;
    private BottomSheetCloseListener bottomSheetCloseListener;
    public ProgressBar bottomSheetProgressbar;
    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        //setBottomSheetHeight();
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        // Bind ID with view method
        bindId(view);

        // button click event
        closeButton.setOnClickListener(this);
        currentLocationButton.setOnClickListener(this);
        return view;
    }

    private void bindId(View v) {
        closeButton = v.findViewById(R.id.iv_close_bottom_sheet);
        currentLocationButton = v.findViewById(R.id.rl_use_current_location);
        bottomSheetProgressbar = v.findViewById(R.id.bottom_sheet_progressbar);
    }

    private void closeBottomSheet(){
        dismiss();
    }

    public void changProgressbarVisibility(){
        bottomSheetProgressbar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"onStart");
        Dialog dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = 1750;
        }
        final View view = getView();
        view.post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) view.getParent();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
                CoordinatorLayout.Behavior behavior = params.getBehavior();
                BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
                bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_close_bottom_sheet:
                closeBottomSheet();
                break;

            case R.id.rl_use_current_location:
                Log.e(TAG,"bottomsheet button");
                bottomSheetProgressbar.setVisibility(View.VISIBLE);
                ((PermissionActivity)getActivity()).checkPermission();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG,"onAttach");
        try {
            bottomSheetCloseListener = (BottomSheetCloseListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement BottomSheetCloseListener");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG,"onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG,"onDetach");
        bottomSheetCloseListener.bottomSheetClosed(false);
    }
}