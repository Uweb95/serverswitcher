## Server Switcher

This plugin allows you to switch servers with custom commands.

### Configuration

The default configuration contains the lobby:

```
[[server]]
name = "lobby"
command = "lobby"
```

The name is the server name registered in the velocity configuration.  
The command is what you type in the chat, for example: ```/lobby```.

To add more servers, you can simply add a new block:

```
[[server]]
name = "lobby"
command = "lobby"

[[server]]
name = "farmworld"
command = "farm"
```

This plugin only works on Velocity proxies!  
There is currently no check whether a command has already been registered or not.