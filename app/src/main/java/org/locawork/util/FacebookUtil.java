package org.locawork.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;
import org.locawork.model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicReference;

public class FacebookUtil {

    private AppEventsLogger logger;

    /**
     * Buy
     */
    public void purchase(){
        logger.logPurchase(BigDecimal.valueOf(4.32), Currency.getInstance("USD"));
    }

    public static Bitmap getFacebookProfilePicture(String userID){
        URL imageURL = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
