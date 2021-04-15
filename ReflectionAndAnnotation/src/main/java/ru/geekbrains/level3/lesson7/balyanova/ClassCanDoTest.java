package ru.geekbrains.level3.lesson7.balyanova;

public class ClassCanDoTest {
    @BeforeSuite
    public void beforeSuiteMethod() {
        System.out.println("Before Suite method");
    }

    @Test(priority = 1)
    public void methodOne() {
        System.out.println("Test method 1/priority 1");
    }
    @Test(priority = 2)
    public void methodTwo() {
        System.out.println("Test method 2/priority 2");
    }
    @Test(priority = 3)
    public void methodThree() {
        System.out.println("Test method 3/priority 3");
    }
    @Test(priority = 4)
    public void methodFour() {
        System.out.println("Test method 4/priority 4");
    }
    @Test(priority = 5)
    public void methodFive() {
        System.out.println("Test method 5/priority 5");
    }
    @Test(priority = 6)
    public void methodSix() {
        System.out.println("Test method 6/priority 6");
    }
    @Test(priority = 7)
    public void methodSeven() {
        System.out.println("Test method 7/priority 7");
    }
    @Test(priority = 8)
    public void methodEight() {
        System.out.println("Test method 8/priority 8");
    }
    @Test(priority = 9)
    public void methodNine() {
        System.out.println("Test method 9/priority 9");
    }
    @Test(priority = 10)
    public void methodTen() {
        System.out.println("Test method 10/priority 10");
    }

    @AfterSuite
    public void afterSuiteMethod() {
        System.out.println("After Suite method");
    }

}