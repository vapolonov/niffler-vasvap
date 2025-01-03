package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserQueueExtension.class)
public class UsersQueueTest {

    @Test
    public void testWithEmptyUser(@UserType(empty = true) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user);

    }

    @Test
    public void testWithEmptyUser1(@UserType(empty = true) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user);

    }

    @Test
    public void testWithEmptyUser2(@UserType(empty = false) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user);

    }
}
