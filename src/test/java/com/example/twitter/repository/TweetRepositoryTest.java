package com.example.twitter.repository;

import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TweetRepositoryTest {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenFindByUserId_thenReturnTweets() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user = userRepository.save(user);

        Tweet tweet1 = new Tweet();
        tweet1.setContent("First tweet");
        tweet1.setUser(user);
        tweetRepository.save(tweet1);

        Tweet tweet2 = new Tweet();
        tweet2.setContent("Second tweet");
        tweet2.setUser(user);
        tweetRepository.save(tweet2);

        List<Tweet> found = tweetRepository.findByUserId(user.getId());

        assertEquals(2, found.size());
        User finalUser = user;
        assertTrue(found.stream()
                .allMatch(t -> t.getUser().getId().equals(finalUser.getId())));
    }

}
