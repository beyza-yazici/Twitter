package com.example.twitter.repository;

import com.example.twitter.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenFindByUsername_thenReturnUser() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByUsername(user.getUsername());

        assertTrue(found.isPresent());
        assertEquals(user.getUsername(), found.get().getUsername());
    }

    @Test
    void whenFindByEmail_thenReturnUser() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail(user.getEmail());

        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

    @Test
    void whenExistsByUsername_thenReturnTrue() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        boolean exists = userRepository.existsByUsername(user.getUsername());

        assertTrue(exists);
    }

    @Test
    void whenExistsByEmail_thenReturnTrue() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertTrue(exists);
    }

    @Test
    void whenSaveUser_thenReturnSavedUser() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password123");

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals(user.getUsername(), saved.getUsername());
        assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void whenDeleteUser_thenUserShouldNotExist() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password123");
        entityManager.persist(user);
        entityManager.flush();

        userRepository.deleteById(user.getId());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
}