package ua.com.javarush.j4;

public class CaesarCipher {

    private final Language language;
    private final char[] alphabet;
    private final int size;

    public CaesarCipher(Language language) {
        this.language = language;
        this.alphabet = language.alphabet();
        this.size = alphabet.length;
    }

    /** Encrypt by shifting letters possition forward according to the key **/
    public String encrypt(String text, int key) {
        return applyShift(text, normalizeKey(key));
    }

    /** Decrypts by key entered by user **/
    public String decrypt(String text, int key) {
        return applyShift(text, normalizeKey(-key));
    }

    /** Brutforce the original text without knowing the key **/
    public String bruteForce(String text) {
        String bestCandidate = text;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int key = 0; key < size; key++) {
            String candidate = applyShift(text, key);
            double s = score(candidate);
            if (s > bestScore) {
                bestScore     = s;
                bestCandidate = candidate;
            }
        }
        return bestCandidate;
    }

    private String applyShift(String text, int normKey) {
        StringBuilder sb = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            int idx = indexOf(c);
            sb.append(idx < 0 ? c : alphabet[(idx + normKey) % size]);
        }
        return sb.toString();
    }

    private int normalizeKey(int key) {
        return ((key % size) + size) % size;
    }

    private int indexOf(char c) {
        for (int i = 0; i < size; i++) {
            if (alphabet[i] == c)
                return i;
        }
        return -1;
    }

    private double score(String text) {
        String lower = text.toLowerCase();
        double total = 0;


        int lowerCount = 0;
        int upperCount = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                if (Character.isLowerCase(c))
                    lowerCount++;
                else
                    upperCount++;
            }
        }
        int totalLetters = lowerCount + upperCount;
        if (totalLetters > 0) {
            total += (double) lowerCount / totalLetters * 3.0;
        }

        return total;
    }
}