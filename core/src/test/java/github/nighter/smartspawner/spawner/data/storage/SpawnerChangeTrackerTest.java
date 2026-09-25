package github.nighter.smartspawner.spawner.data.storage;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpawnerChangeTrackerTest {
    @Test
    void lateModificationDoesNotCancelDeletion() {
        Set<String> dirty = ConcurrentHashMap.newKeySet();
        Set<String> deleted = ConcurrentHashMap.newKeySet();

        SpawnerChangeTracker.markDeleted(dirty, deleted, "spawner-1");
        SpawnerChangeTracker.markModified(dirty, deleted, "spawner-1");

        assertTrue(deleted.contains("spawner-1"));
        assertFalse(dirty.contains("spawner-1"));
    }

    @Test
    void deletionAlsoWinsWhenModificationStartsFirst() {
        Set<String> dirty = ConcurrentHashMap.newKeySet();
        Set<String> deleted = ConcurrentHashMap.newKeySet();

        SpawnerChangeTracker.markModified(dirty, deleted, "spawner-1");
        SpawnerChangeTracker.markDeleted(dirty, deleted, "spawner-1");

        assertTrue(deleted.contains("spawner-1"));
        assertFalse(dirty.contains("spawner-1"));
    }

    @Test
    void deletionWinsConcurrentModificationRace() throws InterruptedException {
        Set<String> dirty = ConcurrentHashMap.newKeySet();
        Set<String> deleted = ConcurrentHashMap.newKeySet();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            for (int i = 0; i < 1_000; i++) {
                String id = "spawner-" + i;
                CountDownLatch start = new CountDownLatch(1);

                executor.submit(() -> {
                    await(start);
                    SpawnerChangeTracker.markModified(dirty, deleted, id);
                });
                executor.submit(() -> {
                    await(start);
                    SpawnerChangeTracker.markDeleted(dirty, deleted, id);
                });
                start.countDown();
            }
        } finally {
            executor.shutdown();
            assertTrue(executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS));
        }

        assertTrue(deleted.size() == 1_000);
        assertTrue(dirty.isEmpty());
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
