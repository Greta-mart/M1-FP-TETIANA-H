package ua.com.javarush.gnew.runner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsParserTest {

    private final ArgumentsParser parser = new ArgumentsParser();

    @Nested
    @DisplayName("happy paths")
    class HappyPaths {

        @Test
        @DisplayName("parses -e -k -f in given order")
        void canonicalOrder() {
            RunOptions options = parser.parse(new String[]{"-e", "-k", "5", "-f", "/tmp/foo.txt"});
            assertEquals(Command.ENCRYPT, options.getCommand());
            assertEquals(5, options.getKey());
            assertEquals(Paths.get("/tmp/foo.txt"), options.getFilePath());
        }

        @Test
        @DisplayName("parses arguments in arbitrary order")
        void reorderedArgs() {
            RunOptions options = parser.parse(new String[]{"-f", "/tmp/foo.txt", "-k", "5", "-e"});
            assertEquals(Command.ENCRYPT, options.getCommand());
            assertEquals(5, options.getKey());
            assertEquals(Paths.get("/tmp/foo.txt"), options.getFilePath());
        }

        @Test
        @DisplayName("recognizes -d as DECRYPT")
        void decryptCommand() {
            RunOptions options = parser.parse(new String[]{"-d", "-k", "5", "-f", "/tmp/foo.txt"});
            assertEquals(Command.DECRYPT, options.getCommand());
        }

        @Test
        @DisplayName("recognizes -bf as BRUTEFORCE")
        void bruteforceCommand() {
            RunOptions options = parser.parse(new String[]{"-bf", "-k", "5", "-f", "/tmp/foo.txt"});
            assertEquals(Command.BRUTEFORCE, options.getCommand());
        }

        @Test
        @DisplayName("accepts negative key")
        void negativeKey() {
            RunOptions options = parser.parse(new String[]{"-e", "-k", "-5", "-f", "/tmp/foo.txt"});
            assertEquals(-5, options.getKey());
        }
    }

    @Nested
    @DisplayName("error cases")
    class Errors {

        @Test
        @DisplayName("missing command throws")
        void missingCommand() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> parser.parse(new String[]{"-k", "5", "-f", "/tmp/foo.txt"}));
            assertTrue(e.getMessage().toLowerCase().contains("command"),
                    "Expected 'command' in error message, got: " + e.getMessage());
        }

        @Test
        @DisplayName("missing -k throws")
        void missingKey() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> parser.parse(new String[]{"-e", "-f", "/tmp/foo.txt"}));
            assertTrue(e.getMessage().toLowerCase().contains("key"),
                    "Expected 'key' in error message, got: " + e.getMessage());
        }

        @Test
        @DisplayName("missing -f throws")
        void missingFile() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> parser.parse(new String[]{"-e", "-k", "5"}));
            assertTrue(e.getMessage().toLowerCase().contains("file"),
                    "Expected 'file' in error message, got: " + e.getMessage());
        }

        @Test
        @DisplayName("-k with no value throws")
        void keyFlagWithoutValue() {
            assertThrows(IllegalArgumentException.class,
                    () -> parser.parse(new String[]{"-e", "-f", "/tmp/foo.txt", "-k"}));
        }

        @Test
        @DisplayName("-f with no value throws")
        void fileFlagWithoutValue() {
            assertThrows(IllegalArgumentException.class,
                    () -> parser.parse(new String[]{"-e", "-k", "5", "-f"}));
        }

        @Test
        @DisplayName("non-numeric key throws NumberFormatException")
        void nonNumericKey() {
            assertThrows(NumberFormatException.class,
                    () -> parser.parse(new String[]{"-e", "-k", "abc", "-f", "/tmp/foo.txt"}));
        }

        @Test
        @DisplayName("unknown flag throws")
        void unknownFlag() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> parser.parse(new String[]{"-e", "-x", "-k", "5", "-f", "/tmp/foo.txt"}));
            assertTrue(e.getMessage().toLowerCase().contains("unknown"),
                    "Expected 'unknown' in error message, got: " + e.getMessage());
        }
    }
}
