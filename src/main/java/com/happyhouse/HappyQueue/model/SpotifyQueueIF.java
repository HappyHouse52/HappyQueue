package com.happyhouse.HappyQueue.model;

import com.happyhouse.HappyQueue.util.HappyHouseStyle;
import java.util.List;
import org.immutables.value.Value.Immutable;

@HappyHouseStyle
@Immutable
public interface SpotifyQueueIF {
  String getId();
  List<Track> getTracks();
}