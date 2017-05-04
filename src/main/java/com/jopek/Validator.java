package com.jopek;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Tomek on 2017-05-03.
 */
public class Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String LETTERS_PATTERN = "[a-zA-z]+";
    private static final String POSTALCODE_PATTERN = "\\d{2}-\\d{3}";
    private static final String PHONE_PATTERN = "\\d{9}";
    private static final String YEAR_PATTERN = "\\d{4}";

    public static boolean validateEmail(String email) {
        if (email != null) {
            if (email.equals("")) {
                return false;
            } else if (!Pattern.matches(EMAIL_PATTERN, email)) {
                return false;
            }
        }

        return true;
    }

    public static boolean validateLetters(String onlyLetters) {
        if(onlyLetters != null) {
            if(Pattern.matches(LETTERS_PATTERN, onlyLetters))
                return true;
        }

        return false;
    }

    public static boolean validatePostalCode(String postalCode) {
        if(postalCode != null) {
            if(Pattern.matches(POSTALCODE_PATTERN, postalCode))
                return true;
        }

        return false;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if(phoneNumber != null) {
            if(Pattern.matches(PHONE_PATTERN, phoneNumber))
                return true;
        }

        return false;
    }

    public static boolean validateYear(String year) {
        if(year != null) {
            if(year.length() == 4 && Pattern.matches(YEAR_PATTERN, year)) {
                int tmpYear = Integer.valueOf(year);
                int currYear = Calendar.getInstance().get(Calendar.YEAR);

                if(tmpYear >= 1899 && tmpYear <= currYear) {
                    return true;
                }
            }
        }

        return false;
    }

}
