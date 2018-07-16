package cm.uds.iutfv.gi.lir.controleaccesapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cm.uds.iutfv.gi.lir.controleaccesapp.PagerAdapter;
import cm.uds.iutfv.gi.lir.controleaccesapp.R;
import cm.uds.iutfv.gi.lir.controleaccesapp.Session;
import cm.uds.iutfv.gi.lir.controleaccesapp.recycler.Etudiants;

/**
 * Created by florentchampigny on 24/04/15.
 */
@SuppressLint("ValidFragment")
public class RecyclerEtudiantEnSalleFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    protected PagerAdapter pagerAdapter;
    private int tab = 0;
    public static  ArrayList<Etudiants> etudiants = new ArrayList<Etudiants>();

    public RecyclerEtudiantEnSalleFragment(int tab) {
        this.tab = tab;
        Session.setTabEnCours(tab);
    }

    public static RecyclerEtudiantEnSalleFragment newInstance(int tab) {
        return new RecyclerEtudiantEnSalleFragment(tab);
    }

    // todo retire apres
    public static RecyclerEtudiantEnSalleFragment newInstance() {

        return new RecyclerEtudiantEnSalleFragment(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //permet un affichage sous forme liste verticale
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setHasFixedSize(true);

        GetListEtudiantESAsynctask asynctask = new GetListEtudiantESAsynctask();
        String url = Session.getRoute_get_etutEnSalle(0);
        asynctask.execute(new String[]{url});
    }

    public static void resetListeEtudiant(ArrayList<Etudiants> listeEtudiant){
        RecyclerEtudiantEnSalleFragment.etudiants = listeEtudiant;
    }

    public void refreshList(){
        Session.setTabEnCours(0);
    }

    public class GetListEtudiantESAsynctask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject jsonObject = null;
            for (String url : urls) {
                StringBuffer reponseHTTP = new StringBuffer();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = null;
                try {
                    response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        InputStream content = entity.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            reponseHTTP.append(line);
                        }
                        jsonObject = new JSONObject(reponseHTTP.toString());
                    } else {
                        Toast.makeText(getContext(), "Code = " + statusCode, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(getContext(), "erreur de ccreation du clientHttp : " + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "erreur de conversion JSON : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            // On récupère le tableau d'objets qui nous concernent
            JSONArray array = null;
            try {
                array = new JSONArray(jsonObject.getString("etudiants"));
                if(array.length()>0) {
                    //Session.movies1.clear();
                    ArrayList<Etudiants> arrayListEtudiants = new ArrayList<Etudiants>();
                    for (int i = 0; i < array.length(); i++) {
                        // On récupère un objet JSON du tableau
                        JSONObject obj = new JSONObject(array.getString(i));
                        // On fait le lien Personne - Objet JSON
                        Etudiants etudiant = null;
                        byte[] decodedString = Base64.decode(obj.getString("photo"), Base64.DEFAULT);
                        Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        etudiant = new Etudiants();
                        etudiant.setName(obj.getString("matricule"));
                        etudiant.setDescription(obj.getString("nom") + " " + obj.getString("prenom"));
                        etudiant.setAvatar(bm);
                        etudiant.setRegime(obj.getString("regime"));

                        // On ajoute la personne à la liste
                        arrayListEtudiants.add(etudiant);
                    }

                    //penser à passer notre Adapter (ici : TestRecyclerViewAdapter) à un RecyclerViewMaterialAdapter
                    mAdapter = new RecyclerViewMaterialAdapter(new PagerAdapter(arrayListEtudiants));
                    mRecyclerView.setAdapter(mAdapter);
                    //notifier le MaterialViewPager qu'on va utiliser une RecyclerView
                    MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

                    Toast.makeText(getContext(), "Nombre d'etudiant : " + array.length(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "pas d'etudiant en salle", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
