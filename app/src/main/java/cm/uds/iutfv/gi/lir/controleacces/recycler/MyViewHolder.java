package cm.uds.iutfv.gi.lir.controleacces.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cm.uds.iutfv.gi.lir.controleacces.R;

/**
 * Created by men_franc 18-05-2018
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView avatar;
    TextView nameTxt;
    TextView descriptionTxt;
    ItemClickListener itemClickListener;

    public MyViewHolder(View itemView) {
        super(itemView);

        nameTxt = (TextView) itemView.findViewById(R.id.infoUser);
        descriptionTxt = (TextView) itemView.findViewById(R.id.descriptionUser);
        avatar = (ImageView) itemView.findViewById(R.id.photoUser);

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