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
	
	@GetMapping("/acomodacoes")
	public ModelAndView acomodacoes() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/acomodacoes");
		return mv;
	}
	
	@GetMapping("/quarto-ayla")
	public ModelAndView quartoAyla() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/ayla");
		return mv;
	}
	
	@GetMapping("/quarto-toby")
	public ModelAndView quartoToby() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/toby");
		return mv;
	}
	
	@GetMapping("/quarto-pipoca")
	public ModelAndView quartoPipoca() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/pipoca");
		return mv;
	}
	
	@GetMapping("/adm")
	public ModelAndView administracao() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/administracao");
		return mv;
	}
	
	@GetMapping("/loginGerente")
	public ModelAndView loginGerente() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/loginGerente");
		return mv;
	}
	
	@GetMapping("/loginFaxineiro")
	public ModelAndView loginFaxineiro() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/loginFaxineiro");
		return mv;
	}
	
	@GetMapping("/homeGerente")
	public ModelAndView homeGerente() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/homeGerente");
		return mv;
	}
	
	@GetMapping("/homeFaxineiro")
	public ModelAndView homeFaxineiros() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/homeFaxineiro");
		return mv;
	}
}
