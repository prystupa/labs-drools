package com.prystupa;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 9/29/12
 * Time: 3:14 PM
 */
public class Applicant {

    private String name;
    private int age;

    public Applicant(String name, int age) {

        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
