# PlugMan

PlugMan is a simple, easy to use plugin that lets server admins manage plugins from either in-game or console without the need to restart the server.

## Features
* Enable, disable, restart, load, reload, and unload plugins from in-game or console.
* List plugins alphabetically, with version if specified.
* Get useful information on plugins such as commands, version, author(s), etc.
* Easily manage plugins without having to constantly restart your server.
* List commands a plugin has registered.
* Find the plugin a command is registered to.
* Tab completion for command names and plugin names.
* Dump plugin list with versions to a file.
* Check if a plugin is up-to-date with dev.bukkit.org
* Permissions Support - All commands default to OP.

## Commands
| Command | Description |
| --------------- | ---------------- |
| /plugman help | Show help information. |
| /plugman list [-v] | List plugins in alphabetical order. Use "-v" to include versions. |
| /plugman info [plugin] | Displays information about a plugin. |
| /plugman dump | Dump plugin names and version to a file. |
| /plugman usage [plugin] | List commands that a plugin has registered. |
| /plugman lookup [command] | Find the plugin a command is registered to. |
| /plugman enable [plugin&#124;all] | Enable a plugin. |
| /plugman disable [plugin&#124;all] | Disable a plugin. |
| /plugman restart [plugin&#124;all] | Restart (disable/enable) a plugin. |
| /plugman load [plugin] | Load a plugin. |
| /plugman reload [plugin&#124;all] | Reload (unload/load) a plugin. |
| /plugman unload [plugin] | Unload a plugin. |
| /plugman check [plugin&#124;all] [-f] | Check if a plugin is up-to-date. |

## Permissions
| Permission Node | Default | Description |
| ------------------------- | ---------- | ---------------- |
| plugman.admin | OP | Allows use of all PlugMan commands. |
| plugman.update | OP | Allows user to see update messages. |
| plugman.help | OP | Allow use of the help command. |
| plugman.list | OP | Allow use of the list command. |
| plugman.info | OP | Allow use of the info command. |
| plugman.dump | OP | Allow use of the dump command. |
| plugman.usage | OP | Allow use of the usage command. |
| plugman.lookup | OP | Allow use of the lookup command. |
| plugman.enable | OP | Allow use of the enable command. |
| plugman.enable.all | OP | Allow use of the enable all command. |
| plugman.disable | OP | Allow use of the disable command. |
| plugman.disable.all | OP | Allow use of the disable all command. |
| plugman.restart | OP | Allow use of the restart command. |
| plugman.restart.all | OP | Allow use of the restart all command. |
| plugman.load | OP | Allow use of the load command. |
| plugman.reload | OP | Allow use of the reload command. |
| plugman.reload.all | OP | Allow use of the reload all command. |
| plugman.unload | OP | Allow use of the unload command. |
| plugman.check | OP | Allow use of the check command. |
| plugman.check.all | OP | Allow use of the check command. |

## Configuration
| File | URL |
| ----- | ------- |
| config.yml | https://github.com/r-clancy/PlugMan/blob/master/src/main/resources/config.yml |

## Sponsors

<div style="text-align:center" markdown="1">

![image](https://raw.githubusercontent.com/r-clancy/PlugMan/master/images/jetbrains_logo.png "JetBrains")

JetBrains is kindly supporting the PlugMan open source project with it's full-featured Java IDE.

Take a look a JetBrain's leading software products over on <a href="http://www.jetbrains.com/">their website.</a>

---

![image](https://raw.githubusercontent.com/r-clancy/PlugMan/master/images/yourkit_logo.png "YourKit")

YourKit is kindly supporting the PlugMan open source project with its full-featured Java Profiler.

YourKit, LLC is the creator of innovative and intelligent tools for profiling Java and .NET applications.

Take a look at YourKit's leading software products: <a href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java Profiler</a> and <a href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET Profiler</a>.

---

![image](https://raw.githubusercontent.com/r-clancy/PlugMan/master/images/intreppid_logo.png "Intreppid Logo")

Intreppid is kindly supporting the PlugMan open source project with hosting for the Jenkins build server along with a test platform for development.

Take a look at Intreppid's premium Minecraft server and dedicated servers on <a href="https://www.intreppid.com/">their website</a>.
