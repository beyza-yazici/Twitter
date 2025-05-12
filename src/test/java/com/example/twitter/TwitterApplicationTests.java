package com.example.twitter;

import com.example.twitter.repository.CommentRepository;
import com.example.twitter.repository.LikeRepository;
import com.example.twitter.service.CommentServiceImpl;
import com.example.twitter.service.LikeServiceImpl;
import com.example.twitter.service.TweetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TwitterApplicationTests {

	@Test
	void contextLoads() {
	}

}