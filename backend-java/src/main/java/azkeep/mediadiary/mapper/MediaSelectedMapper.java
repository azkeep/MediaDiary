package azkeep.mediadiary.mapper;

import azkeep.mediadiary.dto.MediaSelectedDto;
import azkeep.mediadiary.entity.MediaSelected;
import org.springframework.stereotype.Component;

@Component
public class MediaSelectedMapper {

    public MediaSelectedDto toDto(MediaSelected entity) {
        if (entity == null) {
            return null;
        }

        return MediaSelectedDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .date(entity.getDate())
                .isFinished(entity.getIsFinished())
                .type(entity.getType())
                .genre(entity.getGenre())
                .build();
    }

    public MediaSelected toEntity(MediaSelectedDto dto) {
        if (dto == null) {
            return null;
        }

        return MediaSelected.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .date(dto.getDate())
                .isFinished(dto.getIsFinished())
                .type(dto.getType())
                .genre(dto.getGenre())
                .build();
    }
}
