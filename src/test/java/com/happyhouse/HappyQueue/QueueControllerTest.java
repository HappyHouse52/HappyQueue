package com.happyhouse.HappyQueue;

import static org.assertj.core.api.Assertions.assertThat;

import com.happyhouse.HappyQueue.controllers.QueueController;
import com.happyhouse.HappyQueue.model.TrackDb;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.Test;

public class QueueControllerTest {

  public static final TrackDb TRACK_DB =
      new TrackDb("Nice For What0", "by Drake", "blah.com", "spotify:track:whatever", "dbannon",
          Instant.now().minus(9, ChronoUnit.MINUTES));
  public static final TrackDb TRACK_DB_2 =
      new TrackDb("Nice For What1", "by Drake", "blah.com", "spotify:track:whatever", "dbannon",
          Instant.now().minus(8, ChronoUnit.MINUTES));
  public static final TrackDb TRACK_DB_3 =
      new TrackDb("Nice For What2", "by Drake", "blah.com", "spotify:track:whatever", "dbannon",
          Instant.now().minus(7, ChronoUnit.MINUTES));
  public static final TrackDb TRACK_DB_4 =
      new TrackDb("Nice For What3", "by Drake", "blah.com", "spotify:track:whatever", "jarryd",
          Instant.now().minus(6, ChronoUnit.MINUTES));
  public static final TrackDb TRACK_DB_5 =
      new TrackDb("Nice For What4", "by Drake", "blah.com", "spotify:track:whatever", "jarryd",
          Instant.now().minus(5, ChronoUnit.MINUTES));
  public static final List<TrackDb> UNSORTED = List.of(
      TRACK_DB,
      TRACK_DB_2,
      TRACK_DB_3,
      TRACK_DB_4,
      TRACK_DB_5);

  @Test
  public void itSortsRoundRobin() {
    final List<TrackDb> roundRobinOrder = QueueController.getRoundRobinOrder(UNSORTED);
    assertThat(roundRobinOrder).extracting(TrackDb::getTitle)
        .containsExactly("Nice For What0", "Nice For What3", "Nice For What1", "Nice For What4", "Nice For What2");
  }

  @Test
  public void itSortsQueueTime() {
    final List<TrackDb> queueTimeOrder = QueueController.getQueueTimeSort(UNSORTED);
    assertThat(queueTimeOrder).extracting(TrackDb::getTitle).containsExactly("Nice For What0", "Nice For What1", "Nice For What2", "Nice For What3", "Nice For What4");
  }
}
