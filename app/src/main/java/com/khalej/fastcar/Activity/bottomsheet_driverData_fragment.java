package com.khalej.fastcar.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.khalej.fastcar.R;

import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

public class bottomsheet_driverData_fragment extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;
   TextView model,username,carnum,phone,confirm,cancel ,arrivetime;
   CircleImageView image;
    public static bottomsheet_driverData_fragment newInstance() {
        return new bottomsheet_driverData_fragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottom_driverdata_sheet, null);
        model=contentView.findViewById(R.id.model);
        username=contentView.findViewById(R.id.username);
        carnum=contentView.findViewById(R.id.carnum);
        phone=contentView.findViewById(R.id.phone);
        confirm=contentView.findViewById(R.id.confirm);
        cancel=contentView.findViewById(R.id.cancel);
        image=contentView.findViewById(R.id.image);
        arrivetime=contentView.findViewById(R.id.arrivetime);
        model.setText(getArguments().getString("carModel"));
        username.setText(getArguments().getString("name"));
        carnum.setText(getArguments().getString("carNumber"));
        phone.setText(getArguments().getString("phone"));
        arrivetime.setText(   "السائق سيصل خلال :" +getArguments().getDouble("arrival_time",0.0)+"دقيقة");
        Glide.with(getActivity()).load("https://applicationme.com/fastcar/"+getArguments().getString("image")).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.logo).into(image);
        dialog.setContentView(contentView);
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick("cancel");
                dismiss();
            }
        });
        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick("confirm");
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
