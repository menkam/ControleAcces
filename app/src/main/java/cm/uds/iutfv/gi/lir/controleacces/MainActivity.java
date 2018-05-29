package cm.uds.iutfv.gi.lir.controleacces;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsAyantTerminer;
import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsEnSalle;
import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsExclus;
import cm.uds.iutfv.gi.lir.controleacces.recycler.Etudiants;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentIntegrator;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentResult;

import static cm.uds.iutfv.gi.lir.controleacces.R.color;
import static cm.uds.iutfv.gi.lir.controleacces.R.drawable;
import static cm.uds.iutfv.gi.lir.controleacces.R.id;
import static cm.uds.iutfv.gi.lir.controleacces.R.layout;
import static cm.uds.iutfv.gi.lir.controleacces.R.string;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int tabEnCours = -1;
    public TextView auth_role;
    public TextView auth_name;
    public ImageView auth_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(layout.activity_main);

        auth_role = (TextView) findViewById(id.auth_role);
        auth_name = (TextView) findViewById(id.auth_name);
        auth_photo = (ImageView) findViewById(id.auth_photo);

        //auth_role.setText("menkam");
        //auth_name.setText("francis");
        //auth_photo.setText(Session.getAuth_role());


        Toolbar toolbar = (Toolbar) findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("En Salle"));
        tabLayout.addTab(tabLayout.newTab().setText("Terminer"));
        tabLayout.addTab(tabLayout.newTab().setText("Exclus"));
        tabLayout.addTab(tabLayout.newTab().setText("Liste"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




        Session.setTabEnCours(4);
        Session.movies4 = Etudiants.getListe(Session.getRoute_get_listeEtud());

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                FloatingActionButton fab = (FloatingActionButton) findViewById(id.fab);
                fab.setVisibility(View.VISIBLE);
                if(tab.getPosition() == 0){
                    fab.setBackgroundResource(color.colorPrimary);
                    fab.setImageResource(drawable.ic_add_user);

                }
                if(tab.getPosition() == 1){
                    fab.setBackgroundColor(color.colorPrimaryDark);
                    fab.setImageResource(drawable.ic_user_finish);
                }
                if(tab.getPosition() == 2) {
                    fab.setBackgroundResource(color.colorBtnRemove);
                    fab.setImageResource(drawable.ic_remove_user);
                }
                //if(tab.getPosition() == 3) fab.setImageResource(R.drawable.ic_list_all_user);
                if(tab.getPosition() == 3) fab.setVisibility(View.INVISIBLE);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scanCode();
                        setTabEnCours(tab.getPosition());
                    }
                });

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void scanCode() {
        //scan
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            if(scanFormat != null && scanContent!= null){
                if(getTabEnCours()==0) EtudiantsEnSalle.addStudent(getApplicationContext(), scanFormat, scanContent);
                if(getTabEnCours()==1) EtudiantsAyantTerminer.addStudent(getApplicationContext(), scanFormat, scanContent);
                if(getTabEnCours()==2) EtudiantsExclus.addStudent(getApplicationContext(), scanFormat, scanContent);
            }
            else Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
    }

    public int getTabEnCours(){ return tabEnCours; }

    public void setTabEnCours(int tabEnCours) { this.tabEnCours = tabEnCours; }
}
