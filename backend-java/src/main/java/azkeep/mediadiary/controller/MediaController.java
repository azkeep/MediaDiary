package azkeep.mediadiary.controller;

import azkeep.mediadiary.dto.MediaSelectedDto;
import azkeep.mediadiary.service.ViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class MediaController {
    private final ViewService viewService;

    @GetMapping("/entries/{days}")
    public ResponseEntity<List<MediaSelectedDto>> getEntriesLaterThan(@PathVariable Long days) {
        LocalDate daysAgo = LocalDate.now().minusDays(days);
        List<MediaSelectedDto> result = viewService.getMediaLaterThan(daysAgo);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/entries/date/{date}")
    public ResponseEntity<List<MediaSelectedDto>> getEntriesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching entries fora date: {}", date);
        List<MediaSelectedDto> result = viewService.getMediaByDate(date);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/entries/all")
    public ResponseEntity<List<MediaSelectedDto>> getAllEntries() {
        List<MediaSelectedDto> result = viewService.getAllMedia();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/entries")
    public ResponseEntity<MediaSelectedDto> addEntry(@RequestBody MediaSelectedDto dto) {
        log.info("Adding new entry: {}", dto.getTitle());
        MediaSelectedDto saved = viewService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/entries/{entryId}")
    public ResponseEntity<MediaSelectedDto> editEntry(
            @PathVariable Long entryId,
            @RequestBody MediaSelectedDto dto) {
        log.info("Updating entry ID: {}", entryId);
        dto.setId(entryId);
        MediaSelectedDto updated = viewService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long entryId) {
        log.info("Deleting entry ID: {}", entryId);
        viewService.delete(entryId);
        return ResponseEntity.noContent().build();
    }
}