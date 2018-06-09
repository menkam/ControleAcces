package cm.uds.iutfv.gi.lir.controleaccesapp.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cm.uds.iutfv.gi.lir.controleaccesapp.R;

/**
 * Created by men_franc 18-05-2018
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView avatar;
    public TextView nameTxt;
    public TextView descriptionTxt;
    private ItemClickListener itemClickListener;

    protected MyViewHolder(View itemView) {
        super(itemView);

        nameTxt = itemView.findViewById(R.id.infoUser);
        descriptionTxt = itemView.findViewById(R.id.descriptionUser);
        avatar = itemView.findViewById(R.id.photoUser);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }
}