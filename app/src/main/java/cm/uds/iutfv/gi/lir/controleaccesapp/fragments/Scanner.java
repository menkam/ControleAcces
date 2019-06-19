package cm.uds.iutfv.gi.lir.controleaccesapp.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cm.uds.iutfv.gi.lir.controleaccesapp.R;
import cm.uds.iutfv.gi.lir.controleaccesapp.Session;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentIntegrator;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentResult;

public class Scanner extends Activity implements OnClickListener{

    private Button scanBtn;
    private TextView info_user;
    private TextView message,matricule,nom,prenom,matiere,regime,msg;
    private int id_connect;
    private ImageView image;
    private FrameLayout framResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scanner);

        // fixation de variables aux elements
        scanBtn = (Button)findViewById(R.id.btn_scan);
        framResult = (FrameLayout)findViewById(R.id.frameResult);
        info_user = (TextView)findViewById(R.id.info_user);
        message = (TextView)findViewById(R.id.titreInscription);
        matricule = (TextView)findViewById(R.id.tv_matricule);
        nom = (TextView)findViewById(R.id.tv_nom);
        prenom = (TextView)findViewById(R.id.tv_prenom);
        matiere = (TextView)findViewById(R.id.tv_matiere);
        regime = (TextView)findViewById(R.id.tv_regime);
        msg = (TextView)findViewById(R.id.tv_msg);
        info_user = (TextView)findViewById(R.id.info_user);
        //btn_retourScan = (Button)findViewById(R.id.btn_retourScan);
        image = (ImageView)findViewById(R.id.image);

        //info_user.setText(Session.getSession_info_user());
        info_user.setBackgroundColor(Color.BLUE);

        //framResult.setVisibility(View.GONE);

        scanBtn.setOnClickListener(this);
        //btnAnalyseScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId()==R.id.btn_scan){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            if(scanFormat != null){
                cleanValue();
                Session.setRoute_gestion_activite(scanContent);
                ScanAsynctask testAsynctask = new ScanAsynctask();
                String url = Session.getRoute_gestion_activite();
                testAsynctask.execute(new String[]{url});
            }else{ Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show(); }
        }else{ Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show(); }
    }

    public class ScanAsynctask extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject jsonObject = null;
            for(String url : urls){
                StringBuffer reponseHTTP = new StringBuffer();
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = null;
                try {
                    response = client.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if(statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        InputStream content = entity.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) { reponseHTTP.append(line); }
                        jsonObject = new JSONObject(reponseHTTP.toString());
                    }else{ Toast.makeText(getApplicationContext(), "Code = "+statusCode, Toast.LENGTH_LONG).show(); }
                } catch (IOException e) { Toast.makeText(getApplicationContext(), "erreur de ccreation du clientHttp : "+e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) { Toast.makeText(getApplicationContext(), "erreur de conversion JSON : "+e.getMessage(), Toast.LENGTH_LONG).show();}
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            try {
                if(jsonObject.getInt("statut") == 1){
                    message.setBackgroundColor(Color.GREEN);
                    message.setText(R.string.message3);
                    matricule.setText("Matricule : "+jsonObject.getString("matricule_etudiant"));
                    nom.setText("Nom : "+jsonObject.getString("name"));
                    prenom.setText("Prenom : "+jsonObject.getString("prenom"));
                    matiere.setText("matiere : "+jsonObject.getString("libelle_matiere"));
                    regime.setText("Regime : "+jsonObject.getString("regime"));
                    msg.setText("Commentaire : "+jsonObject.getString("msg"));

                    byte[] decodedString = Base64.decode(jsonObject.getString("photo"), Base64.DEFAULT);
                    Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image.setImageBitmap(bm);

                }else if(jsonObject.getInt("statut") == 0){
                    message.setBackgroundColor(Color.RED);
                    message.setText(R.string.message1);

                    matricule.setText("Matricule : ");
                    nom.setText("Nom : ");
                    prenom.setText("Prenom : ");
                    matiere.setText("matiere : ");
                    regime.setText("Regime : ");

                }else if(jsonObject.getInt("statut") == 2){
                    message.setBackgroundColor(Color.CYAN);
                    message.setText(R.string.message2);
                    matricule.setText("Matricule : "+jsonObject.getString("matricule_etudiant"));
                    nom.setText("Nom : "+jsonObject.getString("name"));
                    prenom.setText("Prenom : "+jsonObject.getString("prenom"));
                    matiere.setText("matiere : "+jsonObject.getString("libelle_matiere"));
                    regime.setText("Regime : "+jsonObject.getString("regime"));
                    msg.setText("Commentaire : "+jsonObject.getString("msg"));

                    byte[] decodedString = Base64.decode(jsonObject.getString("photo"), Base64.DEFAULT);
                    Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image.setImageBitmap(bm);
                }
            } catch (JSONException e) { Toast.makeText(getApplicationContext(), "erreur de lecture : "+e.getMessage(), Toast.LENGTH_LONG).show(); }
        }
    }

    public void cleanValue(){
        message.setBackgroundColor(Color.GRAY);
        message.setText(R.string.message);
        matricule.setText("Matricule : ");
        nom.setText("Nom : ");
        prenom.setText("Prenom : ");
        matiere.setText("matiere : ");
        regime.setText("Regime : ");
        image.setImageBitmap(null);
        msg.setText("Commentaire :");
    }
}