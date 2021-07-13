package com.trycatch.GodAarti.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trycatch.GodAarti.R;
import com.trycatch.GodAarti.data.RecyclerDataModel;
import com.trycatch.GodAarti.ui.MainActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>{


    private ArrayList<RecyclerDataModel> dataSet;
    private MyViewHolder.OnNoteListener monNoteListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName;
        public TextView textViewDesc;
        public CircleImageView imageViewIcon;
        public OnNoteListener onNoteListener;


        public MyViewHolder(View itemView,OnNoteListener onNoteListener) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.ItemName);
            this.textViewDesc = (TextView) itemView.findViewById(R.id.ItemDesc);
            this.imageViewIcon = (CircleImageView) itemView.findViewById(R.id.ItemImg);

            this.onNoteListener=onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());


        }

        public interface OnNoteListener{
            void onNoteClick(int position);

        }

    }

    public RecyclerviewAdapter(ArrayList<RecyclerDataModel> data, MyViewHolder.OnNoteListener onNoteListener) {
        this.dataSet = data;
        this.monNoteListener=onNoteListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view,monNoteListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        TextView textViewName = holder.textViewName;
//        TextView textViewVersion = holder.textViewDesc;
//        CircleImageView circleImageView = holder.imageViewIcon;
//
//        textViewName.setText(dataSet.get(position).getName());
//        textViewVersion.setText(dataSet.get(position).getDescription());
//        circleImageView.setImageResource(dataSet.get(position).getImages());

        holder.textViewName.setText(dataSet.get(position).getName());
        holder.textViewDesc.setText(dataSet.get(position).getDescription());
        holder.imageViewIcon.setImageResource(dataSet.get(position).getImages());


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
