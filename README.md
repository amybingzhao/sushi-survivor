# game
First project for CompSci 308 Spring 2016

Name: Amy Zhao

Date started: 1/16/16

Date finished: 1/23/16

Hours worked: 20

Resources used:
* https://gist.github.com/jewelsea/2305098
* http://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
* http://fxapps.blogspot.com/2015/01/stacker-game-in-javafx.html

Main class file:
	Main.java

Data (non Java) files needed:
* images are in 'img' folder

How to play the game:
* Table level: use the arrow keys to dodge knives and collect shrimp. Each shrimp is +1 for your score, getting hit by knives kills you, and the goal is to make it to the end of the table (background scrolls to the end of the table).
* Customer level: use the left and right arrow keys to dodge soysauce drops and chopsticks. If you get hit by a soysauce drop, it'll slow you permanently, and if you get hit by the chopsticks you'll lose 2 shrimp (-2 score). If you end up with a score of 0, you lose instantly. Win by surviving the level (1 min. long) w/ a non-zero score.

Keys/Mouse input:
* use arrow keypad for movement and QWE and SPACE for cheats.

Cheat Keys:
* Q: clear all obstacles (knives in table level, soy sauce droplets in customer level)
* W: temporarily slow all other sprites on the screen besides yourself
* E: temporary speed boost for yourself
* SPACE: in table level, skip to the next level

Known bugs:
* If you press the same cheat key multiple times in quick succession, it'll get stuck on that cheat for W and E (e.g. if you press W twice in a row, the sprites might stay permanently slowed).
* To transition from table level to customer level, must hold ENTER down. Since it's being polled every 1 second, if you just press it, sometimes it misses the key press.

Extra features:
* Splash screen with loading bar

Impressions/Suggestions:
* Fun intro project, should strongly suggest that students in future semesters who've never used javafx before read up on game programming before school starts so they can hit the ground running
