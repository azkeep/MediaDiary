package azkeep.mediadiary.repository;

import azkeep.mediadiary.entity.MediaSelected;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MediaSelectedRepo extends JpaRepository<MediaSelected, Long> {
    List<MediaSelected> findAllByDateGreaterThanEqualOrderByDateDesc(LocalDate date);

    List<MediaSelected> findByDateGreaterThanEqualOrderByDateDescTitleDesc(LocalDate date);

    List<MediaSelected> findAllByOrderByDateDesc();

    List<MediaSelected> findByDate(LocalDate date);


}
