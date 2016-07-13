package Utilities;

import android.content.Context;

/**
 * Created by Okuhle on 5/16/2016.
 */
public class TextSanitizer {

   public TextSanitizer()
   {

   }

    public static boolean isValidText(String text, int minLength, int maxLength) {
        if (text.length() < maxLength && text.length() >= minLength) {
            return true;
        } else {
            return false;
        }
    }

    public static String sanitizeText(String text, boolean toUpper) {
        if (toUpper) {
            text = text.toUpperCase().trim();
            return text;
        } else {
            text = text.trim();
            return text;
        }
    }

    public static boolean isValidEmail(String emailAddress, int minLength, int maxLength) {
        emailAddress = emailAddress.toUpperCase().trim();
        if (emailAddress.contains("@")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSameText(String text1, String text2) {
        if(text1.toUpperCase().trim().equals(text2.toUpperCase().trim())) {
            return true;
        } else {
            return false;
        }
    }
}
