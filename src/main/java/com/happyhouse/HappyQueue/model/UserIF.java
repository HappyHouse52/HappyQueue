package com.happyhouse.HappyQueue.model;

import com.happyhouse.HappyQueue.util.HappyHouseStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HappyHouseStyle
public interface UserIF {
  String getUsername();
  String getPassword();
}
