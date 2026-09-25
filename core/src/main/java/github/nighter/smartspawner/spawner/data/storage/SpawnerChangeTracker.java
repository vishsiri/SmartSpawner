package github.nighter.smartspawner.spawner.data.storage;

import java.util.Set;

/**
 * Applies ordering rules to pending persistence changes.
 *
 * <p>Deletion is a tombstone: once an ID is queued for deletion, a late
 * asynchronous update for the same ID must not cancel it.</p>
 */
public final class SpawnerChangeTracker {
    private SpawnerChangeTracker() {
    }

    public static void markModified(Set<String> dirtySpawners, Set<String> deletedSpawners, String spawnerId) {
        if (spawnerId == null) {
            return;
        }

        dirtySpawners.add(spawnerId);
        // add-then-check makes deletion win regardless of which operation
        // entered first when the two calls race on different threads.
        if (deletedSpawners.contains(spawnerId)) {
            dirtySpawners.remove(spawnerId);
        }
    }

    public static void markDeleted(Set<String> dirtySpawners, Set<String> deletedSpawners, String spawnerId) {
        if (spawnerId == null) {
            return;
        }

        deletedSpawners.add(spawnerId);
        dirtySpawners.remove(spawnerId);
    }
}
