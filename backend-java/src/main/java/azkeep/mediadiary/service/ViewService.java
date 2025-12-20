package azkeep.mediadiary.service;

import azkeep.mediadiary.dto.MediaSelectedDto;

import java.time.LocalDate;
import java.util.List;

public interface ViewService {
    List<MediaSelectedDto> getAllMedia();

    List<MediaSelectedDto> getMediaByDate(LocalDate date);

    List<MediaSelectedDto> getMediaLaterThan(LocalDate date);

    MediaSelectedDto save(MediaSelectedDto media);

    MediaSelectedDto update(MediaSelectedDto dto);

    void delete(Long id);
}
