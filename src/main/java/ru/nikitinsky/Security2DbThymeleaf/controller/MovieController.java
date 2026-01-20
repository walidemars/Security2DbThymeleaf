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
import ru.nikitinsky.Security2DbThymeleaf.entity.Actor;
import ru.nikitinsky.Security2DbThymeleaf.entity.Movie;
import ru.nikitinsky.Security2DbThymeleaf.repository.ActorRepository;
import ru.nikitinsky.Security2DbThymeleaf.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @GetMapping("/movies")
    public ModelAndView getAllMovies(Authentication authentication) {
        log.info("/movies -> connection by user: {}", authentication != null ? authentication.getName() : "anonymous");
        ModelAndView mav = new ModelAndView("list-movies");
        mav.addObject("movies", movieRepository.findAll());
        boolean canEdit = authentication != null && 
                          (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
                           authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        mav.addObject("canEdit", canEdit);
        return mav;
    }

    @GetMapping("/movies/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ModelAndView addMovieForm() {
        log.info("/movies/add -> form");
        ModelAndView mav = new ModelAndView("add-movie-form");
        Movie movie = new Movie();
        mav.addObject("movie", movie);
        mav.addObject("actors", actorRepository.findAll());
        return mav;
    }

    @PostMapping("/movies/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String saveMovie(@ModelAttribute Movie movie,
                            @RequestParam(required = false) List<Long> actorIds) {
        log.info("/movies/save -> saving movie: {}", movie.getTitle());
        if (actorIds != null && !actorIds.isEmpty()) {
            List<Actor> selectedActors = actorRepository.findAllById(actorIds);
            movie.setActors(selectedActors);
        } else {
            movie.setActors(new ArrayList<>());
        }
        movieRepository.save(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movies/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ModelAndView showUpdateForm(@RequestParam Long movieId) {
        log.info("/movies/update -> form for movie id: {}", movieId);
        ModelAndView mav = new ModelAndView("add-movie-form");
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        Movie movie = new Movie();
        if (optionalMovie.isPresent()) {
            movie = optionalMovie.get();
        }
        mav.addObject("movie", movie);
        mav.addObject("actors", actorRepository.findAll());
        return mav;
    }

    @GetMapping("/movies/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String deleteMovie(@RequestParam Long movieId) {
        log.info("/movies/delete -> deleting movie id: {}", movieId);
        movieRepository.deleteById(movieId);
        return "redirect:/movies";
    }
}
