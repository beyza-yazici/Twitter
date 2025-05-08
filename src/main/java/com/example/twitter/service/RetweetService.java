package com.example.twitter.service;

import com.example.twitter.dto.RetweetRequestDTO;
import com.example.twitter.dto.RetweetResponseDTO;

public interface RetweetService {

    RetweetResponseDTO createRetweet(RetweetRequestDTO retweetRequestDTO);
    void deleteRetweet(Long id);
}
