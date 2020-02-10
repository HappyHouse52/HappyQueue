package com.happyhouse.HappyQueue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.happyhouse.HappyQueue.model.QueueDb;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@JsonNaming(SnakeCaseStrategy.class)
public class TrackDb {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private String title;
  private String subtitle;
  private String imageUri;
  private Timestamp queueTime;
  private String spotifyUri;
  private String queuer;

  @ManyToOne
  @JoinColumn
  @JsonIgnore
  @ToString.Exclude
  private QueueDb queue;

  public TrackDb(String title, String subtitle, String imageUri, String spotifyUri, String queuer, QueueDb queue) {
    this(title, subtitle, imageUri, spotifyUri, queuer);
    this.queue = queue;
  }

  public TrackDb(String title, String subtitle, String imageUri, String spotifyUri, String queuer) {
    this.title = title;
    this.subtitle = subtitle;
    this.imageUri = imageUri;
    this.queueTime = Timestamp.from(Instant.now());
    this.spotifyUri = spotifyUri;
    this.queuer = queuer;
  }

  public TrackDb() {}
}