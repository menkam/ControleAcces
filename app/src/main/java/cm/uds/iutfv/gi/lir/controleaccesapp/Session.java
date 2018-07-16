package cm.uds.iutfv.gi.lir.controleaccesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import cm.uds.iutfv.gi.lir.controleaccesapp.recycler.Etudiants;

public class Session extends AppCompatActivity {

    private TextView message,matricule,nom,prenom,matiere,regime,numero,nbrPerso;


    public static ArrayList<Etudiants> movies1 = new ArrayList<>();
    public static ArrayList<Etudiants> movies2 = new ArrayList<>();
    public static ArrayList<Etudiants> movies3 = new ArrayList<>();
    public static ArrayList<Etudiants> movies4 = new ArrayList<>();
    public static int tabEnCours;
    private static Context context;
    public static int statut0 = 99;

    private static JSONObject responseHttp;

    private static int auth_id;
    private static String auth_sexe;
    private static String auth_address;
    private static String auth_name;
    private static String auth_role;
    private static String auth_photo;
    private static int nbr_perso = 0;
    private static boolean connecter = false;
    private static boolean emailExist = false;

    private static String infoScan;
    private static String anneeAcademique = "2017-2018";

    //private static String url = "http://controle-acces-iutfv.herokuapp.com/";
    //private static String url = "http://192.168.8.101/ControleAccesExamens/public/";
    private static String url = "http://192.168.0.1:8000/";

    private static String route_login = getUrl() +"androidLogin?";
    private static String route_reset_login = getUrl() +"androidResetLogin?";
    private static String route_register = getUrl() +"androidRegister?";
    private static String route_verifierEmail = getUrl() +"androidVerifierEmail?email=";

    private static String route_get_etutEnSalle = getUrl() +"androidGetListEtudiant?idActivite="+getIdActivite()+"&statut=";
    private static String route_get_listeEtud = getUrl() +"androidGetListAllEtudiant?idActivite="+getIdActivite();
    private static String route_add_etutEnSalle = getUrl() +"androidAddStudent?";
    private static String route_add_etutAyantTerminer = getUrl() +"androidStudentFinish?";
    private static String route_add_etutExclus = getUrl() +"androidStudentExclus?";
    private  static String route_gestion_activite;
    private static String route_sentMsg = getUrl() +"http://controle-acces-iutfv.herokuapp.com/";
    private static String route_getInfoUser = getUrl() +"http://controle-acces-iutfv.herokuapp.com/";
    private static int idActivite = 1;
    private static int numTab;

    public static String getRoute_gestion_activite() {
        return route_gestion_activite;
    }

    public static void setRoute_gestion_activite(String codeBare) {
        route_gestion_activite = null;
        route_gestion_activite = getUrl()+"androidScanCodeBare?codeBare="+codeBare+"&numTab="+Session.getNumTab()+"&idUser="+Session.getAuth_id();
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context c) {
        context = null;
        context = c;
    }

    public static int getTabEnCours() {
        return tabEnCours;
    }

    public static void setTabEnCours(int tabEnCours) {
        Session.tabEnCours = tabEnCours;
    }

    public static boolean isEmailExist() {
        return emailExist;
    }

    public static void setEmailExist(boolean emailExist) {
        Session.emailExist = emailExist;
    }

    public static JSONObject getResponseHttp() {return responseHttp;}

    public static void setResponseHttp(JSONObject response) {
        //responseHttp = null;
        responseHttp = response;
    }

    public static int getNumTab() {
        return numTab;
    }

    public static void setNumTab(int numTab) {
        Session.numTab = numTab;
    }

    public static String getAuth_name() {
        return auth_name;
    }

    public static void setAuth_name(String auth_name) {
        Session.auth_name = auth_name;
    }

    public static String getAuth_role() {
        return auth_role;
    }

    public static void setAuth_role(String auth_role) {
        Session.auth_role = auth_role;
    }

    public static String getAuth_photo() {
        return auth_photo;
    }

    public static void setAuth_photo(String auth_photo) {
        Session.auth_photo = auth_photo;
    }

    public static int getNbr_perso() {
        return nbr_perso;
    }

    public static void setNbr_perso(int nbr_perso) {
        Session.nbr_perso = nbr_perso;
    }

    public static boolean isConnecter() {
        return connecter;
    }

