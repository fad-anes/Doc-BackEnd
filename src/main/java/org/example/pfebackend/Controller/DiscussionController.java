package org.example.pfebackend.Controller;

import org.example.pfebackend.Entity.Discussion;
import org.example.pfebackend.Service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussions")
@CrossOrigin("*")
public class DiscussionController {
    @Autowired
    private DiscussionService discussionService;

    @PostMapping("/create")
    public ResponseEntity<Discussion> createDiscussion(
            @RequestParam Integer patientId,
            @RequestParam Integer doctorId) {

        Discussion discussion = discussionService.CreateDiscussion(patientId, doctorId);

        if (discussion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(discussion);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDiscussions(@PathVariable Integer id) {
        boolean deleted = discussionService.deleteDiscussion(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Erreur Inconnu", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/{id}/{role}")
    public List<Discussion> getDiscussions(
            @PathVariable Integer id,
            @PathVariable String role) {

        return discussionService.GetAllDiscussions(id, role);
    }
}
