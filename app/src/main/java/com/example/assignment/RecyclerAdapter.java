package com.example.assignment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<MatchModel> listMatchModels;
    private Activity activity;
    private SetListener setListener;

    public RecyclerAdapter(ArrayList<MatchModel> listMatchModels, Activity activity, SetListener setListener) {
        this.listMatchModels = listMatchModels;
        this.activity = activity;
        this.setListener = setListener;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity).build();
        ImageLoader.getInstance().init(config);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.match_item, null, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.details.setText(listMatchModels.get(position).getAge() +
                " - " + listMatchModels.get(position).getName() +
                " - " + listMatchModels.get(position).getCountry());

        ImageLoader.getInstance().displayImage(listMatchModels.get(position).getImage(), holder.displayProfile);

        holder.acceptBtn.setOnClickListener(view -> {
            if (holder.acceptBtn.getText().equals("ACCEPT")) {
                holder.acceptBtn.setText("MEMBER ACCEPTED");
                holder.declineBtn.setText("DECLINE");
                setListener.onClick(listMatchModels.get(position), "insert");
            } else {
                holder.acceptBtn.setText("ACCEPT");
            }

        });

        holder.declineBtn.setOnClickListener(view -> {
            if (holder.declineBtn.getText().equals("DECLINE")) {
                holder.declineBtn.setText("MEMBER DECLINED");
                holder.acceptBtn.setText("ACCEPT");
                setListener.onClick(listMatchModels.get(position), "delete");
            } else {
                holder.declineBtn.setText("DECLINE");
            }

        });
    }

    @Override
    public int getItemCount() {
        return listMatchModels.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView displayProfile;
        private TextView details, acceptBtn, declineBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            displayProfile = itemView.findViewById(R.id.image);
            details = itemView.findViewById(R.id.details);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            declineBtn = itemView.findViewById(R.id.declineBtn);
        }
    }
}
