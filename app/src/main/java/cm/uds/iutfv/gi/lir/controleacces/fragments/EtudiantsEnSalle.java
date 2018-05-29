package cm.uds.iutfv.gi.lir.controleacces.fragments;

import android.content.Context;
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

import cm.uds.iutfv.gi.lir.controleacces.R;
import cm.uds.iutfv.gi.lir.controleacces.Session;
import cm.uds.iutfv.gi.lir.controleacces.recycler.Etudiants;
import cm.uds.iutfv.gi.lir.controleacces.recycler.MyRecyclerAdapter;

public class EtudiantsEnSalle extends Fragment{

    @Nullable
    @Override
    public   View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )   {
        //return   inflater . inflate (R.layout.fragment_tab_fragment3,   container ,   false ) ;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_etutiants_en_salle, null);

        //ArrayList<Movie> liste = getList();

        //RECYCLERVIEW
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.mRecyclerEtudiantEnSalle);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setAdapter(new MyRecyclerAdapter(this.getActivity(), getList()));

        return v;
    }


    //SET TITLE FOR FRAGMENT
    public String toString(){
        return "Etudiants Exclus";
    }


    //AJOUTER UN ETUDIANT DANS LA LISTE
    public static void addStudent(Context c, String scanFormat, String scanContent){
        Toast toast = Toast.makeText(c, scanContent, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * OBTENIR LA LISTE DES ETUDIANTS EXCLUS DE LA SALLE
     * @return
     */
    public ArrayList<Etudiants> getList() {

        try {
            // Création d'un thread
            Thread t = new Thread() {
                public void run() {
                    Looper.prepare();

                    StringBuffer reponseHTTP = new StringBuffer();
                    HttpClient client = new DefaultHttpClient();
                    String url = Session.getRoute_get_listeEtud()+"2";
                    HttpGet httpGet = new HttpGet("http://192.168.8.103:8000/androidGetListEtudiant?idActivite=1&statut=2");
                    try{
                        HttpResponse response = client.execute(httpGet);
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
                            Session.movies1 = etudiants;

                            Toast.makeText(getContext(), "Nombre d'etudiant"+array.length(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Code = " + statusCode, Toast.LENGTH_LONG).show();
                            //progressBarConnexion.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception e) {
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
        return Session.movies1;
    }
}