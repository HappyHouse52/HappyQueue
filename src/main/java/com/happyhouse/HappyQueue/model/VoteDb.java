package com.happyhouse.HappyQueue.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"track_id", "username"})})
public class VoteDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Enumerated(EnumType.STRING)
  private VoteType voteType;
  private String username;

  @ManyToOne
  @JoinColumn
  @ToString.Exclude
  private TrackDb track;

  public VoteDb(VoteType voteType, String username, TrackDb track) {
    this(voteType, username);
    this.track = track;
  }

  public VoteDb(VoteType voteType, String username) {
    this.voteType = voteType;
    this.username = username;
  }

  public VoteDb() {}
}
