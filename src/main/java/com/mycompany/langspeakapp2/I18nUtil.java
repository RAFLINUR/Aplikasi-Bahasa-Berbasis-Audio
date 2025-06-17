/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.langspeakapp2;

/**
 *
 * @author ACER
 */


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18nUtil {
    private static final String BUNDLE_BASE_NAME = "messages";
    private static ResourceBundle bundle;

    static {
        setLocale(Locale.getDefault());
    }

    public static void setLocale(Locale locale) {
        try {
            bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
        } catch (MissingResourceException e) {
            System.err.println("❌ Resource for locale " + locale + " not found. Falling back to English.");
            try {
                bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, Locale.ENGLISH);
            } catch (MissingResourceException fallback) {
                System.err.println("❌ English resource bundle not found. Application may not display texts correctly.");
            }
        }
    }

    public static String getString(String key) {
        if (bundle == null) {
            return "??" + key + "??";
        }

        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "??" + key + "??";
        }
    }
}
