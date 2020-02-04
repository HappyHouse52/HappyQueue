package com.happyhouse.HappyQueue.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.happyhouse.HappyQueue.util.HappyHouseStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HappyHouseStyle
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public interface TrackIF {
  String getTitle();
  String getSubtitle();
  String getImageUrl();
  String getSpotifyUri();
}
