package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface InMemoryMealRepository {

    void save(Meal meal);

    void delete(int id);

    Collection<Meal> getAll();

    Meal getMeal(int id);
}
