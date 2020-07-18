package com.khalej.fastcar.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khalej.fastcar.R;

import androidx.annotation.Nullable;

public class bottomsheet_driver_fragment extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    public static bottomsheet_driver_fragment newInstance() {
        return new bottomsheet_driver_fragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottom_driver_sheet, null);
        dialog.setContentView(contentView);
        dialog.findViewById(R.id.cash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick("cash");
                dismiss();
            }
        });
        dialog.findViewById(R.id.credit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick("credit");
                dismiss();
            }
        });
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ItemClickListener {
        void onItemClick(String item);
    }

}
