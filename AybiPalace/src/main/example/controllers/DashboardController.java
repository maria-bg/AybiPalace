package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.DAOs.DashboardDAO;

import javax.sql.DataSource;
import java.util.Map;

@Controller
public class DashboardController {
    private final DashboardDAO dashboardDAO;

    public DashboardController(DataSource dataSource) {
        this.dashboardDAO = new DashboardDAO(dataSource);
    }

    @GetMapping("/informacoes")
    public String exibirInformacoes(Model model) {
        double mediaTempoHospedes = dashboardDAO.calcularMediaTempoHospedes();
        Map<String, Object> quartoMaisReservado = dashboardDAO.buscarQuartoMaisReservado();
        Map<String, Object> quartoMenosReservado = dashboardDAO.buscarQuartoMenosReservado();
        Map<String, Object> servicoMaisUtilizado = dashboardDAO.buscarServicoMaisUtilizado();

        model.addAttribute("mediaTempoHospedes", mediaTempoHospedes);
        model.addAttribute("quartoMaisReservado", quartoMaisReservado);
        model.addAttribute("quartoMenosReservado", quartoMenosReservado);
        model.addAttribute("servicoMaisUtilizado", servicoMaisUtilizado);

        return "dashboard";
    }
    
    @GetMapping("/reservas-por-tipo")
    public String reservasPorTipo(Model model) {
        Map<String, Integer> reservasPorTipo = dashboardDAO.contarReservasPorTipoDeQuarto();
        model.addAttribute("reservasPorTipo", reservasPorTipo);
        return "reservasPorTipo";
    }
    
    @GetMapping("/dados-reservas-por-tipo")
    @ResponseBody
    public Map<String, Integer> dadosReservasPorTipo() {
        return dashboardDAO.contarReservasPorTipoDeQuarto();
    }

    @GetMapping("/dados-reservas-por-mes")
    @ResponseBody
    public Map<String, Integer> dadosReservasPorMes() {
        return dashboardDAO.contarReservasPorMes();
    }





}
