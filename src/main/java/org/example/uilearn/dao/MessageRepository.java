package org.example.uilearn.dao;

import org.example.uilearn.entity.Message;
import org.example.uilearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByTag(String tag);

    List<Message> findByAuthor(User author);
}
