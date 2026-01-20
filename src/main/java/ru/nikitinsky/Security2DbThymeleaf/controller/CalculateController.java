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

import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@Controller
public class CalculateController {

    @Autowired
    private BoxOfficeRepository boxOfficeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/calculate")
    public ModelAndView showCalculateForm() {
        log.info("/calculate -> calculation form");
        ModelAndView mav = new ModelAndView("calculate");
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        return mav;
    }

    @GetMapping("/calculate/result")
    public ModelAndView calculateRevenue(@RequestParam(required = false) Long movieId,
                                         @RequestParam(required = false) Integer year) {
        log.info("/calculate/result -> calculating revenue for movie id: {}, year: {}", movieId, year);
        ModelAndView mav = new ModelAndView("calculate-result");
        
        List<BoxOffice> boxOffices;
        if (movieId != null && movieId > 0) {
            Movie movie = movieRepository.findById(movieId).orElse(null);
            if (movie != null) {
                boxOffices = boxOfficeRepository.findAll().stream()
                        .filter(bo -> bo.getMovie().getId().equals(movieId))
                        .toList();
                mav.addObject("selectedMovie", movie);
            } else {
                boxOffices = boxOfficeRepository.findAll();
            }
        } else {
            boxOffices = boxOfficeRepository.findAll();
        }

        if (year != null && year > 0) {
            boxOffices = boxOffices.stream()
                    .filter(bo -> bo.getYear().equals(year))
                    .toList();
            mav.addObject("selectedYear", year);
        }

        double totalRevenue = boxOffices.stream()
                .mapToDouble(BoxOffice::getRevenue)
                .sum();

        mav.addObject("boxOffices", boxOffices);
        mav.addObject("totalRevenue", totalRevenue);
        DecimalFormat df = new DecimalFormat("#,###");
        mav.addObject("totalRevenueFormatted", df.format(Math.round(totalRevenue)).replace(",", " "));
        mav.addObject("count", boxOffices.size());
        
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        
        return mav;
    }
}
