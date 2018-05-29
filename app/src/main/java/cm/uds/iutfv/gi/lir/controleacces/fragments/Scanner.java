package cm.uds.iutfv.gi.lir.controleacces.fragments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cm.uds.iutfv.gi.lir.controleacces.R;
import cm.uds.iutfv.gi.lir.controleacces.Session;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentIntegrator;
import cm.uds.iutfv.gi.lir.zxing.integration.android.IntentResult;


public class Scanner {}