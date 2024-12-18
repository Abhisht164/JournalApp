package com.leaning.JournalApp.service;

import com.leaning.JournalApp.entity.JournalEntry;
import com.leaning.JournalApp.entity.User;
import com.leaning.JournalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    JournalRepository journalRepository;

    @Autowired
    UserService userService;


    public Optional<JournalEntry> getJournalById(ObjectId myId){
        return journalRepository.findById(myId);
    }

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUserName(userName);
        JournalEntry saved = journalRepository.save(journalEntry);
        user.getJournalEntryList().add(saved);
        userService.updateUser(user);
    }


    public JournalEntry updateEntry(JournalEntry journalEntry) {
        return journalRepository.save(journalEntry);
    }

    @Transactional
    public void deleteJournal(Object id, String userName) {
        System.out.println(id.toString());
        User user = userService.findByUserName(userName);
        user.getJournalEntryList().removeIf(x->x.getId().equals(id));
        userService.updateUser(user);
        System.out.println(user.getJournalEntryList());
        journalRepository.deleteById(id);
    }
}
