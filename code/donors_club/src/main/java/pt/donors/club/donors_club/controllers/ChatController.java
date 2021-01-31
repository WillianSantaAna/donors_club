package pt.donors.club.donors_club.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.donors.club.donors_club.models.Chat;
import pt.donors.club.donors_club.models.exceptions.NotFoundException;
import pt.donors.club.donors_club.models.views.ChatSimpleView;
import pt.donors.club.donors_club.repositories.ChatRepository;

@RestController
@RequestMapping(path = "/api/chats")
public class ChatController {
  private Logger logger = LoggerFactory.getLogger(ChatController.class);

  @Autowired
  private ChatRepository chatRepository;

  @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<ChatSimpleView> getChats(@PathVariable int userId) {
    return chatRepository.findChatsByUser(userId);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Chat getChat(@PathVariable int id) {
    Optional<Chat> _chat = chatRepository.findById(id);

    if (_chat.isEmpty())
      throw new NotFoundException("" + id, "Chat", "id");
    else
      return _chat.get();
  }

  @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
  public Chat initChat(@RequestBody Chat chat) {

    return chatRepository.save(chat);
  }
}
