package team18.pharmacyapp.helpers;

public class FilteringHelpers {
    private static final String alphaRegex = "[a-zA-Z]+";

    public static boolean isAlpha(String string){
        if (string == null) return true;
        return string.matches(alphaRegex);
    }

}
