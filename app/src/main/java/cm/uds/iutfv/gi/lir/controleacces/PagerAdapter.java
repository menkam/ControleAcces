package cm.uds.iutfv.gi.lir.controleacces;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cm.uds.iutfv.gi.lir.controleacces.recycler.Etudiants;
import cm.uds.iutfv.gi.lir.controleacces.recycler.ItemClickListener;
import cm.uds.iutfv.gi.lir.controleacces.recycler.MyViewHolder;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class PagerAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Etudiants> etudiants;

    public PagerAdapter(List<Etudiants> etudiants) {
        this.etudiants = etudiants;
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card, parent, false);
        return new MyViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.nameTxt.setText(etudiants.get(position).getName());
        holder.descriptionTxt.setText(etudiants.get(position).getDescription());
        holder.avatar.setImageBitmap(etudiants.get(position).getAvatar());

        //LISTENER
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                //Toast.makeText(c, etudiants.get(pos).getRegime(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}