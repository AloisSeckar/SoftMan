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

### &#x1F7E2; IDatabaseEntity
* interface for common DB-related operations
* implementation indicates target logical class contains data that are being persisted into DB
* such class always contains at least one "XyzInfo" attribute - an object that holds the actual DB data

#### Interface methods
* getId - adapter to access database object ID (generated long)
* persist - wrapper around DB-saving operations



### &#x1F7E1; AssociationManager
* top level entity embracing all the softball "world" inside the game.
* implemented as singleton to be referenced throughout the whole app

#### Attributes
* &#x1F7E2; year - season, that is currently being played (initially Constants.START_YEAR)
* &#x1F7E2; currentDate - in-game date
* &#x1F7E2; viewDate - GUI browser date (user may navigate both to past and future)
* &#x1F7E2; managedLeagues - hash map of leagues managed throughout the association history
* &#x1F7E2; registeredClubs - hash map of all clubs ever registed in association
* &#x1F7E2; registeredPlayers - hash map of all players ever registered in association
* &#x1F534; playerClub - shortcut to club controlled by human player
* &#x1F7E2; playerLeague - shortcut to league being played by player's team
* &#x1F7E2; playerTeam - shortcut to team controlled by player

#### Methods
* &#x1F535; getLeagues - get list of leagues for given year
* &#x1F7E1; createNewLeague - register new league into current year // full impl waiting for "league levels"
* &#x1F535; getClubs - get list of clubs (active only or all)
* &#x1F535; getClubById - find club by ID
* &#x1F7E1; registerClub - register new club // full impl waiting for "active" flags
* &#x1F7E1; retireClub - set "inactive" flag to given club // full impl waiting for "active" flags
* &#x1F534; Renew club registration for next year (club ID, year) // possible duplicate with "registerClub"
* &#x1F534; Get list of players
* &#x1F534; Register new player (player info)
* &#x1F534; Renew player registration for next year (player ID, year)
* &#x1F7E1; nextSeason - expire all active registrations + advance to next year // full impl waiting for "active" flags
* &#x1F534; Register team into league (league ID, team ID)
* TODO cover rest of methods

#### TODOs
* &#x1F534; Allow filtering of club and player list
* &#x1F534; Extract logical subdomains into separate "managers" (e.g. for clubs or date handling)
* &#x1F534; Remove "playerLeague" and "playerTeam" because the concept doesn't make sense in multi-league system
* &#x1F534; Club registration should cost money


### &#x1F7E1; Club
* local organizational unit 
* implements IDatabaseEntity

#### Attributes
* &#x1F7E2; clubInfo - basic info about the club (ID, name, city, stadium)
* &#x1F7E1; players - hashmap of all registered players // data type needs to change
* &#x1F7E2; teams - hashmap of teams participating in competitions

#### Methods
* &#x1F7E2; getClubId - get value of clubId (database table ID)
* &#x1F7E2; getClubInfo
* &#x1F7E2; getPlayers
* &#x1F7E2; getTeams
* &#x1F7E1; isActive - club has valid registration for current year // TODO impl
* TODO cover rest of methods

#### TODOs
* &#x1F534; Change playerList data type from "PlayerInfo" to "Player" (after introducing type)
* &#x1F534; Expand "stadium" from simple name to class with more attributes


### &#x1F7E1; Player
* the actual player 
* implements IDatabaseEntity

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