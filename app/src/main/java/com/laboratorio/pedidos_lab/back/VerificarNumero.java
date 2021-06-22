package com.laboratorio.pedidos_lab.back;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.laboratorio.pedidos_lab.controler.ActualizarUsuario;
import com.laboratory.views.R;
import com.laboratory.views.databinding.VerificarNumeroBinding;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

public class VerificarNumero extends AppCompatActivity {

    private VerificarNumeroBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;
    public static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pg;
    EditText code;
    TextView tvCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= VerificarNumeroBinding.inflate(getLayoutInflater());
        setContentView(R.layout.verificar_numero);
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        code = findViewById(R.id.code);
        tvCuenta = findViewById(R.id.cuentaAtras);
        iniciarCuentaAtras();

        String phoneNo = getIntent().getStringExtra("phoneNo");
        binding.phoneEt.setText(phoneNo);

        firebaseAuth = FirebaseAuth.getInstance();

        pg = new ProgressDialog(this);
        pg.setTitle("Por favor espere...");

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                singInWithAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {

                pg.dismiss();
                Toast.makeText(VerificarNumero.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCodeSent(@NonNull @NotNull String verificaTionId, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificaTionId, forceResendingToken);

                Log.d(TAG, "onCodeSent: "+ verificaTionId);

                mVerificationId = verificaTionId;
                forceResendingToken = token;
                pg.dismiss();

                Toast.makeText(VerificarNumero.this, "Verificación enviada", Toast.LENGTH_SHORT).show();

            }
        };

        binding.phoneContinueButton.setOnClickListener(v -> {

            String phone = binding.phoneEt.getText().toString().trim();
            if (TextUtils.isEmpty(phone)){
                Toast.makeText(VerificarNumero.this, "Por favor ingrese el código.", Toast.LENGTH_SHORT).show();
            }
            else {
                starPhoneNumberVerification(phone);
            }
        });

        String phone1 = binding.phoneEt.getText().toString().trim();
        starPhoneNumberVerification(phone1);

        binding.resendCodeTv.setOnClickListener(v -> {

            String phone = binding.phoneEt.getText().toString().trim();
            if (TextUtils.isEmpty(phone)){
                Toast.makeText(VerificarNumero.this, "Por favor ingrese el código.", Toast.LENGTH_SHORT).show();
            }
            else {
                resendVerificationCode(phone, forceResendingToken);
                iniciarCuentaAtras();
            }
        });

        binding.codeSubmitButton.setOnClickListener(v -> {

            String code = binding.CodeEt.getText().toString().trim();
            if (TextUtils.isEmpty(code)){
                Toast.makeText(VerificarNumero.this, "Por favor ingrese el código.", Toast.LENGTH_SHORT).show();
            }
            else {
                verifyPhoneNumberWithCode(mVerificationId, code);
                //new ActualizarUsuario(this).execute();

            }
        });

    }

    private void iniciarCuentaAtras() {

        new CountDownTimer(2000*60, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvCuenta.setText("El codigo caduca en: "+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tvCuenta.setText("El código ha caducado");
            }
        }.start();

        /*Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);*/

    }

    private void starPhoneNumberVerification(String phone){

        pg.setMessage("Verificando número de teléfono, por favor espere...");
        pg.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+503"+phone)
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token){

        pg.setMessage("Reenviando código, por favor espere...");
        pg.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+503"+phone)
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {

        pg.setMessage("Verificando código, por favor espere...");
        pg.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        singInWithAuthCredential(credential);

    }

    private void singInWithAuthCredential(PhoneAuthCredential credential){

        pg.setMessage("Ingresando");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    pg.dismiss();
                    String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    Toast.makeText(VerificarNumero.this, "Ingreso con"+phone, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    i.putExtra("phoneEnv", binding.phoneEt.getText().toString());
                    startActivity(i);


                })
                .addOnFailureListener(e -> {
                    pg.dismiss();
                    Toast.makeText(VerificarNumero.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

    @Override
    public void onBackPressed() {

    }
}