package ru.nikitinsky.Security2DbThymeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.nikitinsky.Security2DbThymeleaf.entity.BoxOffice;
import ru.nikitinsky.Security2DbThymeleaf.entity.Movie;
import ru.nikitinsky.Security2DbThymeleaf.repository.BoxOfficeRepository;
import ru.nikitinsky.Security2DbThymeleaf.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class BoxOfficeController {

    @Autowired
    private BoxOfficeRepository boxOfficeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/boxoffice")
    public ModelAndView getAllBoxOffice(Authentication authentication) {
        log.info("/boxoffice -> connection by user: {}", authentication != null ? authentication.getName() : "anonymous");
        ModelAndView mav = new ModelAndView("list-boxoffice");
        mav.addObject("boxOffices", boxOfficeRepository.findAll());
        boolean canEdit = authentication != null && 
                          (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
                           authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        mav.addObject("canEdit", canEdit);
        return mav;
    }

    @GetMapping("/boxoffice/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ModelAndView addBoxOfficeForm() {
        log.info("/boxoffice/add -> form");
        ModelAndView mav = new ModelAndView("add-boxoffice-form");
        BoxOffice boxOffice = new BoxOffice();
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("boxOffice", boxOffice);
        mav.addObject("movies", movies);
        return mav;
    }

    @PostMapping("/boxoffice/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String saveBoxOffice(@RequestParam Long movieId,
                                 @RequestParam Double revenue,
                                 @RequestParam String currency,
                                 @RequestParam Integer year,
                                 @RequestParam(required = false) Long id) {
        log.info("/boxoffice/save -> saving box office for movie id: {}", movieId);
        BoxOffice boxOffice;
        if (id != null) {
            boxOffice = boxOfficeRepository.findById(id).orElse(new BoxOffice());
        } else {
            boxOffice = new BoxOffice();
        }
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (movie.isPresent()) {
            boxOffice.setMovie(movie.get());
            boxOffice.setRevenue(revenue);
            boxOffice.setCurrency(currency);
            boxOffice.setYear(year);
            boxOfficeRepository.save(boxOffice);
        }
        return "redirect:/boxoffice";
    }

    @GetMapping("/boxoffice/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ModelAndView showUpdateForm(@RequestParam Long boxOfficeId) {
        log.info("/boxoffice/update -> form for box office id: {}", boxOfficeId);
        ModelAndView mav = new ModelAndView("add-boxoffice-form");
        Optional<BoxOffice> optionalBoxOffice = boxOfficeRepository.findById(boxOfficeId);
        BoxOffice boxOffice = new BoxOffice();
        if (optionalBoxOffice.isPresent()) {
            boxOffice = optionalBoxOffice.get();
        }
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("boxOffice", boxOffice);
        mav.addObject("movies", movies);
        mav.addObject("isUpdate", true);
        return mav;
    }

    @GetMapping("/boxoffice/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String deleteBoxOffice(@RequestParam Long boxOfficeId) {
        log.info("/boxoffice/delete -> deleting box office id: {}", boxOfficeId);
        boxOfficeRepository.deleteById(boxOfficeId);
        return "redirect:/boxoffice";
    }
}
