package ru.nikitinsky.Security2DbThymeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.nikitinsky.Security2DbThymeleaf.entity.Actor;
import ru.nikitinsky.Security2DbThymeleaf.repository.ActorRepository;

import java.util.Optional;

@Slf4j
@Controller
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @GetMapping("/actors")
    public ModelAndView getAllActors(Authentication authentication) {
        log.info("/actors -> connection by user: {}", authentication != null ? authentication.getName() : "anonymous");
        ModelAndView mav = new ModelAndView("list-actors");
        mav.addObject("actors", actorRepository.findAll());
        boolean canEdit = authentication != null && 
                          (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
                           authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        mav.addObject("canEdit", canEdit);
        return mav;
    }

    @GetMapping("/actors/{id}")
    public ModelAndView getActorDetails(@PathVariable Long id, Authentication authentication) {
        log.info("/actors/{} -> actor details", id);
        ModelAndView mav = new ModelAndView("actor-detail");
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if (optionalActor.isEmpty()) {
            mav.setViewName("redirect:/actors");
            return mav;
        }
        Actor actor = optionalActor.get();
        mav.addObject("actor", actor);

        boolean canEdit = authentication != null &&
                (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
                        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        mav.addObject("canEdit", canEdit);

        return mav;
    }

    @GetMapping("/actors/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ModelAndView addActorForm() {
        log.info("/actors/add -> form");
        ModelAndView mav = new ModelAndView("add-actor-form");
        Actor actor = new Actor();
        mav.addObject("actor", actor);
        return mav;
    }

    @PostMapping("/actors/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String saveActor(@ModelAttribute Actor actor) {
        log.info("/actors/save -> saving actor: {} {}", actor.getSurname(), actor.getName());
        actorRepository.save(actor);
        return "redirect:/actors";
    }

    @GetMapping("/actors/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ModelAndView showUpdateForm(@RequestParam Long actorId) {
        log.info("/actors/update -> form for actor id: {}", actorId);
        ModelAndView mav = new ModelAndView("add-actor-form");
        Optional<Actor> optionalActor = actorRepository.findById(actorId);
        Actor actor = new Actor();
        if (optionalActor.isPresent()) {
            actor = optionalActor.get();
        }
        mav.addObject("actor", actor);
        return mav;
    }

    @GetMapping("/actors/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String deleteActor(@RequestParam Long actorId) {
        log.info("/actors/delete -> deleting actor id: {}", actorId);
        actorRepository.deleteById(actorId);
        return "redirect:/actors";
    }
}
