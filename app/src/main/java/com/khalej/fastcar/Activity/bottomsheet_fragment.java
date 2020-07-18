package com.khalej.fastcar.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khalej.fastcar.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class bottomsheet_fragment  extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    TextView space,price,time ,pricetext;
    public static bottomsheet_fragment newInstance() {
        return new bottomsheet_fragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet, null);
        sharedpref = getActivity().getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        space=contentView.findViewById(R.id.space);
        time=contentView.findViewById(R.id.time);
        price=contentView.findViewById(R.id.price);
        pricetext=contentView.findViewById(R.id.pricetext);
        space.setText(this.getArguments().getString("distance"));
        time.setText(this.getArguments().getDouble("totaltime")+"");
        price.setText(this.getArguments().getDouble("totalprice")+"");
        if(sharedpref.getInt("country_id",0)==1){
        pricetext.setText("EGP");}
        else{
            pricetext.setText("SAR");
        }
        dialog.setContentView(contentView);
        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick("test");
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
