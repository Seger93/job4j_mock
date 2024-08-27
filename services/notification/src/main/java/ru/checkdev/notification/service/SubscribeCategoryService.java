package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.repository.SubscribeCategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@EnableKafka
public class SubscribeCategoryService {
    private final SubscribeCategoryRepository repository;

    public List<SubscribeCategory> findAll() {
        return repository.findAll();
    }

    @KafkaListener(topics = "add_category", groupId = "group_id")
    public SubscribeCategory save(SubscribeCategory subscribeCategory) {
        return repository.save(subscribeCategory);
    }

    public List<Integer> findCategoriesByUserId(int userId) {
        List<Integer> rsl = new ArrayList<>();
        List<SubscribeCategory> list = repository.findByUserId(userId);
        for (SubscribeCategory subscribeCategory : list) {
            rsl.add(subscribeCategory.getCategoryId());
        }
        return rsl;
    }
    @KafkaListener(topics = "delete_category", groupId = "group_id")
    public SubscribeCategory delete(SubscribeCategory subscribeCategory) {
        SubscribeCategory subscribeCategoryRsl = repository
                .findByUserIdAndCategoryId(subscribeCategory.getUserId(), subscribeCategory.getCategoryId());
        repository.delete(subscribeCategoryRsl);
        return subscribeCategory;
    }
}