package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.MEALS;

public class InMemoryMealRepositoryImpl implements InMemoryMealRepository {
    private final AtomicInteger id = new AtomicInteger(0);

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    {
        MEALS.forEach(this::save);
    }

    @Override
    public void save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(id.incrementAndGet());
            repository.put(meal.getId(), meal);
        }
        else repository.computeIfPresent(meal.getId(), (a, b) -> b = meal);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream().collect(Collectors.toList());
    }

    @Override
    public Meal getMeal(int id) {
        return repository.get(id);
    }
}
