package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.javaops.masterjava.persist.UserTestData.*;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);
        Assert.assertEquals(FIST5_USERS, users);
    }

    @Test
    public void setUserSeq() {
        int count = dao.getUserSeq();
        count += 5;
        dao.setUserSeq(count);
        Assert.assertEquals(count, dao.getUserSeq());
    }

    @Test
    public void insertBatch() {
        dao.clean();
        dao.insertBatch(NEW_USERS, 100);
        List<User> users = dao.getWithLimit(3);
        Assert.assertEquals(NEW_USERS, users);
    }

    @Test
    public void insertBatchWithConflict() {
        dao.insertBatch(MIXED_USERS, 100);
        List<User> actualUsers = dao.getWithLimit(7);
        List<User> expectedUsers = new ArrayList<>(FIST5_USERS);
        expectedUsers.add(USER3);
        expectedUsers.add(USER6);
        for (int i = 0; i < 7; i++) {
            expectedUsers.get(i).setId(actualUsers.get(i).getId());
        }
        Assert.assertEquals(expectedUsers, actualUsers);
    }
}