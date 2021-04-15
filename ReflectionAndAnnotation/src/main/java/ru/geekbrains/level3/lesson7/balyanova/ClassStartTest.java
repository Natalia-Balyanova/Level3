package ru.geekbrains.level3.lesson7.balyanova;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;

public class ClassStartTest {
    private static Object object;
    public static void start(Class testClass) {
        if (!beforeAfterAnno(testClass)) {
            throw new RuntimeException("BeforeSuite и AfterSuite присутствуют не в единственном экземпляре ");
        }
        try {
            object = testClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static boolean beforeAfterAnno(Class testClass) {//проверка на количество аннотаций before after
        int beforeAnno = 0;
        int afterAnno = 0;
        for (Method m : testClass.getDeclaredMethods()) {
            if(m.getAnnotation(BeforeSuite.class) != null) {
                beforeAnno++;
            }
            if(m.getAnnotation(AfterSuite.class) != null) {
                afterAnno++;
            }
        } return (beforeAnno == 1) && (afterAnno == 1);
    }

    public static void sort(Class testClass) {
        ArrayList<Method> tests = new ArrayList<>();
        Method[] methods = testClass.getDeclaredMethods();
        Method before = null;
        Method after = null;

        for(Method m : methods) {
            if(m.getAnnotation(BeforeSuite.class) != null) {
                before = m;
            }
            if(m.getAnnotation(AfterSuite.class) !=null) {
                after = m;
            }
            else if(m.getAnnotation(Test.class) != null) {
                tests.add(m);
            }
        }
        tests.sort(Comparator.comparingInt(o -> o.getAnnotation(Test.class).priority()));//сортировка по приоритетам
        if (before != null) {
            tests.add(0, before);//первый метод в начало
        }
        if(after != null) {
            tests.add(after);//последний метод
        }
        for(Method testMethod : tests) {
            if(Modifier.isPrivate(testMethod.getModifiers())) {
                testMethod.setAccessible(true);
            }
            try {
                testMethod.invoke(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}