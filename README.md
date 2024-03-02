# Amusement Park

AmusementPark is a simple 2D amusement park simulator inspired by games like RollerCoaster Tycoon and SimThemePark. The game features a minimalistic 2D pixel art style, and mechanics such as NPC pathfinding are rudimentary. The game was programmed using the LibGDX Java Game Engine, but it relies minimally on engine components, with elements like the entity system being entirely custom-built. I primarily used this small project to enhance my programming skills and experiment with new programming paradigms.

## How to Compile

To compile you only need to have JDK11+ installed. Then run the gradlew script:
```
./gradlew desktop:dist
```
This will generate an executable .jar file in ./desktop/build/libs.

The last successful compile I did was with JDK15. I've had problems with JDK18 once so if you have problems you might want to try another version of Java.

## Controls

Taste | Funktion
--|--
WASD | Move Camera
CTRL + Mouse Wheel | Zoom In/Out
Mouse Wheel | Select building
Left click | Build selected building
Right click | Unselect Building 
Tab | Toggle Debug View
T | Spawn mechanic
R | Despawn mechanic
G | Spawn cleaner
F | Despawn cleaner
ESC | Exit
F11 | Toggle Fullscreen

## How to get started

At the top centre is the entrance to your park. Connect its middle tile with a path so that your visitors and staff can navigate through the park. Place litter bins at regular intervals along the paths. You can then add shops and rides to the paths. The entrance to the rides is always the bottom left tile. Visitors regularly express their needs in thought bubbles. Make sure you maintain a positive atmosphere in your park. You earn money when visitors make a purchase or use a ride.

