package ru.geekbrains.balyanova.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.geekbrains.balyanova.testing.ArrayMain;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Testing {
    private static ArrayMain arrayMain;//без статики не обойтись здесь...

    @BeforeAll
    static void initAll() {
        arrayMain = new ArrayMain();
    }

    @ParameterizedTest
    @MethodSource("demoMethodReturn")
    public void testMethodReturn(int[] result, int[] arr) {
        Assertions.assertArrayEquals(result, arrayMain.methodReturn(arr));
    }

    public static Stream<Arguments> demoMethodReturn() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[]{5, 6, 7}, new int[]{1, 2, 3, 4, 5, 6, 7}));
        out.add(Arguments.arguments(new int[]{1, 2, 1}, new int[]{4, 1, 2, 1}));
        out.add(Arguments.arguments(new int[]{}, new int[]{1, 2, 3, 4}));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("demoIsArrayEqualsOneAndFour")
    public void testIsArrayEqualsOneAndFour(boolean result, int[] array) {
        Assertions.assertEquals(result, arrayMain.isArrayEqualsOneAndFour(array));
    }

    public static Stream<Arguments> demoIsArrayEqualsOneAndFour() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(false, new int[]{1, 2, 3, 4}));
        out.add(Arguments.arguments(true, new int[]{1, 1, 4, 4}));
        out.add(Arguments.arguments(true, new int[]{1, 4, 1, 4}));
        return out.stream();
    }
}
//    @Test//еще способ, но менее удобный, разбирали на уроке
//    public void testIsArrayEqualsOneAndFour() {
//        Assertions.assertFalse(arrayMain.isArrayEqualsOneAndFour(new int[]{1, 2, 3, 4}));
//        Assertions.assertTrue(arrayMain.isArrayEqualsOneAndFour(new int[]{1, 1, 4, 4}));
//        Assertions.assertTrue(arrayMain.isArrayEqualsOneAndFour(new int[]{1, 4, 1, 4}));
//    }



