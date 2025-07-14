package com.pwii.stock_management.controller;

import com.pwii.stock_management.dto.ReceitaBebidaDto;
import com.pwii.stock_management.dto.ReceitaBebidaFormDto;
import com.pwii.stock_management.dto.ReceitaBebidaViewDto;
import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.IngredienteService;
import com.pwii.stock_management.service.ReceitaBebidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/receitas-bebidas")
public class ReceitaBebidaController {

    @Autowired
    private ReceitaBebidaService receitaBebidaService;

    @Autowired
    private BebidaService bebidaService;

    @Autowired
    private IngredienteService ingredienteService;

    @GetMapping({"", "/"})
    public String verHomePage(Model model) {
        List<ReceitaBebidaViewDto> receitas = receitaBebidaService.getAllReceitaBebidas();
        Map<Bebida, List<ReceitaBebidaViewDto>> receitasAgrupadas = receitas.stream()
                .collect(Collectors.groupingBy(ReceitaBebidaViewDto::getBebida));
        model.addAttribute("receitasAgrupadas", receitasAgrupadas);
        return "receitas-bebidas/index";
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        ReceitaBebidaFormDto form = new ReceitaBebidaFormDto();

        model.addAttribute("form", form);
        model.addAttribute("bebidas", bebidaService.getAllBebidas());
        model.addAttribute("ingredientes", ingredienteService.getAllIngredientes());
        return "receitas-bebidas/criar";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ReceitaBebidaFormDto form) {
        receitaBebidaService.salvarReceitas(form);
        return "redirect:/receitas-bebidas";
    }

    @GetMapping("/editar/{idBebida}")
    public String editar(@PathVariable Long idBebida, Model model) {
        List<ReceitaBebidaViewDto> receitas = receitaBebidaService.getAllReceitaBebidas().stream()
                .filter(r -> r.getBebida().getId().equals(idBebida))
                .collect(Collectors.toList());

        ReceitaBebidaFormDto form = new ReceitaBebidaFormDto();
        form.setIdBebida(idBebida);

        List<ReceitaBebidaDto> ingredientes = receitas.stream().map(r -> {
            ReceitaBebidaDto dto = new ReceitaBebidaDto();
            dto.setIdBebida(r.getIdBebida());
            dto.setIdIngrediente(r.getIdIngrediente());
            dto.setQuantidade(r.getQuantidade());
            dto.setUnidadeMedida(r.getUnidade_medida());
            return dto;
        }).collect(Collectors.toList());

        form.setIngredientes(ingredientes);

        model.addAttribute("form", form);
        model.addAttribute("bebidas", bebidaService.getAllBebidas());
        model.addAttribute("ingredientes", ingredienteService.getAllIngredientes());
        return "receitas-bebidas/editar";
    }

    @GetMapping("/deletar/{idBebida}")
    public String deletar(@PathVariable Long idBebida) {
        receitaBebidaService.deletarReceitaByIdBebida(idBebida);
        return "redirect:/receitas-bebidas";
    }
}