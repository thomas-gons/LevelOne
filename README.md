## Introduction

I've tried to make a generic game thus I see it more as a game engine than a real game.
The game is a top-down 2D game and a RPG-like game with an ECS architecture. All the basics features are implemented: 
movement, collision (just with map), interaction, inventory, spells, artefacts, consumables, enemies, etc.
Besides, I've tried to make the game as generic as possible so that it is easy to add new features. And 
I've tried to use advanced programming techniques such as design patterns, callbacks, reflection (but not implemented in this version),
genericity, etc. 


## How to play

- z q s d for movement
- a to interact with npc to buy consumables by clicking on them
- e to cast spell if you have enough mana; projectiles direction is the same as the player's
- & to switch between artefacts
- é t to switch between spells
- " to switch between consumables
- b to consume a consumable


- F3 to toggle debug mode
- F4 to toggle hit boxes
- space to toggle light


## Aim

As it is more a game engine than a real game, the victory condition is very simple: kill all the enemies.
Concerning the defeat condition, it is when the player die by losing all his health points or by falling into the void (e.g. holes).
