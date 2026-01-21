package ru.nikitinsky.MovieCatalog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class AboutController {

    @GetMapping("/about")
    public ModelAndView showAbout() {
        log.info("/about -> about page");
        return new ModelAndView("about");
    }
}
