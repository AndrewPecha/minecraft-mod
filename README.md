# Test Minecraft Plugin - Spigot/Bukkit/Paper

Using [this tutorial](https://www.spigotmc.org/wiki/spigot-plugin-development/) for the plugin creation

Using a Paper server to test this [downloaded from here](https://papermc.io/downloads)

## Commands
### /kit
- defined in CommandKit.java
  - adds 20 brick and 1 diamond to the player's inventory
### /move
- defined in CommandMove.java
  - teleports the player to +100 blocks on the x axis

## Things to look out for when making mods
- according to [this documentation](https://www.spigotmc.org/wiki/plugin-yml/#required-attributes), you must include the java file name in the "main" declaration
- make sure to set the api-version in the plugins.yml and use the correct version, otherwise the server may complain about it being a legacy plugin
- 
