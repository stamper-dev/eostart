package wannabit.io.eoswallet.crypto;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

import wannabit.io.eoswallet.utils.WLog;

public class WannabitKeyStore {

    private static final String KEYSTORE = "AndroidKeyStore";
    private static final String TYPE_RSA = "RSA";
    private static final String SIGNATURE_SHA256withRSA = "SHA256withRSA";
//    private static String mAlias = "EOS";



    public static void createKeys(Context context, String alias) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 1);
        KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance(TYPE_RSA, KEYSTORE);
        AlgorithmParameterSpec spec;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=" + alias))
                    .setSerialNumber(BigInteger.valueOf(0742))
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();


        } else {
            spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN)
                    .setCertificateSubject(new X500Principal("CN=" + alias))
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setCertificateSerialNumber(BigInteger.valueOf(0742))
                    .setCertificateNotBefore(start.getTime())
                    .setCertificateNotAfter(end.getTime())
                    .build();
        }

        kpGenerator.initialize(spec);
        kpGenerator.generateKeyPair();
    }


    public static String signData(String inputStr, String alias) throws KeyStoreException,
            UnrecoverableEntryException, NoSuchAlgorithmException, InvalidKeyException,
            SignatureException, IOException, CertificateException {

        byte[] data = inputStr.getBytes();
        KeyStore ks = KeyStore.getInstance(KEYSTORE);
        ks.load(null);
        KeyStore.Entry entry = ks.getEntry(alias, null);

        if (entry == null) {
//            WLog.w("No key found under alias: " + alias);
//            WLog.w("Exiting signData()...");
            return null;
        }

        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
//            WLog.w("Not an instance of a PrivateKeyEntry");
//            WLog.w("Exiting signData()...");
            return null;
        }
        Signature s = Signature.getInstance(SIGNATURE_SHA256withRSA);
        s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());

        s.update(data);
        byte[] signature = s.sign();
        String result = Base64.encodeToString(signature, Base64.DEFAULT);

        return result;
    }

    public static boolean verifyData(String input, String signatureStr, String alias) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException,
            UnrecoverableEntryException, InvalidKeyException, SignatureException {
        byte[] data = input.getBytes();
        byte[] signature;

        if (signatureStr == null) {
//            WLog.w("Invalid signature.");
//            WLog.w("Exiting verifyData()...");
            return false;
        }

        try {
            signature = Base64.decode(signatureStr, Base64.DEFAULT);
        } catch (IllegalArgumentException e) {
            return false;
        }

        KeyStore ks = KeyStore.getInstance(KEYSTORE);
        ks.load(null);
        KeyStore.Entry entry = ks.getEntry(alias, null);
        if (entry == null) {
//            WLog.w("No key found under alias: " + alias);
//            WLog.w("Exiting verifyData()...");
            return false;
        }

        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
//            WLog.w("Not an instance of a PrivateKeyEntry");
            return false;
        }

        Signature s = Signature.getInstance(SIGNATURE_SHA256withRSA);

        s.initVerify(((KeyStore.PrivateKeyEntry) entry).getCertificate());
        s.update(data);
        return s.verify(signature);
    }
}
