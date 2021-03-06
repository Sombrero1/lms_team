package com.mts.lts.mapper;

import com.mts.lts.domain.Course;
import com.mts.lts.domain.Module;
import com.mts.lts.dto.ModuleDto;
import com.mts.lts.dto.ModuleTreeDto;
import com.mts.lts.service.CourseListerService;
import com.mts.lts.service.ModuleListerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModuleMapper extends AbstractMapper<ModuleDto, Module, ModuleListerService> {

    private CourseListerService courseListerService;

    @Autowired
    public ModuleMapper(ModuleListerService moduleListerService, CourseListerService courseListerService) {
        super(moduleListerService);
        this.courseListerService = courseListerService;
    }

    @Override
    public Module dtoToDomain(ModuleDto entityDto) {
        Module module;
        Long courseDtoId = entityDto.getId();
        if (courseDtoId != null) {
            module = entityService.getOne(courseDtoId);
        } else {
            module = new Module();
            Course course = courseListerService.getOne(entityDto.getCourseId());
            module.setCourse(course);
        }
        module.setAuthor(entityDto.getAuthor());
        module.setTitle(entityDto.getTitle());

        return module;
    }

    @Override
    public ModuleDto domainToDto(Module entity) {
        return new ModuleDto(
                entity.getId(),
                entity.getAuthor(),
                entity.getTitle(),
                entity.getCoverImage() != null
        );
    }


    private ModuleTreeDto domainToTreeDto(Module entity) {
        return new ModuleTreeDto(
                entity.getId(),
                entity.getTitle(),
                entity.getTopics().stream().map(x -> new ModuleTreeDto.TopicItemDto(x.getId(),x.getTitle()))
                        .collect(Collectors.toList())
        );
    }

    public List<ModuleTreeDto> listDomainToModuleTreeDtoList(List<Module> dtos) {
        return dtos.stream()
                .map(this::domainToTreeDto)
                .collect(Collectors.toList());
    }
}
