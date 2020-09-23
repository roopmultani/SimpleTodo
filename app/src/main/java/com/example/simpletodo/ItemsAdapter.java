package com.example.simpletodo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.xml.xpath.XPathFunctionResolver;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongCLickListener{
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongCLickListener longCLickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> items, OnLongCLickListener longCLickListener, OnClickListener clickListener){
        this.items = items;
        this.longCLickListener = longCLickListener;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout infaltor to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent,false);

        //wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    //responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //grab the item at the position
        String item = items.get(position);

        // bind the item into the specified view holder
        holder.bind(item);
    }

    // Tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView){
            super (itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update the view inside of the view holder with this data
        public void bind(String item){
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // notify the listener which position was long pressed.
                    longCLickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
