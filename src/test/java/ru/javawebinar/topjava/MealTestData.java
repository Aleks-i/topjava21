package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL_ID_FOR_USER = START_SEQ + 2;
    public static final int MEAL_ID_FOR_ADMIN = START_SEQ + 9;
    public static final int NOT_FOUND = 10;

    public static final Meal MEAL_FOR_USER = new Meal(MEAL_ID_FOR_USER, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_FOR_ADMIN_1 = new Meal(MEAL_ID_FOR_ADMIN, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal MEAL_FOR_ADMIN_2 = new Meal(MEAL_ID_FOR_ADMIN + 1, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);
    public static final List<Meal> MEALS_FOR_ADMIN =Arrays.asList(MEAL_FOR_ADMIN_2, MEAL_FOR_ADMIN_1);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.OCTOBER, 20, 12, 0), "Новая еда", 1555);
    }

    public static Meal getNewDuplicateDateTime() {
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Новая еда повторное время", 150);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_FOR_USER);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("").isEqualTo(expected);
    }
}
