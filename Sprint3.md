

# Connection strings for Hörður #

Here are the server connection strings that unregister a player from a game and say that a player has finished a game.
  * Unregister : "controller.php?method=removeUserFromGame&gameId=" + gameId+"&playerId="+playerId;
  * Finished game : "controller.php?method=finishedGame&userId=" + playerId + "&gameId=" + gameId;

  * RegisterForGame  adds user to gameMember table