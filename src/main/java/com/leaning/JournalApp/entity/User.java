package com.leaning.JournalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {


    @Id
    private ObjectId userId;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    private String email;
    @NonNull
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();
    @DBRef
    private List<JournalEntry> journalEntryList=new ArrayList<>();
}

