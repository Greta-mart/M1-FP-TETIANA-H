package ua.com.javarush.gnew.language;

import java.util.List;

/**
 * Extension point for the optional Ukrainian-alphabet task
 * (see src/main/resources/project-description.pdf, "Додаткові завдання").
 *
 * <p>The core implementation does not use this class — {@link ua.com.javarush.gnew.crypto.Cypher}
 * ships with a hard-coded English alphabet. To add Ukrainian support, subclass
 * Language with a Ukrainian alphabet and refactor Cypher to accept a Language
 * instead of using its hard-coded list.
 */
public abstract class Language {

    private final List<Character> alphabet;

    protected Language(List<Character> alphabet) {
        this.alphabet = alphabet;
    }

    public List<Character> getAlphabet() {
        return alphabet;
    }
}
