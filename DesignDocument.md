# SoftMan
This file is tracking down core concepts + implementation progress

#### Progress markup
* &#x1F7E2; feature fully completed
* &#x1F535; implemented, but not covered with tests yet (methods)
* &#x1F7E1; partly implemented
* &#x1F534; to be implemented


# Features
* TODO


# Logical Structure

### &#x1F7E1; AssociationManager
Top level entity embracing all the softball "world" inside the game.

#### Notes
* implemented as singleton to be referenced throughout the whole app

#### Attributes
* &#x1F7E2; year - season, that is currently being played (initially Constants.START_YEAR)
* &#x1F7E2; currentDate - in-game date
* &#x1F7E2; viewDate - GUI browser date (user may navigate both to past and future)
* &#x1F7E2; activeLeagues - list of current (this year) leagues
* &#x1F7E2; archivedLeagues - list of past years leagues
* &#x1F534; playerClub - shortcut to club controlled by human player
* &#x1F7E2; playerLeague - shortcut to league being played by player's team
* &#x1F7E2; playerTeam - shortcut to team controlled by player
* &#x1F7E2; activeClubs - list of current (this year registered) clubs
* &#x1F7E2; archivedClubs - list of past clubs
* &#x1F534; List of registered players

#### Methods
* &#x1F7E2; getClubs - get list of clubs (active only or all)
* &#x1F7E1; getClubById - find club by ID // waiting for changing lists to maps
* &#x1F535; registerClub - register new club (as active)
* &#x1F535; retireClub - remove club from active list
* &#x1F534; Renew club registration for next year (club ID, year)
* &#x1F534; Get list of players
* &#x1F534; Register new player (player info)
* &#x1F534; Renew player registration for next year (player ID, year)
* &#x1F535; getLeagues - get list of leagues for given year
* &#x1F7E1; createNewLeague - register new league into current year (league level) // full impl waiting for "league levels"
* &#x1F535; nextSeason - archive all leagues at the end of year + advance to next year
* &#x1F534; Register team into league (league ID, team ID)
* TODO cover rest of methods

#### TODOs
* &#x1F534; Change lists to ID maps for faster searching
* &#x1F534; Allow filtering of club and player list
* &#x1F534; Extract logical subdomains into separate "managers" (e.g. for clubs or date handling)
* &#x1F534; Remove "playerLeague" and "playerTeam" because the concept doesn't make sense in multi-league system
* &#x1F534; Club registration should cost money


### &#x1F7E1; Club
Local organizational unit 

#### Attributes
* &#x1F7E2; name
* &#x1F7E2; city
* &#x1F7E2; stadium
* &#x1F7E1; players - list of all registered players // data type needs to change
* &#x1F7E2; teams - list of teams participating in competitions

#### Methods
* &#x1F534; Get basic info
* &#x1F7E2; getPlayers
* &#x1F7E2; getTeams
* TODO cover rest of methods

#### TODOs
* &#x1F534; Change lists to ID maps for faster searching
* &#x1F534; Change playerList data type from "PlayerInfo" to "Player" (after introducing type)
* &#x1F534; Expand "stadium" from simple name to class with more attributes


### &#x1F7E1; Player
The actual player 

#### Attributes
* &#x1F7E2; Name
* &#x1F7E2; Gender
* &#x1F7E2; Birthdate
* &#x1F7E2; Number
* &#x1F7E2; Attributes
* &#x1F534; List of all registrations
* &#x1F534; List of all team assignments
* &#x1F534; List of all stats

#### Methods
* &#x1F534; Get basic info
* &#x1F7E2; Get age from birthdate
* &#x1F7E2; Get attributes


This file is still a stub and will be expanded...