package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getMealTo;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final InMemoryMealRepository mealRepository = new InMemoryMealRepositoryImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info("save meal {}", meal);
        mealRepository.save(meal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch(action == null ? "all" : action){
            case ("delete"):
                int id = getId(request);
                log.debug("delete meal id {}", id);
                mealRepository.delete(id);
                response.sendRedirect("meals");
                break;
            case ("update"):
                int idForUpdate = getId(request);
                log.debug("update meal id {}", idForUpdate);
                request.setAttribute("meal", mealRepository.getMeal(idForUpdate));
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case ("create"):
                log.debug("create meal");
                Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
            case ("all"):
            default:
                log.debug("get all meal");
                request.setAttribute("mealsTo", getMealTo(mealRepository.getAll(), 2000));
                request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
    }

    public int getId(HttpServletRequest request) {
        String id = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(id);
    }
}
