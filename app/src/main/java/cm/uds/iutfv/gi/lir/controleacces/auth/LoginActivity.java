package cm.uds.iutfv.gi.lir.controleacces.auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import android.os.Looper;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cm.uds.iutfv.gi.lir.controleacces.MainActivity;
import cm.uds.iutfv.gi.lir.controleacces.R;
import cm.uds.iutfv.gi.lir.controleacces.Session;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_login);
        Button btnreset = (Button) findViewById(R.id.btn_reset_in_login);
        Button btnregister = (Button) findViewById(R.id.btn_register_in_login);


        btnreset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ResetPassActivity.class);
                startActivity(i);
            }
        });
        btnregister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password and email, if the user entered one.

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email)){
                mEmailView.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
            }
            else {
                mPasswordView.setError(getString(R.string.error_field_required));
                focusView = mPasswordView;
            }
            cancel = true;
        }
        else if (!isEmailValid(email) || !isEmailExist(email) || !isPasswordValid(password) || !isPasswoadExist(email, password)){
            if (!isEmailValid(email) || !isEmailExist(email)) {
                if(!isEmailValid(email)){
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                }else{
                    mEmailView.setError(getString(R.string.error_email_unexist));
                    focusView = mEmailView;
                }
            }
            else if (!isPasswordValid(password) || !isPasswoadExist(email, password)) {
                if (!isPasswordValid(password)){
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                }
                else {
                    mPasswordView.setError(getString(R.string.error_password_unexist));
                    focusView = mPasswordView;
                }
            }
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (email.contains("@yahoo.fr") ||
                email.contains("@yahoo.com") ||
                email.contains("@yahoo.cm") ||
                email.contains("@uds.cm") ||
                email.contains("@gmail.com") ||
                email.contains("@hotmail.fr") );
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    /**
     * Vérification de l'email dans la BD
     * @param email
     * @return
     */
    private boolean isEmailExist(String email) {

        boolean trouver = true;
/*
        StringBuffer reponseHTTP = new StringBuffer();
        HttpClient client = new DefaultHttpClient();
        //String url = Session.route_verifierEmail+email;
        String url = Session.route_verifierEmail+email;
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
                JSONObject jsonObject = new JSONObject(reponseHTTP.toString());

                if(jsonObject.getInt("status")>0){
                    trouver = true;
                    //return true;
                }
                else{
                    trouver = false;
                    //return false;
                }
            }else
                Toast.makeText(getApplicationContext(), "Erreur interne du serveur !!!", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Impossible de joindre le serveur !!!", Toast.LENGTH_LONG).show();
        }
        finally {
            reponseHTTP = null;
        }*/
        return trouver;
    }

    /**
     * verifier dans la BD si le mot de passe correspond au login
     * @param login
     * @param pass
     * @return
     */
    private boolean isPasswoadExist(String login, String pass) {

        boolean trouver = true;
/*
        StringBuffer reponseHTTP = new StringBuffer();
        HttpClient client = new DefaultHttpClient();
        String url = Session.route_login;
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
                JSONObject jsonObject = new JSONObject(reponseHTTP.toString());

                if(jsonObject.getInt("status") == 0){
                    Toast.makeText(getApplicationContext(), "Login / Pass incorrect !!!", Toast.LENGTH_LONG).show();
                    trouver = false;
                }else{
                    Session.setId_connect(jsonObject.getInt("id"));
                    String nom = jsonObject.getString("nom");
                    String prenom = jsonObject.getString("prenom");
                    String sexe = jsonObject.getString("sexe");
                    String role = jsonObject.getString("role");
                    Session.setSession_info_user("Connecter en tant que : "+role+" ("+nom+" "+prenom+")");
                    if(sexe=="M") Toast.makeText(getApplicationContext(), "Bienvenu M. "+nom+" "+prenom, Toast.LENGTH_LONG).show();
                    if(sexe=="F") Toast.makeText(getApplicationContext(), "Bienvenu Mme. "+nom+" "+prenom, Toast.LENGTH_LONG).show();
                    trouver = true;
/*
                    // si Admin
                    if(jsonObject.getInt("status") == 1){
                        Intent i = new Intent(Login.this, AccueilleAdmin.class);
                        startActivity(i);
                        //progressBarConnexion.setVisibility(View.INVISIBLE);
                    }
                    // si Enseignat
                    if(jsonObject.getInt("status") == 2){
                        Intent i = new Intent(Login.this, Scanner.class);
                        startActivity(i);
                        //progressBarConnexion.setVisibility(View.INVISIBLE);
                    }
                    // si Etudiant
                    if(jsonObject.getInt("status") == 3){
                        Toast.makeText(getApplicationContext(), "Attention !!! acces limité pour les étudiants", Toast.LENGTH_LONG).show();
                        //progressBarConnexion.setVisibility(View.INVISIBLE);
                    }
                    // si Surveillant
                    if(jsonObject.getInt("status") == 4){
                        Intent i = new Intent(Login.this, Scanner.class);
                        startActivity(i);
                        //progressBarConnexion.setVisibility(View.INVISIBLE);
                    }
                    // si visiteur
                    if(jsonObject.getInt("status") == 5){
                        Toast.makeText(getApplicationContext(), "votre compte n'est pas activivé bien vouloir contacter l'administrateur", Toast.LENGTH_LONG).show();
                        //progressBarConnexion.setVisibility(View.INVISIBLE);
                    }*/
/*
                }
            }else{
                Toast.makeText(getApplicationContext(), "Code = "+statusCode, Toast.LENGTH_LONG).show();
                //progressBarConnexion.setVisibility(View.INVISIBLE);
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            //progressBarConnexion.setVisibility(View.INVISIBLE);
        }*/

        return trouver;
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                //Thread.sleep(2000);
                // Création d'un thread
                Session.setConnecter(false);
                Thread t = new Thread()
                {


                    public void run()
                    {
                        Looper.prepare();

                        StringBuffer reponseHTTP = new StringBuffer();
                        HttpClient client = new DefaultHttpClient();
                        String url = Session.getRoute_login() +"email="+mEmail+"&password="+mPassword;
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
                                JSONObject jsonObject = new JSONObject(reponseHTTP.toString());

                                if(jsonObject.getInt("status") == 0){
                                    Toast.makeText(getApplicationContext(), "Login / Pass incorrect !!!", Toast.LENGTH_LONG).show();
                                    onPostExecute(false);
                                }else{

                                    // si Admin
                                    if(jsonObject.getInt("status") == 1 || jsonObject.getInt("status") == 2 || jsonObject.getInt("status") == 4){
                                        Session.setConnecter(true);

                                        String nom = jsonObject.getString("nom");
                                        String prenom = jsonObject.getString("prenom");
                                        String sexe = jsonObject.getString("sexe");
                                        String role = jsonObject.getString("role");

                                        Session.setAuth_id(jsonObject.getInt("id"));
                                        Session.setAuth_name(nom+" "+prenom);
                                        Session.setAuth_role(role);

                                        if(sexe=="M") Toast.makeText(getApplicationContext(), "Bienvenu M. "+nom+" "+prenom, Toast.LENGTH_LONG).show();
                                        if(sexe=="F") Toast.makeText(getApplicationContext(), "Bienvenu Mme. "+nom+" "+prenom, Toast.LENGTH_LONG).show();



                                        Toast.makeText(getApplicationContext(), "connecter", Toast.LENGTH_LONG).show();
                                        onPostExecute(true);
                                    }
                                    // si Etudiant
                                    else if(jsonObject.getInt("status") == 3){
                                        Toast.makeText(getApplicationContext(), "Attention !!! acces limité pour les étudiants", Toast.LENGTH_LONG).show();
                                        onPostExecute(false);
                                    }
                                    // si visiteur
                                    else if(jsonObject.getInt("status") == 5){
                                        Toast.makeText(getApplicationContext(), "votre compte n'est pas activivé bien vouloir contacter l'administrateur", Toast.LENGTH_LONG).show();
                                        onPostExecute(false);

                                    }
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Code = "+statusCode, Toast.LENGTH_LONG).show();
                                //progressBarConnexion.setVisibility(View.INVISIBLE);
                            }
                          }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            //progressBarConnexion.setVisibility(View.INVISIBLE);
                        }

                        Looper.loop();
                    }

                };
                t.sleep(1000);
                t.start();

            } catch (Exception e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return Session.isConnecter();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
