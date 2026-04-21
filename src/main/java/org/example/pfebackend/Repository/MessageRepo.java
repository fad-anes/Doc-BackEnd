package org.example.pfebackend.Repository;
import org.example.pfebackend.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer> {
    List<Message> findByDiscussion_IdOrderByCreatedAtAsc(Integer discussionId);
}
