# SoftMan
This file is tracking down core concepts + implementation progress

##### Progress markup
* &#x1F7E2; feature fully completed
* &#x1F535; implemented, but not covered with tests yet (methods)
* &#x1F7E1; partly implemented
* &#x1F534; to be implemented


# Features
* TODO


# Logical Structure

### &#x1F7E1; AssociationManager
Top level entity embracing all the softball "world" inside the game.

##### Notes
* implemented as singleton to be referenced throughout the whole app

##### Attributes
* &#x1F7E2; year - season, that is currently being played (initially Constants.START_YEAR)
* &#x1F7E2; currentDate - in-game date
* &#x1F7E2; viewDate - GUI browser date (user may navigate both to past and future)
* &#x1F7E2; activeLeagues - List of current (this year) leagues
* &#x1F7E2; archivedLeagues - List of past years leagues
* &#x1F534; playerClub - shortcut to club controlled by human player
* &#x1F7E2; playerLeague - shortcut to league being played by player's team
* &#x1F7E2; playerTeam - shortcut to team controlled by player
* &#x1F534; List of registered clubs
* &#x1F534; List of registered players

##### Methods
* &#x1F534; Get list of clubs
* &#x1F534; Register new club (club info)
* &#x1F534; Renew club registration for next year (club ID, year)
* &#x1F534; Get list of players
* &#x1F534; Register new player (player info)
* &#x1F534; Renew player registration for next year (player ID, year)
* &#x1F535; List<League> getLeagues(int year) - get list of leagues (year)
* &#x1F7E1; void createNewLeague() - register new league into current year (league level) // full impl waiting for "league levels"
* &#x1F535; void nextSeason() - archive all leagues at the end of year + advance to next year
* &#x1F534; Register team into league (league ID, team ID)
* TODO cover rest of methods

##### TODOs
* &#x1F534; Extract current date/viewing dates handling into separate class
* &#x1F534; Remove "playerLeague" and "playerTeam" because the concept doesn't make sense in multi-league system

### &#x1F534; Club
Local organizational unit 

##### Attributes
* &#x1F534; Name
* &#x1F534; City
* &#x1F534; Stadium
* &#x1F534; List of registered players
* &#x1F534; List of teams participating in competitions

##### Methods
* &#x1F534; Get basic info
* &#x1F534; Get list of registered players
* &#x1F534; Get list of teams
* &#x1F534; Register team into league

### &#x1F7E1; Player
The actual player 

##### Attributes
* &#x1F7E2; Name
* &#x1F7E2; Gender
* &#x1F7E2; Birthdate
* &#x1F7E2; Number
* &#x1F7E2; Attributes
* &#x1F534; List of all registrations
* &#x1F534; List of all team assignments
* &#x1F534; List of all stats

##### Methods
* &#x1F534; Get basic info
* &#x1F7E2; Get age from birthdate
* &#x1F7E2; Get attributes


This file is still a stub and will be expanded...