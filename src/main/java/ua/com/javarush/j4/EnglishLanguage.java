package ua.com.javarush.j4;

public class EnglishLanguage implements Language {

    private static final char[] ALPHABET = buildAlphabet();

    private static char[] buildAlphabet() {
        char[] a = new char[52];
        for (int i = 0; i < 26; i++) {
            a[i]      = (char) ('A' + i);
            a[i + 26] = (char) ('a' + i);
        }
        return a;
    }

    @Override
    public char[] alphabet() {
        return ALPHABET;
    }
}