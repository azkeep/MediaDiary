package azkeep.mediadiary.service;

import azkeep.mediadiary.dto.MediaSelectedDto;
import azkeep.mediadiary.entity.MediaSelected;
import azkeep.mediadiary.mapper.MediaSelectedMapper;
import azkeep.mediadiary.repository.MediaSelectedRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewServiceImpl implements ViewService {
    private final MediaSelectedRepo mediaSelectedRepo;
    private final MediaSelectedMapper mapper;

    @Override
    public List<MediaSelectedDto> getMediaLaterThan(LocalDate date) {
        List<MediaSelected> recentMediaEntities = mediaSelectedRepo.findByDateGreaterThanEqualOrderByDateDescTitleDesc(date);

        return recentMediaEntities.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<MediaSelectedDto> getAllMedia() {
        List<MediaSelected> result = mediaSelectedRepo.findAllByOrderByDateDesc();

        return result.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<MediaSelectedDto> getMediaByDate(LocalDate date) {
        List<MediaSelected> result = mediaSelectedRepo.findByDate(date);

        return result.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public MediaSelectedDto save(MediaSelectedDto media) {
        MediaSelected entity = mapper.toEntity(media);
        MediaSelected saved = mediaSelectedRepo.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public MediaSelectedDto update(MediaSelectedDto dto) {
        if (!mediaSelectedRepo.existsById(dto.getId())) {
            throw new RuntimeException("Media entry does not exist with id: " + dto.getId());
        }
        MediaSelected entity = mapper.toEntity(dto);
        MediaSelected updated = mediaSelectedRepo.save(entity);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        mediaSelectedRepo.deleteById(id);
    }


}
