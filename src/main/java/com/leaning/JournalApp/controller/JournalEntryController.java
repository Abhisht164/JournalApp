package com.leaning.JournalApp.controller;

import com.leaning.JournalApp.entity.JournalEntry;
import com.leaning.JournalApp.entity.User;
import com.leaning.JournalApp.service.JournalService;
import com.leaning.JournalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    //  handler for getting all journalEntry
    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> allJournals = user.getJournalEntryList();
        if (allJournals != null && !allJournals.isEmpty()) {
            return new ResponseEntity<>(allJournals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    handler for getting journalEntryById
    @GetMapping("id/{journalId}")
    public ResponseEntity<?> getJournalById(@PathVariable ObjectId journalId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntryList()
                .stream()
                .filter(id -> id.getId().equals(journalId))
                .toList();
        if(!journalEntryList.isEmpty()){
            Optional<JournalEntry> journal = journalService.getJournalById(journalId);
            if (journal.isPresent()) {
                return new ResponseEntity<>(journal.get(), HttpStatus.OK);
            }
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    handler for creating journalEntry
    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            journalService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry.getTitle().toUpperCase() + " Created.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //    handler for updating journalById
    @PutMapping("/id/{journalId}")
    public ResponseEntity<?> updateEntry(@RequestBody JournalEntry newEntry, @PathVariable ObjectId journalId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntryList()
                .stream()
                .filter(id -> id.getId().equals(journalId))
                .toList();
        if(!journalEntryList.isEmpty()){
            JournalEntry oldEntry = journalService.getJournalById(journalId).orElse(null);
            if (oldEntry != null) {
                oldEntry.setTitle(newEntry.getTitle() == null || newEntry.getTitle().isEmpty() ? oldEntry.getTitle() : newEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() == null || newEntry.getContent().isEmpty() ? oldEntry.getContent() : newEntry.getContent());
            }
            journalService.saveEntry(oldEntry,userName);
            JournalEntry updated = journalService.updateEntry(oldEntry);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

    //    handler for deleting journalById
    @DeleteMapping("id/{journalId}")
    public ResponseEntity<?> deleteJournalByID(@PathVariable ObjectId journalId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntryList()
                .stream()
                .filter(id -> id.getId().equals(journalId))
                .toList();
        if(!journalEntryList.isEmpty()) {
            journalService.deleteJournal(journalId, userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }
}
