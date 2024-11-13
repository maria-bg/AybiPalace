package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class TestController {
	
	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/home");
		return mv;
	}
	
	@GetMapping("/experiencias")
	public ModelAndView experiencias() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/experiencias");
		return mv;
	}
	
	@GetMapping("/reserva")
	public ModelAndView reserva() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/reserva");
		return mv;
	}
	
	@GetMapping("/cadastro")
	public ModelAndView cadastro() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/cadastro");
		return mv;
	}
}
