package edu.byu.cs.tweeter.client.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Contains utility methods for reading byte arrays.
 */
public class ByteArrayUtils implements Serializable {




        /**
         * Reads the bytes from the specified urlString.
         *
         * @param urlString the url where the bytes to be read reside.
         * @return the bytes.
         * @throws IOException if an I/O error occurs while attempting to open the URL or read from it's
         * input stream.
         */
    public static byte [] bytesFromUrl(String urlString) throws IOException {


        InputStream URLcontent = null;
        HttpURLConnection connection = null;

        try {

                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    return bytesFromInputStream(inputStream);
                } else {
                    throw new IOException("Unable to read from url. Response code: " + connection.getResponseCode());
                }
//            }
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String StrinfFromByteArray(String url) throws IOException {
        byte [] bytes = bytesFromUrl(url);
        String imageString = Base64.getEncoder().encodeToString(bytes);
        return imageString;
    }

    /**
     * Reads the bytes from the specified input stream.
     *
     * @param inputStream the stream where the bytes to be read reside.
     * @return the bytes.
     * @throws IOException if an I/O error occurs while attempting to read from the stream.
     */
    public static byte [] bytesFromInputStream(InputStream inputStream) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int numbRead;
        byte[] data = new byte[1024];
        while ((numbRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, numbRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }
}
