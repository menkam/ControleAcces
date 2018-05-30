package cm.uds.iutfv.gi.lir.controleacces;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;

import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsAyantTerminer;
import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsEnSalle;
import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsExclus;
import cm.uds.iutfv.gi.lir.controleacces.fragments.ListeEtudiants;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentIntegrator;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity  {

    MaterialViewPager materialViewPager;
    View headerLogo;
    ImageView headerLogoContent;
    public TextView auth_role;
    public TextView auth_name;
    public ImageView auth_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        auth_role = (TextView) findViewById(R.id.auth_role);
        auth_name = (TextView) findViewById(R.id.auth_name);
        auth_photo = (ImageView) findViewById(R.id.auth_photo);

        //auth_role.setText("menkam");
        //auth_name.setText("francis");
        //auth_photo.setText(Session.getAuth_role());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        //4 onglets
        final int tabCount = 4;

        //les vues définies dans @layout/header_logo
        headerLogo = findViewById(R.id.headerLogo);
        headerLogoContent = (ImageView) findViewById(R.id.headerLogoContent);


        //le MaterialViewPager
        this.materialViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        this.materialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            /*@Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return RecyclerViewFragment.newInstance();
                    case 1:
                        return RecyclerViewFragment.newInstance();
                    case 2:
                        return RecyclerViewFragment.newInstance();
                    case 3:
                        return RecyclerViewFragment.newInstance();
                    default:
                        return null;
                }
            }*/
            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        EtudiantsEnSalle tab1 = new EtudiantsEnSalle();
                        return tab1;
                    case 1:
                        EtudiantsAyantTerminer tab2 = new EtudiantsAyantTerminer();
                        return tab2;
                    case 2:
                        EtudiantsExclus tab3 = new EtudiantsExclus();
                        return tab3;
                    case 3:
                        ListeEtudiants tab4 = new ListeEtudiants();
                        return tab4;
                    default:
                        return null;
                }
            }


            @Override
            public int getCount() {
                return tabCount;
            }

            //le titre à afficher pour chaque page
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getResources().getString(R.string.etudiant_en_salle);
                    case 1:
                        return getResources().getString(R.string.etudiant_ayant_terminer);
                    case 2:
                        return getResources().getString(R.string.etudiant_exclus);
                    case 3:
                        return getResources().getString(R.string.list_etudiant);
                    default:
                        return "Page " + position;
                }
            }


            int oldItemPosition = -1;
            @SuppressLint("ResourceAsColor")
            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

                //seulement si la page est différente
                if (oldItemPosition != position) {
                    oldItemPosition = position;

                    //définir la nouvelle couleur et les nouvelles images
                    Drawable image = null;
                    int color = Color.BLACK;
                    Drawable newDrawable = null;

                    switch (position) {
                        case 0:
                            image = getDrawable(R.drawable.img_en_salle);
                            color = getResources().getColor(R.color.purple);
                            newDrawable = getResources().getDrawable(R.drawable.ticket);
                            fab.setImageResource(R.color.colorPrimary);
                            fab.setImageResource(R.drawable.ic_add_user);
                            fab.setVisibility(View.VISIBLE);

                            break;
                        case 1:
                            image = getDrawable(R.drawable.img_terminer);
                            color = getResources().getColor(R.color.green);
                            newDrawable = getResources().getDrawable(R.drawable.tennis);
                            fab.setBackgroundColor(R.color.green);
                            fab.setImageResource(R.drawable.ic_user_finish);
                            fab.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            image = getDrawable(R.drawable.img_tricheur);
                            color = getResources().getColor(R.color.orange);
                            newDrawable = getResources().getDrawable(R.drawable.light);
                            fab.setBackgroundResource(R.color.colorBtnRemove);
                            fab.setImageResource(R.drawable.ic_remove_user);
                            fab.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            image = getDrawable(R.drawable.img_liste);
                            color = getResources().getColor(R.color.cyan);
                            newDrawable = getResources().getDrawable(R.drawable.earth);
                            fab.setVisibility(View.INVISIBLE);
                            break;
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            scanCode();
                            //setTabEnCours(tab.getPosition());
                        }
                    });

                    //puis modifier les images/couleurs
                    int fadeDuration = 200;
                    materialViewPager.setColor(color, fadeDuration);
                    //materialViewPager.setImageUrl(imageUrl, fadeDuration);
                    materialViewPager.setImageDrawable(image, fadeDuration);
                    toggleLogo(newDrawable,color,fadeDuration);

                }
            }
        });

        //permet au viewPager de garder 4 pages en mémoire (à ne pas utiliser sur plus de 4 pages !)
        this.materialViewPager.getViewPager().setOffscreenPageLimit(tabCount);
        //relie les tabs au viewpager
        this.materialViewPager.getPagerTitleStrip().setViewPager(this.materialViewPager.getViewPager());
    }

    private void toggleLogo(final Drawable newLogo, final int newColor, int duration){

        //animation de disparition
        final AnimatorSet animatorSetDisappear = new AnimatorSet();
        animatorSetDisappear.setDuration(duration);
        animatorSetDisappear.playTogether(
                ObjectAnimator.ofFloat(headerLogo, "scaleX", 0),
                ObjectAnimator.ofFloat(headerLogo, "scaleY", 0)
        );

        //animation d'apparition
        final AnimatorSet animatorSetAppear = new AnimatorSet();
        animatorSetAppear.setDuration(duration);
        animatorSetAppear.playTogether(
                ObjectAnimator.ofFloat(headerLogo, "scaleX", 1),
                ObjectAnimator.ofFloat(headerLogo, "scaleY", 1)
        );

        //après la disparition
        animatorSetDisappear.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                //modifie la couleur du cercle
                ((GradientDrawable) headerLogo.getBackground()).setColor(newColor);

                //modifie l'image contenue dans le cercle
                headerLogoContent.setImageDrawable(newLogo);

                //démarre l'animation d'apparition
                animatorSetAppear.start();
            }
        });

        //démarre l'animation de disparition
        animatorSetDisappear.start();
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
                //if(getTabEnCours()==0) EtudiantsEnSalle.addStudent(getApplicationContext(), scanFormat, scanContent);
                //if(getTabEnCours()==1) EtudiantsAyantTerminer.addStudent(getApplicationContext(), scanFormat, scanContent);
                //if(getTabEnCours()==2) EtudiantsExclus.addStudent(getApplicationContext(), scanFormat, scanContent);
            }
            else Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

}
