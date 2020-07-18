package com.khalej.fastcar.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.khalej.fastcar.R;
import com.khalej.fastcar.RoundRectCornerImageView;
import com.khalej.fastcar.model.My_Order;
import com.khalej.fastcar.model.apiinterface_home;
import com.khalej.fastcar.model.notificationData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerAdapter_rides extends RecyclerView.Adapter<RecyclerAdapter_rides.MyViewHolder> {
    Typeface myTypeface;
    private Context context;
    List<My_Order> contactslist;
    private apiinterface_home apiinterface;
    TextView toolbar_title;

    ProgressDialog progressDialog;
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor edt;
    public RecyclerAdapter_rides(Context context, List<My_Order> contactslist){
        this.contactslist=contactslist;
        this.context=context;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rideslist,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        myTypeface = Typeface.createFromAsset(context.getAssets(), "Nasser.otf");
        sharedpref = context.getSharedPreferences("Education", Context.MODE_PRIVATE);
        edt = sharedpref.edit();
        if(sharedpref.getString("language","").trim().equals("ar")) {
        }
        holder.to.setText(contactslist.get(position).getTo_address());
        holder.from.setText(contactslist.get(position).getFrom_address());
        holder.Date.setText(contactslist.get(position).getDate());
        holder.time.setText(contactslist.get(position).getTime_spanttime_spant());
        holder.rate.setRating((float) contactslist.get(position).getClient_rate());



    }
    @Override
    public int getItemCount() {
        return contactslist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView from,time,Date,to;
        RatingBar rate;

        public MyViewHolder(View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            Date=itemView.findViewById(R.id.date);
            from=(TextView)itemView.findViewById(R.id.from);
            to=itemView.findViewById(R.id.to);
            rate=itemView.findViewById(R.id.rate);
        }
    }


}