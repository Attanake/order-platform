package arch.attanake.service;

import arch.attanake.dto.NotificationTemplateDto;
import arch.attanake.entity.NotificationTemplateEntity;
import arch.attanake.entity.NotificationType;
import arch.attanake.exception.NotFoundException;
import arch.attanake.mapper.NotificationTemplateMapper;
import arch.attanake.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final NotificationTemplateRepository templateRepository;
    private final NotificationTemplateMapper templateMapper;

    public NotificationTemplateDto createTemplate(NotificationTemplateDto templateDto) {
        NotificationTemplateEntity template = templateMapper.toEntity(templateDto);
        template.setVersion(1); // Устанавливаем начальную версию
        NotificationTemplateEntity savedTemplate = templateRepository.save(template);
        return templateMapper.toDto(savedTemplate);
    }

    public NotificationTemplateDto getTemplateById(String id) {
        return templateRepository.findById(id)
                .map(templateMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Template not found with id: " + id));
    }

    public Optional<NotificationTemplateDto> getTemplateByTypeAndLanguage(NotificationType type, String languageCode) {
        return templateRepository.findByTypeAndLanguageCode(type, languageCode)
                .map(templateMapper::toDto);
    }

    public List<NotificationTemplateDto> getTemplatesByType(NotificationType type) {
        return templateRepository.findByType(type).stream()
                .map(templateMapper::toDto)
                .toList();
    }

    public NotificationTemplateDto updateTemplate(String id, NotificationTemplateDto templateDto) {
        NotificationTemplateEntity existing = templateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Template not found with id: " + id));

        NotificationTemplateEntity updated = templateMapper.incrementVersion(existing);
        updated.setSubject(templateDto.getSubject());
        updated.setContent(templateDto.getContent());
        updated.setLanguageCode(templateDto.getLanguageCode());

        NotificationTemplateEntity saved = templateRepository.save(updated);
        return templateMapper.toDto(saved);
    }

    public void deleteTemplate(String id) {
        templateRepository.deleteById(id);
    }
}
