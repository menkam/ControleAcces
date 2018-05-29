package cm.uds.iutfv.gi.lir.controleacces.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Base64;
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

import cm.uds.iutfv.gi.lir.controleacces.Session;

/**
 * Created by men_franc 18-05-2018
 */

public class Etudiants {

    private String name;
    private String description;
    private String regime;
    private Bitmap avatar;

    public Etudiants(String name, String description, Bitmap avatar) {
        this.name = name;
        this.description = description;
        this.avatar = avatar;
    }

    public Etudiants() {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegime() {
        return regime;
    }

    public Bitmap getAvatar() {
        return avatar;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    /**
     * OBTENIR LA LISTE DES ETUDIANTS
     * @return ArrayList<Etudiants>
     */
    public static ArrayList<Etudiants> getListe(final String url){
        try {
           /* // Création d'un thread
            Thread t = new Thread() {
                public void run() {

                    Looper.prepare();*/

                    StringBuffer reponseHTTP = new StringBuffer();
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);
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
                            if(Session.getTabEnCours()==1) Session.movies1 = etudiants;
                            if(Session.getTabEnCours()==2) Session.movies2 = etudiants;
                            if(Session.getTabEnCours()==3) Session.movies3 = etudiants;
                            if(Session.getTabEnCours()==4) Session.movies4 = etudiants;

                            //Toast.makeText(Session.getContext(), "Nombre d'etudiant"+array.length(), Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(Session.getContext(), "Code = " + statusCode, Toast.LENGTH_LONG).show();
                            //progressBarConnexion.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception e) {
                        //Toast.makeText(Session.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        //progressBarConnexion.setVisibility(View.INVISIBLE);
                    }
                   /* Looper.loop();
                }
            };
            //t.sleep(1000);
            t.start();*/

        } catch (Exception e) {
            //Toast.makeText(Session.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        if(Session.getTabEnCours()==1) return Session.movies1;
        if(Session.getTabEnCours()==2) return Session.movies2;
        if(Session.getTabEnCours()==3) return Session.movies3;
        if(Session.getTabEnCours()==4) return Session.movies4;
        return null;
    }
}