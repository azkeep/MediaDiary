package azkeep.mediadiary.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "media_selected")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaSelected {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selected_id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @ColumnDefault("now()")
    @Column(name = "date_selected", nullable = false)
    private LocalDate date;

    @ColumnDefault("false")
    @Column(name = "is_finished", nullable = false)
    private Boolean isFinished = false;

    @Column(name = "type_selected", length = 20)
    private String type;

    @Column(name = "genre_selected", length = 20)
    private String genre;
}