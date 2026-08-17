package com.library.iam.application.service.module;

import java.util.List;

import com.library.iam.application.dto.ModuleDTO;
import com.library.iam.domain.model.Module;
import com.library.iam.domain.port.out.ModulePort;

public class ListModulesUseCase {

    private final ModulePort modulePort;

    public ListModulesUseCase(ModulePort modulePort) {
        this.modulePort = modulePort;
    }

    public List<ModuleDTO> execute() {
        return modulePort.findAll().stream()
                .map(ModuleDTO::of)
                .toList();
    }
}
