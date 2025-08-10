package com.ijaa.user.common.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UniqueIdGenerator {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final AtomicLong COUNTER = new AtomicLong(0);

    /**
     * Generates a UUID-like unique identifier
     * Format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     */
    public String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * Generates a short unique ID (8-12 characters)
     * Uses base62 encoding with timestamp and random components
     */
    public String generateShortId() {
        return generateShortId(8);
    }

    /**
     * Generates a short unique ID with specified length
     * @param length desired length (minimum 6, maximum 20)
     */
    public String generateShortId(int length) {
        if (length < 6 || length > 20) {
            throw new IllegalArgumentException("Length must be between 6 and 20");
        }

        long timestamp = System.currentTimeMillis();
        long counter = COUNTER.incrementAndGet();
        long combined = timestamp + counter;

        StringBuilder id = new StringBuilder();

        // Convert to base62
        while (combined > 0 && id.length() < length - 2) {
            id.append(ALPHABET.charAt((int) (combined % BASE)));
            combined /= BASE;
        }

        // Add random characters to reach desired length
        while (id.length() < length) {
            id.append(ALPHABET.charAt(RANDOM.nextInt(BASE)));
        }

        return id.toString();
    }

    /**
     * Generates a user-friendly ID with prefix
     * Format: USER_xxxxxxxx
     */
    public String generateUserIdWithPrefix() {
        return "USER_" + generateShortId(8).toUpperCase();
    }

    /**
     * Generates a numeric ID based on timestamp and counter
     */
    public String generateNumericId() {
        long timestamp = Instant.now().toEpochMilli();
        long counter = COUNTER.incrementAndGet() % 1000; // Keep counter within 3 digits
        return String.valueOf(timestamp) + String.format("%03d", counter);
    }

    /**
     * Generates a snowflake-like ID
     * 64-bit ID with timestamp, machine ID, and sequence
     */
    public String generateSnowflakeId() {
        long timestamp = System.currentTimeMillis() - 1609459200000L; // Epoch: 2021-01-01
        long machineId = 1L; // You can make this configurable
        long sequence = COUNTER.incrementAndGet() % 4096; // 12 bits for sequence

        long id = (timestamp << 22) | (machineId << 12) | sequence;
        return String.valueOf(id);
    }

    /**
     * Generates a readable ID with words and numbers
     * Format: word-word-1234
     */
    public String generateReadableId() {
        String[] words = {
                "alpha", "beta", "gamma", "delta", "epsilon", "zeta", "theta", "kappa",
                "lambda", "sigma", "omega", "phoenix", "dragon", "tiger", "eagle", "falcon",
                "storm", "thunder", "lightning", "spark", "flame", "frost", "crystal", "shadow"
        };

        String word1 = words[RANDOM.nextInt(words.length)];
        String word2 = words[RANDOM.nextInt(words.length)];
        int number = 1000 + RANDOM.nextInt(9000); // 4 digit number

        return word1 + "-" + word2 + "-" + number;
    }

    /**
     * Generates a custom format ID
     * @param prefix prefix for the ID
     * @param length length of the random part
     * @param includeNumbers include numbers in random part
     * @param includeLetters include letters in random part
     */
    public String generateCustomId(String prefix, int length, boolean includeNumbers, boolean includeLetters) {
        StringBuilder alphabet = new StringBuilder();

        if (includeNumbers) {
            alphabet.append("0123456789");
        }
        if (includeLetters) {
            alphabet.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        }

        if (alphabet.length() == 0) {
            throw new IllegalArgumentException("Must include either numbers or letters");
        }

        StringBuilder id = new StringBuilder(prefix);
        if (!prefix.isEmpty()) {
            id.append("_");
        }

        for (int i = 0; i < length; i++) {
            id.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }

        return id.toString();
    }

    /**
     * Generates a secure random token (for sensitive operations)
     */
    public String generateSecureToken(int length) {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++) {
            token.append(ALPHABET.charAt(RANDOM.nextInt(BASE)));
        }
        return token.toString();
    }
}
