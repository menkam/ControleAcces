package cm.uds.iutfv.gi.lir.controleacces.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cm.uds.iutfv.gi.lir.controleacces.PagerAdapter;
import cm.uds.iutfv.gi.lir.controleacces.R;
import cm.uds.iutfv.gi.lir.controleacces.Session;
import cm.uds.iutfv.gi.lir.controleacces.recycler.Etudiants;

/**
 * Created by florentchampigny on 24/04/15.
 */
@SuppressLint("ValidFragment")
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int tab = -1;

    public RecyclerViewFragment(int tab) {
        this.tab = tab;
    }

    public static RecyclerViewFragment newInstance(int tab) {
        return new RecyclerViewFragment(tab);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //permet un affichage sous forme liste verticale
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //penser à passer notre Adapter (ici : TestRecyclerViewAdapter) à un RecyclerViewMaterialAdapter
        switch (this.tab){
            case 0:
                mAdapter = new RecyclerViewMaterialAdapter(new PagerAdapter(getObect()));
                mRecyclerView.setAdapter(mAdapter);
                //notifier le MaterialViewPager qu'on va utiliser une RecyclerView
                MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
                break;
            case 1:
                mAdapter = new RecyclerViewMaterialAdapter(new PagerAdapter(getObect()));
                mRecyclerView.setAdapter(mAdapter);
                //notifier le MaterialViewPager qu'on va utiliser une RecyclerView
                MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
                break;
            case 2:
                mAdapter = new RecyclerViewMaterialAdapter(new PagerAdapter(getObect()));
                mRecyclerView.setAdapter(mAdapter);
                //notifier le MaterialViewPager qu'on va utiliser une RecyclerView
                MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
                break;
            case 3:
                mAdapter = new RecyclerViewMaterialAdapter(new PagerAdapter(getList()));
                mRecyclerView.setAdapter(mAdapter);
                //notifier le MaterialViewPager qu'on va utiliser une RecyclerView
                MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
                break;
        }

    }

    private ArrayList<Etudiants> getObect() {
        ArrayList<Etudiants> mContentItems = new ArrayList<>();
        for (int i = 0; i < 10; ++i)
            mContentItems.add(new Etudiants());
        return mContentItems;
    }
    public ArrayList<Etudiants> getList(){

        try
        {
            // Création d'un thread
            Thread t = new Thread()
            {
                public void run()
                {
                    Looper.prepare();

                    StringBuffer reponseHTTP = new StringBuffer();
                    HttpClient client = new DefaultHttpClient();
                    String url = Session.getRoute_get_listeEtud();
                    HttpGet httpGet = new HttpGet(url);
                    try{
                        HttpResponse response = client.execute(httpGet);
                        StatusLine statusLine = response.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        if(statusCode == 200){
                            HttpEntity entity = response.getEntity();
                            InputStream content = entity.getContent();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                            String line;
                            while ((line = reader.readLine()) != null){
                                reponseHTTP.append(line);
                            }

                            // On récupère le JSON complet
                            JSONObject jsonObject = new JSONObject(reponseHTTP.toString());
                            // On récupère le tableau d'objets qui nous concernent
                            JSONArray array = new JSONArray(jsonObject.getString("etudiants"));
                            ArrayList<Etudiants> etudiants = new ArrayList<Etudiants>();
                            if(array.length()>0)
                                for (int i = 0; i < array.length(); i++) {
                                    // On récupère un objet JSON du tableau
                                    JSONObject obj = new JSONObject(array.getString(i));
                                    // On fait le lien Personne - Objet JSON
                                    Etudiants etudiant = null;
                                    byte[] decodedString = Base64.decode(obj.getString("photo"), Base64.DEFAULT);
                                    Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    etudiant = new Etudiants();
                                    etudiant.setName(obj.getString("matricule"));
                                    etudiant.setDescription(obj.getString("nom")+" "+obj.getString("prenom"));
                                    etudiant.setAvatar(bm);
                                    etudiant.setRegime(obj.getString("regime"));

                                    etudiants.add(etudiant);
                                }
                            // On ajoute la personne à la liste
                            Session.movies4 = etudiants;

                            Toast.makeText(getContext(), "Nombre d'etudiant"+array.length(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(), "Code = "+statusCode, Toast.LENGTH_LONG).show();
                            //progressBarConnexion.setVisibility(View.INVISIBLE);
                        }
                    }catch(Exception e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        //progressBarConnexion.setVisibility(View.INVISIBLE);
                    }
                    Looper.loop();
                }
            };
            //t.sleep(1000);
            t.start();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //return Session.movies4;
        //return personnes;
        ArrayList<Etudiants> mContentItems = new ArrayList<>();
        for (int i = 0; i < 100; ++i){
            Etudiants etudiant = new Etudiants();
            etudiant.setName("Matricule"+i+" NOM"+i+" Prenom"+i);
            if((i+1)%3==1) etudiant.setDescription("Regulier");
            else etudiant.setDescription("Creditaire");
            mContentItems.add(etudiant);
        }

        return mContentItems;
    }
}
