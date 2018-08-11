package com.example.gjwls.whisperer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SpeechRecognizeListener,TextToSpeechListener {
    private final int CODE_PERMISSIONS = 0;//...

    private TextToSpeechClient ttsClient;
    private TextView mStatus;
    private SpeechRecognizerClient client;
    IALocationManager mLocationManager;

    IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            TextView txtLoc = (TextView)findViewById(R.id.textView);
            Log.e(this.getClass().getName(),String.valueOf("---------------------------------"+iaLocation.getLatitude() +","+iaLocation.getLongitude()));
            txtLoc.setText(String.valueOf(iaLocation.getLatitude() +","+iaLocation.getLongitude()+" , "+iaLocation.getBearing()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions( this, neededPermissions, CODE_PERMISSIONS );

        mStatus = (TextView) findViewById(R.id.textView);

        mLocationManager = IALocationManager.create(this);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());
        findViewById(R.id.button2).setOnClickListener(this);

        //Log.e(this.getClass().getName(),"☆★☆★"+getKeyHash(this));
    }
    @Override
    protected void onResume(){
        super.onResume();
        mLocationManager.requestLocationUpdates(IALocationRequest.create(),mLocationListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mLocationManager.removeLocationUpdates(mLocationListener);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationManager.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Handle if any of the permissions are denied, in grantResults
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;

        if (id == R.id.button2) {
//            if(PermissionUtils.checkAudioRecordPermission(this)) {
//
//                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
//                        setServiceType(serviceType);
//
//                if (serviceType.equals(SpeechRecognizerClient.SERVICE_TYPE_WORD)) {
//                }
//
//                client = builder.build();
//
//                client.setSpeechRecognizeListener(this);
//                client.startRecording(true);
//            }

            if (ttsClient != null && ttsClient.isPlaying()) {
                ttsClient.stop();
                return;
            }

            TextView textView = (TextView) findViewById(R.id.textView);
            String strText = textView.getText().toString();

            String speechMode = TextToSpeechClient.NEWTONE_TALK_2;

            String voiceType = TextToSpeechClient.VOICE_WOMAN_DIALOG_BRIGHT;;

            double speechSpeed = 1.0D;

//                ttsClient = new TextToSpeechClient.Builder()
//                        .setApiKey(SpeechSampleActivity.APIKEY)
//                        .setSpeechMode(speechMode)
//                        .setSpeechSpeed(speechSpeed)
//                        .setSpeechVoice(voiceType)
//                        .setListener(TextToSpeechActivity.this)
//                        .build();

            ttsClient = new TextToSpeechClient.Builder()
                    .setSpeechMode(speechMode)
                    .setSpeechSpeed(speechSpeed)
                    .setSpeechVoice(voiceType)
                    .setListener(MainActivity.this)
                    .build();

            if(ttsClient.play(strText))
                mStatus.setText("playTTS");
        }
    }



    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        Log.e("SpeechSampleActivity", "onError");

        ttsClient = null;
        client = null;
    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    @Override
    public void onResults(Bundle results) {

        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");

        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        for (int i = 0; i < texts.size(); i++) {
            builder.append(texts.get(i));
            builder.append(" (");
            builder.append(confs.get(i).intValue());
            builder.append(")\n");
        }

        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // finishing일때는 처리하지 않는다.
                if (activity.isFinishing()) return;

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity).
                        setMessage(builder.toString()).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        });

        client = null;
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {
        int intSentSize = ttsClient.getSentDataSize();
        int intRecvSize = ttsClient.getReceivedDataSize();
        Log.i("SpeechSampleActivity", "onFinished");

        final String strInacctiveText = "onFinished() SentSize : " + intSentSize + " RecvSize : " + intRecvSize;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setText(strInacctiveText);
            }
        });

        ttsClient = null;
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (android.content.pm.Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("1", "☆★☆★☆★☆★☆★Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}