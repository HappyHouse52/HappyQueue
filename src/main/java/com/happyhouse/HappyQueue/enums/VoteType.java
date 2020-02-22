package com.happyhouse.HappyQueue.enums;

public enum VoteType {
  UP(1),
  NONE(0),
  DOWN(-1);

  private int value;

  VoteType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
