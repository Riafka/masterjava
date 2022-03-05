package ru.javaops.masterjava.xml;

import org.junit.Assert;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.User;

import java.util.List;

public class MainXmlTest {

    public static final String MASTERJAVA = "masterjava";
    public static final String TOPJAVA = "topjava";
    public static final String TOPJAVA_2 = "topjava2";

    @Test
    public void getUsersByProject() throws Exception {
        List<User> usersTopJava = MainXml.getUsersByProject(TOPJAVA);
        System.out.println("topjava users:");
        System.out.println(usersTopJava);
        Assert.assertEquals(3, usersTopJava.size());

        List<User> usersMasterJava = MainXml.getUsersByProject(MASTERJAVA);
        System.out.println("masterjava users:");
        System.out.println(usersMasterJava);
        Assert.assertEquals(3, usersMasterJava.size());

        List<User> usersTopJava2 = MainXml.getUsersByProject(TOPJAVA_2);
        Assert.assertTrue(usersTopJava2.isEmpty());
    }

    @Test
    public void getUsersByProjectStax() throws Exception {
        List<User> usersTopJava = MainXml.getUsersByProjectStax(TOPJAVA);
        System.out.println("topjava users:");
        System.out.println(usersTopJava);
        Assert.assertEquals(3, usersTopJava.size());

        List<User> usersMasterJava = MainXml.getUsersByProjectStax(MASTERJAVA);
        System.out.println("masterjava users:");
        System.out.println(usersMasterJava);
        Assert.assertEquals(3, usersTopJava.size());

        List<User> usersTopJava2 = MainXml.getUsersByProjectStax(TOPJAVA_2);
        Assert.assertTrue(usersTopJava2.isEmpty());
    }

    @Test
    public void getUsersByProjectCompare() throws Exception {
        List<User> usersTopJavaJAXB = MainXml.getUsersByProject(TOPJAVA);
        List<User> usersTopJavaStax = MainXml.getUsersByProjectStax(TOPJAVA);
        Assert.assertEquals(usersTopJavaJAXB, usersTopJavaStax);

        List<User> usersMasterJavaJAXB = MainXml.getUsersByProject(MASTERJAVA);
        List<User> usersMasterJavaStax = MainXml.getUsersByProjectStax(MASTERJAVA);
        Assert.assertEquals(usersMasterJavaJAXB, usersMasterJavaStax);

        List<User> usersTopJava2JAXB = MainXml.getUsersByProject(TOPJAVA_2);
        List<User> usersTopJava2Stax = MainXml.getUsersByProjectStax(TOPJAVA_2);
        Assert.assertEquals(usersTopJava2JAXB, usersTopJava2Stax);
    }

    @Test
    public void getUsersByProjectHtml() throws Exception {
        System.out.println(MainXml.getUsersByProjectHtml(TOPJAVA));
        System.out.println(MainXml.getUsersByProjectHtml(MASTERJAVA));
    }

    @Test
    public void getGroupsByProjectHtml() throws Exception {
        System.out.println(MainXml.getGroupsByProjectHtml(TOPJAVA));
    }
}