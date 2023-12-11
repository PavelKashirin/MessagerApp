package org.example.uilearn.controller;

import lombok.Setter;
import org.example.uilearn.dao.MessageRepository;
import org.example.uilearn.entity.Message;
import org.example.uilearn.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@Setter(onMethod = @__(@Autowired))
public class MainController {

    private MessageRepository messageRepository;

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

    private List<Message> getMessagesByUser(User user) {
        return messageRepository.findByAuthor(user);
    }

    private List<Message> getMessagesByUserAndTag(User user, String filter) {
        return messageRepository.findByAuthorAndTag(user, filter);
    }
}
