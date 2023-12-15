package org.example.uilearn.controller;

import lombok.RequiredArgsConstructor;
import org.example.uilearn.dao.MessageRepository;
import org.example.uilearn.entity.Message;
import org.example.uilearn.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String mainPage(
            @AuthenticationPrincipal User user,
            @RequestParam (required = false, defaultValue = "") String filter,
            Model model
    ) {

        if (Objects.nonNull(filter) && !filter.isEmpty() ) {
            model.addAttribute("messages", getMessagesByUserAndTag(user, filter));
            model.addAttribute("filter", filter);
        } else {
            model.addAttribute("messages", getMessagesByUser(user));
            model.addAttribute("filter", filter);
        }

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String messageText,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file,
            Map<String, Object> model
    ) throws IOException {
        if ((Objects.nonNull(messageText) && !messageText.isEmpty())
        && (Objects.nonNull(tag) && !tag.isEmpty())) {

            Message message = new Message(messageText, tag, user);

            if (Objects.nonNull(file) && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                File uploadDir = new File(uploadPath);

                checkAndCreateUploadDir(uploadDir);
                String uuidFile = getNewRandomUUID();
                String resultFileName = getNewFileName(uuidFile, file);
                file.transferTo(new File(uploadPath + "/" + resultFileName));

                message.setFilename(resultFileName);
            }

            messageRepository.save(message);

            model.put("messages", getMessagesByUser(user));
            return "main";
        }

        model.put("messages", getMessagesByUser(user));
        return "main";
    }

    private String getNewFileName(String uuidFile, MultipartFile file) {
        return uuidFile + "." + file.getOriginalFilename();
    }

    private String getNewRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private void checkAndCreateUploadDir(File uploadDir) {
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    private List<Message> getMessagesByUser(User user) {
        return messageRepository.findByAuthor(user);
    }

    private List<Message> getMessagesByUserAndTag(User user, String filter) {
        return messageRepository.findByAuthorAndTag(user, filter);
    }
}
