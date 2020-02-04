package com.happyhouse.HappyQueue.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
