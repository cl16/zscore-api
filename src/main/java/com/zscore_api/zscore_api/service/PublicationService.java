package com.zscore_api.zscore_api.service;

import com.zscore_api.zscore_api.repository.PublicationRepository;
import com.zscore_api.zscore_api.entity.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    public Iterable<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    public Optional<Publication> getPublicationById(Integer id) {
        return publicationRepository.findById(id);
    }
}