    public static void setConnecter(boolean connecter) {
        Session.connecter = connecter;
    }

    public static String getInfoScan() {
        return infoScan;
    }

    public static void setInfoScan(String infoScan) {
        Session.infoScan = infoScan;
    }

    public static String getAnneeAcademique() {
        return anneeAcademique;
    }

    public static void setAnneeAcademique(String anneeAcademique) {
        Session.anneeAcademique = anneeAcademique;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Session.url = url;
    }

    public static String getRoute_login() {
        return route_login;
    }

    public static void setRoute_login(String route_login) {
        Session.route_login = route_login;
    }

    public static String getRoute_reset_login() {
        return route_reset_login;
    }

    public static void setRoute_reset_login(String route_reset_login) {
        Session.route_reset_login = route_reset_login;
    }

    public static String getRoute_register() {
        return route_register;
    }

    public static void setRoute_register(String route_register) {
        Session.route_register = route_register;
    }

    public static String getRoute_verifierEmail() {
        return route_verifierEmail;
    }

    public static void setRoute_verifierEmail(String route_verifierEmail) {
        Session.route_verifierEmail = route_verifierEmail;
    }

    public static String getRoute_get_etutEnSalle(int statut) {
        return route_get_etutEnSalle+statut;
    }

    public static void setRoute_get_etutEnSalle(String route_get_etutEnSalle) {
        Session.route_get_etutEnSalle = route_get_etutEnSalle;
    }


    public static String getRoute_get_listeEtud() {
        return route_get_listeEtud;
    }

    public static void setRoute_get_listeEtud(String route_get_listeEtud) {
        Session.route_get_listeEtud = route_get_listeEtud;
    }

    public static String getRoute_add_etutEnSalle() {
        return route_add_etutEnSalle;
    }

    public static void setRoute_add_etutEnSalle(String route_add_etutEnSalle) {
        Session.route_add_etutEnSalle = route_add_etutEnSalle;
    }

    public static String getRoute_add_etutAyantTerminer() {
        return route_add_etutAyantTerminer;
    }

    public static void setRoute_add_etutAyantTerminer(String route_add_etutAyantTerminer) {
        Session.route_add_etutAyantTerminer = route_add_etutAyantTerminer;
    }

    public static String getRoute_add_etutExclus() {
        return route_add_etutExclus;
    }

    public static void setRoute_add_etutExclus(String route_add_etutExclus) {
        Session.route_add_etutExclus = route_add_etutExclus;
    }

    public static String getRoute_sentMsg() {
        return route_sentMsg;
    }

    public static void setRoute_sentMsg(String route_sentMsg) {
        Session.route_sentMsg = route_sentMsg;
    }

    public static String getRoute_getInfoUser() {
        return route_getInfoUser;
    }

    public static void setRoute_getInfoUser(String route_getInfoUser) {
        Session.route_getInfoUser = route_getInfoUser;
    }

    public static int getAuth_id() {
        return auth_id;
    }

    public static void setAuth_id(int auth_id) {
        Session.auth_id = auth_id;
    }

    public static String getAuth_sexe() {
        return auth_sexe;
    }

    public static void setAuth_sexe(String auth_sexe) {
        Session.auth_sexe = auth_sexe;
    }

    public static String getAuth_address() {
        return auth_address;
    }

    public static void setAuth_address(String auth_address) {
        Session.auth_address = auth_address;
    }

    public static int getIdActivite() {
        return idActivite;
    }

    public static void setIdActivite(int idActivite) {
        Session.idActivite = idActivite;
    }


    /**
     * redimentionner une image
     * @param bm
     * @param w
     * @param h
     * @return
     */
    public Bitmap redimentionnerBitmap(Bitmap bm, int w, int h){
        int width = bm.getWidth();
        int height = bm.getHeight();
        /*int newWidth = w;
        int newHeight = h;*/
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
         return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

    }

    public static String getHttpResponse(URI uri2) {
        StringBuilder response = new StringBuilder();
        try {
            HttpGet get = new HttpGet();
            get.setURI(uri2);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.d("[GET REQUEST]", "HTTP Get succeeded");

                HttpEntity messageEntity = httpResponse.getEntity();
                InputStream is = messageEntity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (Exception e) {
            Log.e("[GET REQUEST]", e.getMessage());
        }
        Log.d("[GET REQUEST]", "Done with HTTP getting");
        return response.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
