package com.docencia.aed.repository.memory;

import com.docencia.aed.entity.Publisher;
import com.docencia.aed.repository.PublisherRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * @author danielrguezh
 * @version 1.0.0
 */
@Repository
public class InMemoryPublisherRepository implements PublisherRepository {

    private final InMemoryDataStore store;

    public InMemoryPublisherRepository(InMemoryDataStore store) {
        this.store = store;
    }

    @Override
    public List<Publisher> findAll() {
        List<Publisher> list = new ArrayList<>(store.publishers.values());
        list.sort(Comparator.comparing(Publisher::getId));
        return list;
    }

    @Override
    public Publisher save(Publisher publisher) {
        if (publisher.getId() == null) {
            publisher.setId(store.publisherSeq.incrementAndGet());
        } else {
            store.publisherSeq.updateAndGet(prev -> Math.max(prev, publisher.getId()));
        }

        store.publishers.put(publisher.getId(), publisher);
        return publisher;
    }
}
