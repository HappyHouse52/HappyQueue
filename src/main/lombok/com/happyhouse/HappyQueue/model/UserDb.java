package com.happyhouse.HappyQueue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class UserDb {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Transient
  @JsonIgnore
  private String password;

  @JsonIgnore
  private String encryptedPassword;

  public UserDb(String username, String password, String encryptedPassword) {
    this.username = username;
    this.password = password;
    this.encryptedPassword = encryptedPassword;
  }

  public UserDb() {}
}
