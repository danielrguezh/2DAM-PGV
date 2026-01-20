package com.docencia.aed.service.impl;

import com.docencia.aed.entity.Publisher;
import com.docencia.aed.repository.PublisherRepository;
import com.docencia.aed.service.IPublisherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class PublisherServiceImpl implements IPublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    @Override
    @Transactional
    public Publisher create(Publisher publisher) {
        return publisherRepository.save(publisher);
    }
}
