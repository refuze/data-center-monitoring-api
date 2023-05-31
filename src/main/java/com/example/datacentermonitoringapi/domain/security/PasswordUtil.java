package com.example.datacentermonitoringapi.domain.security;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PasswordUtil {
    public static String generateSecureRandomPassword(int specialCharsCount, int numbersCount, int alphabetsCount) {
        Stream<Character> pwdStream = Stream.concat(getRandomNumbers(numbersCount),
                Stream.concat(getRandomSpecialChars(specialCharsCount),
                        Stream.concat(getRandomAlphabets(alphabetsCount / 2, true), getRandomAlphabets(alphabetsCount / 2 + alphabetsCount % 2, false))));

        List<Character> charList = pwdStream.collect(Collectors.toList());

        Collections.shuffle(charList);

        return charList.stream()
                .collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
    }


    private static Stream<Character> getRandomSpecialChars(int count) {
        Random random = new SecureRandom();
        IntStream specialChars = random.ints(count, 33, 48);
        return specialChars.mapToObj(data -> (char) data);
    }

    private static Stream<Character> getRandomNumbers(int count) {
        Random random = new SecureRandom();
        IntStream numbers = random.ints(count, 48, 58);
        return numbers.mapToObj(data -> (char) data);
    }

    private static Stream<Character> getRandomAlphabets(int count, boolean upperCase) {
        Random random = new SecureRandom();
        IntStream alphabets = upperCase ? random.ints(count, 65, 91) : random.ints(count, 97, 123);
        return alphabets.mapToObj(data -> (char) data);
    }
}
