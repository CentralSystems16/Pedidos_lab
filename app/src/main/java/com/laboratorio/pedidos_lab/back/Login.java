package com.laboratorio.pedidos_lab.back;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.laboratorio.pedidos_lab.biometric.BiometricCallback;
import com.laboratorio.pedidos_lab.biometric.BiometricManager;
import com.laboratorio.pedidos_lab.front.SplashPrincipal;
import com.laboratorio.pedidos_lab.front.acercaDe;
import com.laboratory.views.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity implements BiometricCallback, Serializable {

    Button btnEntrar, btnRegistrar;
    EditText user, password;
    CheckBox mostrarPass;
    TextView recuperarPass;
    ImageButton about;
    ImageView logoLabLogin;
    BiometricManager mBiometricManager;
    public static int gIdCliente, gIdUsuario, gIdPedido, gIdFacDetPedido, edad, dui, meses;
    public static String nombre, email, sexo, nacimiento, direccion, latitud, longitud, usuario, contra, pass, repeatContra;
    private FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //TODO: Bloquear orientación de pantalla.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        HashMap<String, Object> defaultsRate = new HashMap<>();
        defaultsRate.put("Nueva version disponible",String.valueOf(getVersionCode()));

        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(10)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(defaultsRate);

        remoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                final String new_version_code_ = remoteConfig.getString("new_version_code");
                if (Integer.parseInt(new_version_code_)>getVersionCode()){
                    showDialog();
                }

            }
        });

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
                    //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                    startActivity(emailIntent);

                })
                .OnNegativeClicked(() -> Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT))
                .build());

        recuperarDatos();

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RegistroUsuario.class);
            startActivity(i);
        });

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(SplashPrincipal.gRed, SplashPrincipal.gGreen, SplashPrincipal.gBlue)));
        btnEntrar.setOnClickListener(v -> {
            final ProgressDialog loading = ProgressDialog.show(this, "Procesando...", "Espere por favor");
            usuario = user.getText().toString();
            contra = password.getText().toString();

            Response.Listener<String> responseListener = response -> {

                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean succes = jsonResponse.getBoolean("success");

                    if (succes){
                        guardarPreferencias();
                        gIdUsuario = jsonResponse.getInt("id_usuario");
                        nombre = jsonResponse.getString("nombre_usuario");
                        email = jsonResponse.getString("email_usuario");
                        pass = jsonResponse.getString("password_usuarios");
                        repeatContra = jsonResponse.getString("password_repeat_usuario");
                        sexo = jsonResponse.getString("sexo_usuario");
                        gIdCliente = jsonResponse.getInt("id_cliente");
                        nacimiento = jsonResponse.getString("nacimiento_usuario");
                        edad = jsonResponse.getInt("edad_usuario");
                        dui = jsonResponse.getInt("dui_usuario");
                        meses = jsonResponse.getInt("meses_usuario");
                        direccion = jsonResponse.getString("direccion_usuario");
                        latitud = jsonResponse.getString("latitud");
                        longitud = jsonResponse.getString("longitud");

                        Intent intent = new Intent(getApplicationContext(), DatosPrincipales.class);
                        intent.putExtra("id_usuario", gIdUsuario);
                        intent.putExtra("nombre_usuario", nombre);
                        intent.putExtra("email_usuario",email);
                        intent.putExtra("password_repeat_usuario",repeatContra);
                        intent.putExtra("sexo_usuario",sexo);
                        intent.putExtra("id_cliente",gIdCliente);
                        intent.putExtra("nacimiento_usuario",nacimiento);
                        intent.putExtra("edad_usuario",edad);
                        intent.putExtra("dui_usuario",dui);
                        intent.putExtra("meses_usuario",meses);
                        intent.putExtra("direccion_usuario",direccion);
                        intent.putExtra("latitud", latitud);
                        intent.putExtra("longitud", longitud);
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

    private void showDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nueva versión disponible!!!")
                .setMessage("Por favor, actualiza a la última versión para recibir nuevas y mejores funcionalidades.")
                .setPositiveButton("Actualizar", null)
                .show();
        dialog.setCancelable(false);

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.laboratorio.views.debug")));
            } catch (android.content.ActivityNotFoundException anfe){
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=" + "com.laboratorio.views.debug")));

            }
        });
    }

    public int getVersionCode() {

        PackageInfo packageInfo = null;

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
        }catch (PackageManager.NameNotFoundException e){
            Log.i("MyLog", "NameNotFoundException "+e.getMessage());

        }

        return Objects.requireNonNull(packageInfo).versionCode;

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

    @Override
    public void onBackPressed(){
        new FancyGifDialog.Builder(this)
                .setTitle("¿Cerrar aplicación?")
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
                .OnNegativeClicked(() -> Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT))
                .build();


    }
}
