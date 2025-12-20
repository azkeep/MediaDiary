package azkeep.mediadiary.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaSelectedDto {
    private Long id;
    private String title;
    private LocalDate date;
    private Boolean isFinished;
    private String type;
    private String genre;
}
