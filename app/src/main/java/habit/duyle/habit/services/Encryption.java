package habit.duyle.habit.services;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import habit.duyle.habit.Activities.MainActivity;

/**
 * Created by Duy Le on 12/30/2016.
 */

public class Encryption {
    private String TAG = "TAG";

    private KeyStore keyStore;
    private String dataDirectory;
    private String filesDirectory;
    private String encryptedDataFilePath;
    private Cipher inCipher;
    private Cipher outCipher;
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Encryption(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            String alias = "password";
            // Create the keys if necessary
            if (!keyStore.containsAlias(alias)) {

                Calendar notBefore = Calendar.getInstance();
                Calendar notAfter = Calendar.getInstance();
                notAfter.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(MainActivity.getMainActivity().getBaseContext())
                        .setAlias(alias)
                        .setKeyType(KeyProperties.KEY_ALGORITHM_RSA)
                        .setKeySize(2048)
                        .setSubject(new X500Principal("CN=test"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(notBefore.getTime())
                        .setEndDate(notAfter.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                KeyPair keyPair = generator.generateKeyPair();
            }
            // Retrieve the keys
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
            publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            // Encrypt the text

            dataDirectory = MainActivity.getMainActivity().getApplicationInfo().dataDir;
            filesDirectory = MainActivity.getMainActivity().getAppFolder().getAbsolutePath();
            encryptedDataFilePath = filesDirectory + File.separator + "keep_yer_secrets_here";
            inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            outCipher.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (NoSuchProviderException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (KeyStoreException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (CertificateException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (UnrecoverableEntryException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (NoSuchPaddingException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (InvalidKeyException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void encryptPassword(String password){
        try{
            MainActivity.getMainActivity().showProgressBar();
            CipherOutputStream cipherOutputStream =
                    new CipherOutputStream(
                            new FileOutputStream(encryptedDataFilePath), inCipher);
            cipherOutputStream.write(password.getBytes("UTF-8"));
            cipherOutputStream.close();
            MainActivity.getMainActivity().hideProgressBar();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
    public String decryptPassword(){
        try{
            CipherInputStream cipherInputStream =
                    new CipherInputStream(new FileInputStream(encryptedDataFilePath),
                            outCipher);
            byte [] roundTrippedBytes = new byte[1000]; // TODO: dynamically resize as we get more data

            int index = 0;
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                roundTrippedBytes[index] = (byte)nextByte;
                index++;
            }
            String roundTrippedString = new String(roundTrippedBytes, 0, index, "UTF-8");
            return roundTrippedString;
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }
}
