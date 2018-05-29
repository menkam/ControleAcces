package cm.uds.iutfv.gi.lir.controleacces.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cm.uds.iutfv.gi.lir.controleacces.R;

public class MyDialogFragment extends DialogFragment{

    // Empty constructor required for DialogFragment
    public MyDialogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scanner, container);

        return view;
    }

}