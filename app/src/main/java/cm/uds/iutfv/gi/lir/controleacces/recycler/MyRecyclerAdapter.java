package cm.uds.iutfv.gi.lir.controleacces.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cm.uds.iutfv.gi.lir.controleacces.R;

/**
 * Created by men_franc 18-05-2018
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder>{

    Context c;
    ArrayList<Etudiants> etudiants;

    public MyRecyclerAdapter(Context c, ArrayList<Etudiants> etudiants) {
        this.c = c;
        this.etudiants = etudiants;
    }

    //INITIALIZE HOLDER
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, null);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    //BIND DATA TO VIEWS
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameTxt.setText(etudiants.get(position).getName());
        holder.descriptionTxt.setText(etudiants.get(position).getDescription());
        holder.avatar.setImageBitmap(etudiants.get(position).getAvatar());

        //LISTENER
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                Toast.makeText(c, etudiants.get(pos).getRegime(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }
}