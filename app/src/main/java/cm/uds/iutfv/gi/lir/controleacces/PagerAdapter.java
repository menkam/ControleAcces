package cm.uds.iutfv.gi.lir.controleacces;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsAyantTerminer;
import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsEnSalle;
import cm.uds.iutfv.gi.lir.controleacces.fragments.EtudiantsExclus;
import cm.uds.iutfv.gi.lir.controleacces.fragments.ListeEtudiants;
import cm.uds.iutfv.gi.lir.controleacces.recycler.Etudiants;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

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
        return mNumOfTabs;
    }
}