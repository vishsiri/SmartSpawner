# Spawner Data Access

Methods for reading and modifying spawner data via the API.

## Method Reference

| Method | Description | Returns |
|--------|-------------|---------|
| `getSpawnerByLocation(Location)` | Get spawner at a block location | `SpawnerDataDTO` |
| `getSpawnerById(String)` | Get spawner by unique ID | `SpawnerDataDTO` |
| `getAllSpawners()` | Get all registered spawners | `List<SpawnerDataDTO>` |
| `getSpawnerModifier(String)` | Get modifier for changing spawner properties | `SpawnerDataModifier` |
| `getSellPrice(Material)` | Get the active sell price for a material | `double` |
| `hasSellPrice(Material)` | Check whether a material has a sell price | `boolean` |
| `setSellPrice(Material, double)` | Set a custom sell price | `void` |
| `removeSellPrice(Material)` | Remove a custom sell price | `void` |
| `getCustomSellPrices()` | Get a snapshot of custom sell prices | `Map<String, Double>` |
| `removeSpawner(String)` | Remove spawner by ID | `CompletableFuture<Boolean>` |
| `removeSpawner(Location)` | Remove spawner by location | `CompletableFuture<Boolean>` |

## SpawnerDataDTO (Read-Only)

`SpawnerDataDTO` provides read-only access to spawner information.

| Method | Returns |
|--------|---------|
| `getSpawnerId()` | `String` |
| `getLocation()` | `Location` |
| `getEntityType()` | `EntityType` |
| `getSpawnedItemMaterial()` | `Material` (item spawners) |
| `getStackSize()` | `int` |
| `getMaxStackSize()` | `int` |
| `getBaseMaxStoragePages()` | `int` |
| `getBaseMinMobs()` | `int` |
| `getBaseMaxMobs()` | `int` |
| `getBaseMaxStoredExp()` | `int` |
| `getBaseSpawnerDelay()` | `long` (ticks) |
| `isItemSpawner()` | `boolean` |

## SpawnerDataModifier

`SpawnerDataModifier` is the **only way to modify spawner properties**. Use method chaining and call `applyChanges()` to save.

Note: `stackSize` is read-only and cannot be modified.

| Method | Returns |
|--------|---------|
| `getStackSize()` | `int` (read-only) |
| `getMaxStackSize()` / `setMaxStackSize(int)` | `int` / `SpawnerDataModifier` |
| `getBaseMaxStoragePages()` / `setBaseMaxStoragePages(int)` | `int` / `SpawnerDataModifier` |
| `getBaseMinMobs()` / `setBaseMinMobs(int)` | `int` / `SpawnerDataModifier` |
| `getBaseMaxMobs()` / `setBaseMaxMobs(int)` | `int` / `SpawnerDataModifier` |
| `getBaseMaxStoredExp()` / `setBaseMaxStoredExp(int)` | `int` / `SpawnerDataModifier` |
| `getBaseSpawnerDelay()` / `setBaseSpawnerDelay(long)` | `long` / `SpawnerDataModifier` |
| `applyChanges()` | `void` |

## Examples

### Read Spawner Data

```java
Location location = block.getLocation();
SpawnerDataDTO spawnerData = api.getSpawnerByLocation(location);

if (spawnerData != null) {
    player.sendMessage("ID: " + spawnerData.getSpawnerId());
    player.sendMessage("Mob: " + spawnerData.getEntityType());
    player.sendMessage("Stack: " + spawnerData.getStackSize());
    player.sendMessage("Delay: " + spawnerData.getBaseSpawnerDelay() + " ticks");
}
```

### Modify Spawner Properties

```java
SpawnerDataModifier modifier = api.getSpawnerModifier(spawnerData.getSpawnerId());

if (modifier != null) {
    modifier.setBaseMaxMobs(15)
            .setBaseMinMobs(5)
            .setBaseSpawnerDelay(400L)
            .setBaseMaxStoredExp(5000)
            .setBaseMaxStoragePages(3)
            .applyChanges();

    player.sendMessage("Spawner updated!");
}
```

### List All Spawners

```java
List<SpawnerDataDTO> all = api.getAllSpawners();
player.sendMessage("Total spawners: " + all.size());

for (SpawnerDataDTO s : all) {
    player.sendMessage("- " + s.getEntityType() + " at " + s.getLocation()
        + " (stack: " + s.getStackSize() + ")");
}
```

### Manage Sell Prices

The active price follows SmartSpawner's configured price-source mode. Price mutations affect the custom price store and are persisted only when custom prices are enabled.

```java
double diamondPrice = api.getSellPrice(Material.DIAMOND);

if (!api.hasSellPrice(Material.EMERALD)) {
    api.setSellPrice(Material.EMERALD, 125.0);
}

api.getCustomSellPrices().forEach((material, price) ->
    plugin.getLogger().info(material + " = " + price));
```

### Remove a Spawner

`removeSpawner` returns a `CompletableFuture<Boolean>` because the chunk may need to be loaded asynchronously. It completes with `true` after cleanup finishes, or `false` if the spawner does not exist or is already being modified.

```java
// Remove by ID
api.removeSpawner(spawnerId).thenAccept(removed -> {
    if (removed) {
        player.sendMessage("Spawner removed.");
    } else {
        player.sendMessage("Spawner not found or currently busy.");
    }
});

// Remove by location
api.removeSpawner(block.getLocation()).thenAccept(removed -> {
    player.sendMessage(removed ? "Removed." : "Nothing found there.");
});
```
