package com.example.simpletodo;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


//Responsible for taking data in the model into a row in the recycle view.
public class ItemsAdapter extends  RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClicked(int position);

    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;


    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflater to inflate a view.
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);

        //Wrap it inside a ViewHolder and return it.
        return new ViewHolder(todoView);
    }

    //Responsible for binding data to a particular ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the item at the position.
        String item = items.get(position);

        //Bind the item into the specified ViewHolder.
        holder.bind(item);

    }

    //Tells the Recycle View how many items are in the list.
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each rows of the list.
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //Update the view inside the ViewHolder with this data.
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notify the listener the position of the item clicked.
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    /*When android notifies us that an item was long-pressed,
                    notify the listener which position was long pressed*/

                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
