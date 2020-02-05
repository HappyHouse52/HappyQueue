package com.happyhouse.HappyQueue.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = "tracks")

@Entity
public class QueueDb {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(unique = true)
  private String name;

  @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL)
  private List<TrackDb> tracks;

  public QueueDb(String name, TrackDb... tracks) {
    this.name = name;
    this.tracks = Stream.of(tracks).collect(Collectors.toList());
    this.tracks.forEach(t -> t.setQueue(this));
  }

  public QueueDb() {}
}
