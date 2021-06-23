package com.laboratorio.pedidos_lab.back;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.laboratorio.pedidos_lab.biometric.BiometricCallback;
import com.laboratorio.pedidos_lab.biometric.BiometricManager;
import com.laboratorio.pedidos_lab.front.SplashPrincipal;
import com.laboratorio.pedidos_lab.front.acercaDe;
import com.laboratory.views.R;
import com.pushlink.android.PushLink;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.Serializable;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.sun.activation.registries.LogSupport.log;

public class Login extends AppCompatActivity implements BiometricCallback, Serializable {

    private static final int MY_REQUEST_CODE = 520;
    Button btnEntrar, btnRegistrar;
    EditText user, password;
    CheckBox mostrarPass;
    TextView recuperarPass;
    LottieAnimationView about;
    ImageView logoLabLogin;
    BiometricManager mBiometricManager;
    String URL_USUARIOS = "http://pedidoslab.6te.net/consultas/login.php";
    String URL_CLIENTES = "http://pedidoslab.6te.net/consultas/clientesRequest.php";
    public static int gIdCliente, gIdUsuario, gIdPedido, gIdUsuarioClient, gIdClienteClient, gIdFacDetPedido;
    public static String nombre, email, sexo, nacimiento;
    public static int edad, gusuario, cliente, dui, meses, verificacion;
    String usuario, contra;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //TODO: Bloquear orientación de pantalla.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkForAppUpdate();

        mostrarPass = findViewById(R.id.mostrarPass);
        mostrarPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        about = findViewById(R.id.about);
        about.setOnClickListener(v -> {
            Intent i = new Intent(this, acercaDe.class);
            startActivity(i);
        });

        logoLabLogin = findViewById(R.id.logoLabLogin);
        Glide.with(this).load(SplashPrincipal.gLogoEmpresa).into(logoLabLogin);

        user = findViewById(R.id.user);

        String phoneNo = getIntent().getStringExtra("phoneEnv");
        user.setText(phoneNo);

        password = findViewById(R.id.pass);
        recuperarPass = findViewById(R.id.recuperarPass);
        recuperarPass.setOnClickListener(v -> new FancyGifDialog.Builder(this)
                .setTitle("Por el momento esta función esta temporalmente desabilitada, pero puede enviar un correo a Central Systems para recuperar su contraseña, gracias.")
                .setNegativeBtnText("Cancelar")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Confirmar")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.gif7)
                .isCancellable(false)
                .OnPositiveClicked(() -> {

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{SplashPrincipal.gCorreoEmpresa});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto: Recuperar mi contraseña");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Solicito la recuperación de mi contraseña");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Nombre según registro:\nCorreo de contacto:");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                    startActivity(emailIntent);

                })
                .OnNegativeClicked(() -> Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show())
                .build());

        recuperarDatos();

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RegistroUsuario.class);
            startActivity(i);
        });

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(v -> {
            final ProgressDialog loading = ProgressDialog.show(this, "Procesando...", "Espere por favor");
            datos();
            usuario = user.getText().toString();
            contra = password.getText().toString();

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean succes = jsonResponse.getBoolean("success");
                    if (succes){

                        guardarPreferencias();

                        gusuario = jsonResponse.getInt("id_usuario");
                        nombre = jsonResponse.getString("nombre_usuario");
                        email = jsonResponse.getString("email_usuario");
                        sexo = jsonResponse.getString("sexo_usuario");
                        nacimiento = jsonResponse.getString("nacimiento_usuario");
                        cliente = jsonResponse.getInt("id_cliente");
                        edad = jsonResponse.getInt("edad_usuario");
                        dui = jsonResponse.getInt("dui_usuario");
                        meses = jsonResponse.getInt("meses_usuario");
                        verificacion = jsonResponse.getInt("verificacion");

                        Intent intent = new Intent(getApplicationContext(), DatosPrincipales.class);

                        intent.putExtra("id_usuario", gusuario);
                        intent.putExtra("nombre_usuario", nombre);
                        intent.putExtra("login_usuario", usuario);
                        intent.putExtra("password_usuarios", contra);
                        intent.putExtra("email_usuario", email);
                        intent.putExtra("sexo_usuario", sexo);
                        intent.putExtra("id_cliente", cliente);
                        intent.putExtra("nacimiento_usuario", nacimiento);
                        intent.putExtra("edad_usuario", edad);
                        intent.putExtra("dui_usuario", dui);
                        intent.putExtra("meses_usuario", meses);
                        intent.putExtra("verificacion", verificacion);

                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Usuario y/o contraseña incorrectos o usuario inactivo y/o sin permisos, por favor intentalo nuevamente.", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            };

            loginRequest loginRequest = new loginRequest(usuario, contra, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Login.this);
            queue.add(loginRequest);

        });


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


          /*  mBiometricManager = new BiometricManager.BiometricBuilder(Login.this)
                    .setTitle(getString(R.string.biometric_title))
                    .setSubtitle(getString(R.string.biometric_subtitle))
                    .setDescription(getString(R.string.biometric_description))
                    .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                    .build();

            //start authentication
            mBiometricManager.authenticate(Login.this); */


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    log("Update flow failed! Result code: " + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterInstallStateUpdListener();
        super.onDestroy();
    }


    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo);
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ) {
                    // Start an update.
                    startAppUpdateImmediate(appUpdateInfo);
                }
            }
        });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    Login.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    Login.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {


        Snackbar snackbar =
                Snackbar.make(findViewById(R.id.light), getString(R.string.update_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.restart, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.rosado));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }



    @Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
//        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show();
        mBiometricManager.cancelAuthentication();
    }

    @Override
    public void onAuthenticationSuccessful() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_success), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }

    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  preferences.edit();
        editor.putString("login_usuario", usuario);
        editor.putString("password_usuarios", contra);
        editor.putBoolean("sesion", true);
        editor.apply();
    }

    private void recuperarDatos(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        user.setText(preferences.getString("login_usuario", ""));
        password.setText(preferences.getString("password_usuarios", ""));
    }


    public void datos(){
        RequestQueue requestQueue2 = Volley.newRequestQueue(Login.this);
        StringRequest request2 = new StringRequest(Request.Method.GET, URL_USUARIOS,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Usuarios");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            gIdUsuario = jsonObject1.getInt("id_usuario");
                            gIdCliente = jsonObject1.getInt("id_cliente");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
        };
        requestQueue2.add(request2);
    }

    public void clientes(){
        RequestQueue requestQueue2 = Volley.newRequestQueue(Login.this);
        StringRequest request2 = new StringRequest(Request.Method.GET, URL_CLIENTES,

                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Usuarios");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            gIdUsuarioClient = jsonObject1.getInt("id_usuario");
                            gIdClienteClient = jsonObject1.getInt("id_cliente");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
        };
        requestQueue2.add(request2);
    }

    @Override
    public void onBackPressed(){
        new FancyGifDialog.Builder(this)
                .setTitle("Cerrar aplicación?")
                .setNegativeBtnText("Cancelar")
                .setPositiveBtnBackground(R.color.rosado)
                .setPositiveBtnText("Cerrar")
                .setNegativeBtnBackground(R.color.rojo)
                .setGifResource(R.drawable.gif8)
                .isCancellable(false)
                .OnPositiveClicked(() -> {

                    finishAffinity();
                    finishActivity(0);
                    System.exit(0);

                })
                .OnNegativeClicked(() -> Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show())
                .build();

    }

    public class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Login.this.getPackageName()  + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            //int currentVersion = Integer.parseInt("1.3.6");

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

              /*  if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show anything
                } */

            }

            //Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

        }
    }


}
