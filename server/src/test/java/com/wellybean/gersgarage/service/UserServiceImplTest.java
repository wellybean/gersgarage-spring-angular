package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.repository.UserRepository;
import static com.wellybean.gersgarage.util.Constants.VALID_ID;
import com.wellybean.gersgarage.util.EntityFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    public void test_findById_succeeds() {
        userService.findById(VALID_ID);
        verify(userRepository).findById(VALID_ID);
    }

    @Test
    public void test_findAll_succeeds() {
        userService.findAll();
        verify(userRepository).findAll();
    }

    @Test
    public void test_deleteUser_succeeds() {
        User user = EntityFactory.getUser(EntityFactory.listWithAllRoles());
        userService.deleteUser(user);
        verify(userRepository).delete(user);
    }
}