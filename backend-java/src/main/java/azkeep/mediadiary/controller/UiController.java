package azkeep.mediadiary.controller;

import azkeep.mediadiary.dto.MediaSelectedDto;
import azkeep.mediadiary.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui")
public class UiController {
    private final ViewService viewService;

    @GetMapping("/recent")
    public String showRecent(Model model) {

        LocalDate daysAgo = LocalDate.now().minusDays(14);

        List<MediaSelectedDto> list = viewService.getMediaLaterThan(daysAgo);

        model.addAttribute("recent", list);

        return "view-recent";
    }
}
