# SoftMan
This file is tracking down core concepts + implementation progress

# Features
* TODO

# Logical Structure

## &#x1F7E1; Association manager
Top level entity embracing all the softball "world" inside the game.

### Attributes
* &#x1F534; List of registered clubs
* &#x1F534; List of registered players
* &#x1F534; List of managed leagues

### Methods
* &#x1F534; Get list of clubs
* &#x1F534; Register new club (club info)
* &#x1F534; Renew club registration for next season (club ID, season)
* &#x1F534; Get list of players
* &#x1F534; Register new player (player info)
* &#x1F534; Renew player registration for next season (player ID, season)
* &#x1F534; Get list of leagues
* &#x1F534; Create new league (season, league level)
* &#x1F534; Register team into league (team ID)

## &#x1F534; Club
Local organizational unit 

### Attributes
* &#x1F534; Name
* &#x1F534; City
* &#x1F534; Stadium
* &#x1F534; List of registered players
* &#x1F534; List of teams participating in competitions

### Methods
* &#x1F534; Get basic info
* &#x1F534; Get list of registered players
* &#x1F534; Get list of teams
* &#x1F534; Register team into league

## &#x1F7E1; Player
The actual player 

### Attributes
* &#x1F7E2; Name
* &#x1F7E2; Gender
* &#x1F7E2; Birthdate
* &#x1F7E2; Number
* &#x1F7E2; Attributes
* &#x1F534; List of all registrations
* &#x1F534; List of all team assignments
* &#x1F534; List of all stats

### Methods
* &#x1F534; Get basic info
* &#x1F7E2; Get age from birthdate
* &#x1F7E2; Get attributes


This file is still a stub and will be expanded...