How to run code
    - To run the code us gradle run, and to generate the javadoc use gradle javadoc. When setting a colour in a config file use
    all lowercase.
List of implemented extension features
    - Pockets and More Coloured Balls, except for the visible player-controlled cue stick
    - Difficulty Level 
    - Time and Score
    - Undo and Cheat
List of names of the design patterns used in extension, providing the corresponding class and file names regarding these patterns
    - Strategy Pattern: Classes inside difficulty_strategy folder, including Menu.java, LevelStrategy.java, EasyLevel.java, 
    NormalLevel.java, HardLevel.java. Menu is called in App.java
    - Memento Pattern: Classes inside memento folder, including Origintor.java, Caretaker.java, and Memento.java. They are called in
    saveState() and undo() of GameManager.java.
    - Observer Pattern: Classes inside observer folder, including Observer.java, BallObserver.java, Subject.java, and Ball.java in 
    objects folder implements Subject. Used in tick() and updateBallScore() of GameManager.java.
How to select difficulty level, how to undo, and how to cheat
    - To select difficulty, click one of the buttons - "Easy", "Normal", or "Hard". Game difficulty defaults to Easy on inital
    load. 
    - To undo, press the U key, and the game state before the last move will be loaded.
    - To cheat, press the key corresponding to the first letter of the colour (i.e. O for orange, Y for yellow, B for blue
    R for red, G for green, P for purple) EXCEPT for brown and black ball - press X for brown and Z for black.
Additional info
    - Javadoc can be generated using gradle javadoc and found in build/docs/javadoc folder