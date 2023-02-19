package saber.mcmmochatcolor.utils;

public class HexColors {

    // Valid Hex Characters
    private static final String HexChars = "0123456789abcdef";

    // Used to confirm if the given string consists solely of hex colors in format &#RRGGBB
    public static boolean validHexColorCodes(String HexColors){

        // Check if length is multiple of 8, impossible to be valid if not
        if (HexColors.length() % 8 != 0) return false;

        for (int x = 0; x < HexColors.length(); x++) {
            char i = HexColors.charAt(x);

            // Check that the correct char is in the right relative position
            switch (x % 8) {
                case 0:
                    if (i != '&') return false;
                    break;
                case 1:
                    if (i != '#') return false;
                    break;
                default:
                    if (HexChars.indexOf(i) == -1) return false;
                    break;
            }
        }
        return true;
    }
}
