package com.presidents.controller;

import com.presidents.model.dto.PresidentDto;
import com.presidents.service.president.PresidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PresidentsControllerThymeleaf {

    private final PresidentService presidentService;

    @GetMapping("/")
    public String getIndex(
                           @RequestParam(name = "form", required = false, defaultValue = "false") Boolean form,
                           @RequestParam(name = "pageNumber", required = false, defaultValue =  "1") Integer pageNumber,
                            Model model) {

        Page<PresidentDto> presidentsPage = presidentService.getAllPresidentsPaginated(pageNumber - 1, 5);

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", presidentsPage.getTotalPages());
        model.addAttribute("totalPresidents", presidentsPage.getTotalElements());
        model.addAttribute("presidents", presidentsPage.getContent());
        model.addAttribute("presidentDto", new PresidentDto());
        model.addAttribute("form", form);
        return "index";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("presidentDto") PresidentDto presidentDto, Model model) {
        presidentService.savePresident(presidentDto);
        return "redirect:/";
    }



}
