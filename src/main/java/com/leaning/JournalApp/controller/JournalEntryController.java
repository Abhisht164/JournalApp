package com.leaning.JournalApp.controller;

import com.leaning.JournalApp.entity.JournalEntry;
import com.leaning.JournalApp.entity.User;
import com.leaning.JournalApp.repository.UserRepository;
import com.leaning.JournalApp.service.JournalService;
import com.leaning.JournalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    //  handler for getting all journal entry
    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        List<JournalEntry> allJournals = user.getJournalEntryList();
        if (allJournals != null && !allJournals.isEmpty()) {
            return new ResponseEntity<>(allJournals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    handler for getting journal entry by Id
    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalById(@PathVariable Object myId) {
        Optional<JournalEntry> journal = journalService.getJournalById(myId);
        if (journal.isPresent()) {
            return new ResponseEntity<>(journal.get(), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    handler for creating journal entry
    @PostMapping("{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        try {
            journalService.saveEntry(myEntry, userName);
            return new ResponseEntity<>("Journal Named " + myEntry.getTitle().toUpperCase() + " Created.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //    handler for updating journal by Id
    @PutMapping("/id/{userName}/{myId}")
    public ResponseEntity<?> updateEntry(
            @PathVariable Object myId,
            @RequestBody JournalEntry newEntry,
            @PathVariable String userName
    ) {
        JournalEntry oldEntry = journalService.getJournalById(myId).orElse(null);
        if (oldEntry != null) {
            oldEntry.setTitle(newEntry.getTitle() == null || newEntry.getTitle().isEmpty() ? oldEntry.getTitle() : newEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() == null || newEntry.getContent().isEmpty() ? oldEntry.getContent() : newEntry.getContent());
        }
        journalService.saveEntry(oldEntry,userName);
        JournalEntry updated = journalService.updateEntry(oldEntry);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    //    handler for deleting journal by Id
    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalByID(@PathVariable Object myId, @PathVariable String userName) {
        journalService.deleteJournal(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
