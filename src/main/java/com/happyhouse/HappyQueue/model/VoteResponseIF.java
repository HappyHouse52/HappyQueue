package com.happyhouse.HappyQueue.model;

import com.happyhouse.HappyQueue.util.HappyHouseStyle;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@HappyHouseStyle
@Immutable
public interface VoteResponseIF {
  @Parameter
  boolean isSuccess();
  @Parameter
  int getVoteTotal();
}
