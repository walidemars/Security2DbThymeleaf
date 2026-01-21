package ru.nikitinsky.Security2DbThymeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.nikitinsky.Security2DbThymeleaf.entity.BoxOffice;
import ru.nikitinsky.Security2DbThymeleaf.entity.Movie;
import ru.nikitinsky.Security2DbThymeleaf.repository.BoxOfficeRepository;
import ru.nikitinsky.Security2DbThymeleaf.repository.MovieRepository;

import java.util.List;

@Slf4j
@Controller
public class CalculateController {

    @Autowired
    private BoxOfficeRepository boxOfficeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/calculate")
    public ModelAndView showCalculateForm(@RequestParam(required = false) Long movieId) {
        log.info("/calculate -> calculation form, movieId: {}", movieId);
        ModelAndView mav = new ModelAndView("calculate");

        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);

        if (movieId != null && movieId > 0) {
            Movie movie = movieRepository.findById(movieId).orElse(null);
            if (movie != null) {
                mav.addObject("selectedMovie", movie);

                List<BoxOffice> boxOffices = boxOfficeRepository.findByMovie(movie);

                double totalRevenue = 0.0;
                for (BoxOffice boxOffice : boxOffices) {
                    if (boxOffice.getRevenue() != null) {
                        totalRevenue = totalRevenue + boxOffice.getRevenue();
                    }
                }

                double budget = 0.0;
                if (movie.getBudget() != null) {
                    budget = movie.getBudget();
                }

                double profit = totalRevenue - budget;

                double requiredRevenueForBreakEven = 2 * budget;
                double remainingToBreakEven = requiredRevenueForBreakEven - totalRevenue;
                if (remainingToBreakEven < 0) {
                    remainingToBreakEven = 0;
                }

                mav.addObject("totalRevenue", totalRevenue);
                mav.addObject("profit", profit);
                mav.addObject("requiredRevenueForBreakEven", requiredRevenueForBreakEven);
                mav.addObject("remainingToBreakEven", remainingToBreakEven);
            }
        }

        return mav;
    }
}
