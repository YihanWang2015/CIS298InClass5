package edu.kvcc.cis298.inclass3.inclass3;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ywang4241 on 12/7/2015.
 */
public class CrimeFetcher {



    private static final String TAG = "CrimeFetcher";

  //  private static final String API_KEY = "YourApiKeyHere";

    //Method to get the raw bytes from the web source.
    // Conversion from bytes to something more meaningful will happen in a different method.
    // The method has one parameter which is the url that we want to connect to.

    private byte[] getUrlBytes(String urlSpec) throws IOException{

        //Create a new URL object from the url string that was passed in.
        URL url = new URL(urlSpec);

        //Create a new HTTP connection to the specified url.
        //If we were to load data from a secure site, it would need to use HttpsURLConnection.
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{

            //Create a output stream to hold that data that is read from the url source.
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //create a input stream from the http connection.
            InputStream in = connection.getInputStream();

            //Check to see that the response code from the http request is 200, which is the same as http_ok.
            //Every web request will return some sort of response code. We can google them.
            //Typically 200's = good, 300's = cache, 400's = error, 500's = server error.
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                ": with " +
                urlSpec);
            }

            //create an int to hold how many bytes were read in.
            int bytesRead = 0;

            //create a byte array to act as a buffer that will read
            //in up to 1024 bytes at a time.
            byte[] buffer = new byte[1024];

            //while we can read bytes from the input stream
            while ((bytesRead = in.read(buffer)) > 0){

                //write the bytes out to the output stream
                out.write(buffer, 0, bytesRead);
            }

            //once everything has been read and written, close the output stream
            out.close();

            //convert the output stream to a byte array.
            return out.toByteArray();

        } finally{

            //make sure the connection to the web is closed.
            connection.disconnect();
        }

    }

    //Method to get the string result from the http web address
    //The url bytes representing the data get returned from the getUrlBytes method,
    //and are then transformed into a string.

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    //Method to fetch the crimes from the web url and parse them into crimes
    public void fetchCrimes(){
        try{
            //This method will take the original URL and allow us to add
            //any parameters that might be required to it.
            //For the URL's on my server there are no additional parameters need.
            //However many API's require extra parameters.
            //The API that the book uses requires extra parameters and this is where they add them.
            String url = Uri.parse("http://barnesbrothers.homeserver.com/crimeapi")
                    .buildUpon()

                            //Add extra parameters here.
                            //.appendQueryParamether("param", "Value")

                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

        }catch(IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);

        }
    }
}
