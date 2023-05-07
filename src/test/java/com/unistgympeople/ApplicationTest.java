package com.unistgympeople;

import com.unistgympeople.realTime.controller.UserController;
import com.unistgympeople.realTime.model.User;
import com.unistgympeople.realTime.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationTest {

    private UserRepository userRepository;
    private UserController userController;
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @DisplayName("Test for creating entity")
    @Test
    void CreateEntityTest() {
        User user = new User();
        ResponseEntity<User> actual = userController.saveUser(user);
    }
}
