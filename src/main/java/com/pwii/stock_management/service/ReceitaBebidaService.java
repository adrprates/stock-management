package com.pwii.stock_management.service;

import com.pwii.stock_management.dto.ReceitaBebidaDto;
import com.pwii.stock_management.dto.ReceitaBebidaFormDto;
import com.pwii.stock_management.dto.ReceitaBebidaViewDto;
import com.pwii.stock_management.model.ReceitaBebida;
import com.pwii.stock_management.model.ReceitaBebidaId;

import java.util.List;

public interface ReceitaBebidaService {
    List<ReceitaBebidaViewDto> getAllReceitaBebidas();
    ReceitaBebidaDto getReceitaBebidaById(ReceitaBebidaId id);

    void salvarReceitas(ReceitaBebidaFormDto form);
    void deletarReceitaByIdBebida(Long idBebida);
}