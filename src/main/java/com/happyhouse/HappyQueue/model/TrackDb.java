package com.happyhouse.HappyQueue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@JsonNaming(SnakeCaseStrategy.class)
public class TrackDb {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String title;
  private String subtitle;
  private String imageUrl;
  private Timestamp queueTime;
  private String spotifyUri;
  private String queuer;

  @ManyToOne
  @JoinColumn
  @JsonIgnore
  @ToString.Exclude
  private QueueDb queue;

  @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
  @JsonIgnore
  @ToString.Exclude
  private List<VoteDb> votes;

  public int getVoteTotal() {
    return votes.stream().mapToInt(v -> v.getVoteType().getValue()).sum();
  }

  public TrackDb(String title, String subtitle, String imageUrl, String spotifyUri, String queuer, QueueDb queue) {
    this(title, subtitle, imageUrl, spotifyUri, queuer);
    this.queue = queue;
  }

  public TrackDb(String title, String subtitle, String imageUrl, String spotifyUri, String queuer) {
    this.title = title;
    this.subtitle = subtitle;
    this.imageUrl = imageUrl;
    this.queueTime = Timestamp.from(Instant.now());
    this.spotifyUri = spotifyUri;
    this.queuer = queuer;
  }

  public TrackDb() {}
}
