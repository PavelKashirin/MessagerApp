package org.example.uilearn.controller;

import lombok.Setter;
import org.example.uilearn.dao.MessageRepository;
import org.example.uilearn.entity.Message;
import org.example.uilearn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Setter(onMethod = @__(@Autowired))
public class MainController {

    private MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting(
            Map<String, Object> model) {
        return "greeting"; // Возвращает файл шаблон из resources.templates - по умолчанию
    }

    @GetMapping("/main")
    public String mainPage(
            @AuthenticationPrincipal User user,
            Map<String, Object> model
    ) {
        model.put("messages", getMessagesByUser(user));

        return "main";
    }

    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String messageText,
            @RequestParam String tag,
            Map<String, Object> model
    ) {
        if ((Objects.nonNull(messageText) && !messageText.isEmpty())
        && (Objects.nonNull(tag) && !tag.isEmpty())) {

            Message message = new Message(messageText, tag, user);
            messageRepository.save(message);

            model.put("messages", getMessagesByUser(user));
            return "main";
        }

        model.put("messages", getMessagesByUser(user));
        return "main";
    }

    @PostMapping("filter")
    public String filter(
            @AuthenticationPrincipal User user,
            @RequestParam (name = "filter") String filter,
            Map<String, Object> model
    ) {
        model.put("messages", getMessagesByUser(user));

        return "main";
    }

    private List<Message> getMessagesByUser(User user) {
        return messageRepository.findByAuthor(user);
    }
}
