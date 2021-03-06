package com.laboratorio.pedidos_lab.biometric;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class BiometricManager extends BiometricManagerV23 {

    protected CancellationSignal mCancellationSignal = new CancellationSignal();

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected BiometricManager(final BiometricBuilder biometricBuilder) {
        this.context = biometricBuilder.context;
        this.title = biometricBuilder.title;
        this.subtitle = biometricBuilder.subtitle;
        this.description = biometricBuilder.description;
        this.negativeButtonText = biometricBuilder.negativeButtonText;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authenticate(@NonNull final BiometricCallback biometricCallback) {

        if(title == null) {
            biometricCallback.onBiometricAuthenticationInternalError("El título del diálogo biométrico no puede ser nulo");
            return;
        }


        if(subtitle == null) {
            biometricCallback.onBiometricAuthenticationInternalError("\n" + "El subtítulo del diálogo biométrico no puede ser nulo");
            return;
        }

        if(description == null) {
            biometricCallback.onBiometricAuthenticationInternalError("\n" + "La descripción del cuadro de diálogo biométrico no puede ser nula");
            return;
        }

        if(negativeButtonText == null) {
            biometricCallback.onBiometricAuthenticationInternalError("\n" + "El texto del botón negativo del cuadro de diálogo biométrico no puede ser nulo");
            return;
        }

        if(!BiometricUtils.isSdkVersionSupported()) {
            biometricCallback.onSdkVersionNotSupported();
            return;
        }

        if(!BiometricUtils.isPermissionGranted(context)) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted();
            return;
        }

        if(!BiometricUtils.isHardwareSupported(context)) {
            biometricCallback.onBiometricAuthenticationNotSupported();
            return;
        }

        if(!BiometricUtils.isFingerprintAvailable(context)) {
            biometricCallback.onBiometricAuthenticationNotAvailable();
            return;
        }

        displayBiometricDialog(biometricCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void cancelAuthentication(){
        if(BiometricUtils.isBiometricPromptEnabled()) {
            if (!mCancellationSignal.isCanceled())
                mCancellationSignal.cancel();
        }else{
            if (!mCancellationSignalV23.isCanceled())
                mCancellationSignalV23.cancel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void displayBiometricDialog(BiometricCallback biometricCallback) {
        if(BiometricUtils.isBiometricPromptEnabled()) {
            displayBiometricPrompt(biometricCallback);
        } else {
            displayBiometricPromptV23(biometricCallback);
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private void displayBiometricPrompt(final BiometricCallback biometricCallback) {
        new BiometricPrompt.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButton(negativeButtonText, context.getMainExecutor(), (dialogInterface, i) -> {
                    biometricCallback.onAuthenticationCancelled();
                    System.exit(0);
                })
                .build()
                .authenticate(mCancellationSignal, context.getMainExecutor(),
                        new BiometricCallbackV28(biometricCallback));
    }

    public static class BiometricBuilder {

        private String title;
        private String subtitle;
        private String description;
        private String negativeButtonText;

        private final Context context;
        public BiometricBuilder(Context context) {
            this.context = context;
        }

        public BiometricBuilder setTitle(@NonNull final String title) {
            this.title = title;
            return this;
        }

        public BiometricBuilder setSubtitle(@NonNull final String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BiometricBuilder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }


        public BiometricBuilder setNegativeButtonText(@NonNull final String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public BiometricManager build() {
            return new BiometricManager(this);
        }
    }
}
