package com.example.guess.game.storage;

import com.example.guess.dto.BetDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@RequiredArgsConstructor
public class InMemoryGuessGameStorage implements GuessGameStorage {

  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = lock.readLock();
  private final Lock writeLock = lock.writeLock();

  private volatile ConcurrentHashMap<String, BetDto> storage = new ConcurrentHashMap<>();

  @Override
  public void placeBet(@NonNull BetDto dto) {
    try {
      readLock.lock();
      storage.put(dto.getUserId(), dto);
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public Map<String, BetDto> nextRound() {
    try {
      writeLock.lock();
      val result = storage;
      storage = new ConcurrentHashMap<>();
      return result;
    } finally {
      writeLock.unlock();
    }
  }

}
