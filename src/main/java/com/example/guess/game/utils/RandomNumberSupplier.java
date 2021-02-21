package com.example.guess.game.utils;

import java.util.function.Supplier;

public interface RandomNumberSupplier {
  int nextInt(int lowerBound, int upperBound);
}
