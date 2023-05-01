package PoolGame;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import PoolGame.memento.*;
import PoolGame.objects.*;
import PoolGame.strategy.*;
import PoolGame.observer.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * Controls the game interface; drawing objects, handling logic and collisions.
 */
public class GameManager {

    private Table table;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private Line cue;
    private boolean cueSet = false;
    private boolean cueActive = false;
    private boolean winFlag = false;
    private int score = 0;

    private final double TABLEBUFFER = Config.getTableBuffer();
    private final double TABLEEDGE = Config.getTableEdge();
    private final double FORCEFACTOR = 0.1;
    private ArrayList<Button> buttons = Config.getButtons();

    private Scene scene;
    private GraphicsContext gc;

    // Game duration
    private double gameTime;
    private String gameTimeString;
    private String finalTime;
    private double fps = 0.017 / 2; // divided by 2 because GameManager.run() called twice in loading stage

    // Observer: score
    public ArrayList<Observer> observers = new ArrayList<>();
    private int ballScoreValue = 0; // the specific score value of the ball that has just been hit into a pocket

    // Memento: undo
    private Originator originator = new Originator();
    private Caretaker caretaker = new Caretaker();
    private int savedStates = 0;

    /**
     * Initialises timeline and cycle count.
     */
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Builds GameManager properties such as initialising pane, canvas,
     * graphicscontext, and setting events related to clicks.
     */
    public void buildManager() {
        Pane pane = new Pane();
        setClickEvents(pane);
        this.scene = new Scene(pane, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        Canvas canvas = new Canvas(table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        HBox box = new HBox();
        for (Button button : buttons) {
            box.getChildren().add(button);
        }
        pane.getChildren().add(box);
    }

    /**
     * Draws all relevant items - table, cue, balls, pockets - onto Canvas.
     * Used Exercise 6 as reference.
     */
    private void draw() {
        tick();

        // Fill in background
        gc.setFill(Paint.valueOf("white"));
        gc.fillRect(0, 0, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);

        // Fill in edges
        gc.setFill(Paint.valueOf("brown"));
        gc.fillRect(TABLEBUFFER - TABLEEDGE, TABLEBUFFER - TABLEEDGE, table.getxLength() + TABLEEDGE * 2,
                table.getyLength() + TABLEEDGE * 2);

        // Fill in Table
        gc.setFill(table.getColour());
        gc.fillRect(TABLEBUFFER, TABLEBUFFER, table.getxLength(), table.getyLength());

        // Fill in Pockets
        for (Pocket pocket : table.getPockets()) {
            gc.setFill(Paint.valueOf("black"));
            gc.fillOval(pocket.getxPos() - pocket.getRadius(), pocket.getyPos() - pocket.getRadius(),
                    pocket.getRadius() * 2, pocket.getRadius() * 2);
        }

        // Cue
        if (this.cue != null && cueActive && !winFlag) {
            gc.setStroke(Paint.valueOf("BROWN"));
            gc.setLineWidth(5);
            gc.strokeLine(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());
        }

        for (Ball ball : balls) {
            if (ball.isActive()) {
                gc.setFill(ball.getColour());
                gc.fillOval(ball.getxPos() - ball.getRadius(),
                        ball.getyPos() - ball.getRadius(),
                        ball.getRadius() * 2,
                        ball.getRadius() * 2);
            }

        }
        
        // After cue
        gc.setLineWidth(1);

        // Win
        if (winFlag) {
            gc.setStroke(Paint.valueOf("white"));
            gc.setFont(new Font("Impact", 80));
            gc.strokeText("Win and bye", table.getxLength() / 2 + TABLEBUFFER - 180,
                    table.getyLength() / 2 + TABLEBUFFER);

            gc.setFont(new Font("Verdana", 20));
            gc.setStroke(Paint.valueOf("black"));
            gc.strokeText(String.format("Final Score: %d", score), table.getxLength() / 2 + TABLEBUFFER - 180,
            table.getyLength() / 2 + TABLEBUFFER - 100);
            gc.strokeText(String.format("Final Time: %s", finalTime), table.getxLength() / 2 + TABLEBUFFER - 5,
            table.getyLength() / 2 + TABLEBUFFER - 100);
        }
        else {
            // Timer
            getGameTime();
            gc.setStroke(Paint.valueOf("black"));
            gc.strokeText(String.format("Time: %s", gameTimeString), table.getxLength() / 2 - 50, 15);

            // Score
            gc.strokeText(String.format("Score: %d", score), table.getxLength() / 2 + 100, 15);
        }
    }

    /**
    * Gets game time in min:secs.
    *
    */
    public void getGameTime() {
        this.gameTime += fps;
        long milli_sec = (long) this.gameTime * 1000;

        this.gameTimeString = String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(milli_sec), 
        TimeUnit.MILLISECONDS.toSeconds(milli_sec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milli_sec)));
    }

    /**
     * Updates score to add to game score using a BallObserver.
     */
    public void updateBallScore(Observer observer) {
        // System.out.print(observer.isActive());
        // if (! observer.isActive()) {
        //     this.ballScoreValue = observer.getScoreValue();
        // }
        this.ballScoreValue = observer.getScoreValue();
    } 

    /**
     * Checks if game has been won (i.e. only cue ball left)
     * 
     * @return true or false for game won
     */
    public boolean checkWin() {
        int activeCount = 0;
        boolean cueFlag = false;
        
        for (Ball ball : balls) {
            if (ball.isActive()) {
                cueFlag = ball.isCue();
                activeCount++;
            }
        }

        if (activeCount == 1 && cueFlag) {
            return true;
        }

        return false;
    }

    /**
     * Updates positions of all balls, handles logic related to collisions.
     * Used Exercise 6 as reference.
     */
    public void tick() {
        if (checkWin()) {
            finalTime = gameTimeString;
            winFlag = true;

        }

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);

            // Observer Pattern
            Observer observer = observers.get(i);
            ball.update();
            updateBallScore(observer);

            // added to codebase to only check active balls
            if (ball.isActive()) {
                ball.tick();

                if (ball.isCue() && cueSet && !winFlag) {
                    hitBall(ball);
                }

                double width = table.getxLength();
                double height = table.getyLength();

                // Check if ball landed in pocket
                for (Pocket pocket : table.getPockets()) {
                    if (pocket.isInPocket(ball)) {
                        if (ball.isCue()) {
                            this.reset();
                        } else {
                            if (ball.remove()) {
                                // Observer Pattern
                                score += this.ballScoreValue;
                                // ball.detach(observer);
                            } else {
                                // Check if when ball is removed, any other balls are present in its space. (If
                                // another ball is present, blue ball is removed)
                                for (Ball otherBall : balls) {
                                    double deltaX = ball.getxPos() - otherBall.getxPos();
                                    double deltaY = ball.getyPos() - otherBall.getyPos();
                                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                                    if (otherBall != ball && otherBall.isActive() && distance < 10) {
                                        ball.remove();
                                        score += this.ballScoreValue;
                                    }
                                }

                                if (ball.isActive()) {
                                    score += this.ballScoreValue;
                                }
                            }
                        }
                        break;
                    }
                }

                // Handle the edges (balls don't get a choice here)
                if (ball.getxPos() + ball.getRadius() > width + TABLEBUFFER) {
                    ball.setxPos(width - ball.getRadius());
                    ball.setxVel(ball.getxVel() * -1);
                }
                if (ball.getxPos() - ball.getRadius() < TABLEBUFFER) {
                    ball.setxPos(ball.getRadius());
                    ball.setxVel(ball.getxVel() * -1);
                }
                if (ball.getyPos() + ball.getRadius() > height + TABLEBUFFER) {
                    ball.setyPos(height - ball.getRadius());
                    ball.setyVel(ball.getyVel() * -1);
                }
                if (ball.getyPos() - ball.getRadius() < TABLEBUFFER) {
                    ball.setyPos(ball.getRadius());
                    ball.setyVel(ball.getyVel() * -1);
                }

                // Apply table friction
                double friction = table.getFriction();
                ball.setxVel(ball.getxVel() * friction);
                ball.setyVel(ball.getyVel() * friction);

                // Check ball collisions
                for (Ball ballB : balls) {
                    if (checkCollision(ball, ballB)) {
                        Point2D ballPos = new Point2D(ball.getxPos(), ball.getyPos());
                        Point2D ballBPos = new Point2D(ballB.getxPos(), ballB.getyPos());
                        Point2D ballVel = new Point2D(ball.getxVel(), ball.getyVel());
                        Point2D ballBVel = new Point2D(ballB.getxVel(), ballB.getyVel());
                        Pair<Point2D, Point2D> changes = calculateCollision(ballPos, ballVel, ball.getMass(), ballBPos,
                                ballBVel, ballB.getMass(), false);
                        calculateChanges(changes, ball, ballB);
                    }
                }
            }
        }
    }

    /**
     * Resets the game.
     */
    public void reset() {
        for (Ball ball : balls) {
            ball.reset();
        }

        this.gameTime = 0;
        this.score = 0;
    }

    /**
     * @return scene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Sets the table of the game.
     * 
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Sets the balls of the game.
     * Sets up observers of balls.
     * 
     * @param balls
     */
    public void setBalls(ArrayList<Ball> balls) {
        observers.clear();
        this.balls = balls;

        // Observer Pattern - attach each ball to observers
        for (Ball ball : balls) {
            Observer observer = new BallObserver(ball);
            observers.add(observer);
            ball.attach(observer);
        }
    }

    /**
     * Hits the ball with the cue, distance of the cue indicates the strength of the
     * strike.
     * 
     * @param ball
     */
    private void hitBall(Ball ball) {
        double deltaX = ball.getxPos() - cue.getStartX();
        double deltaY = ball.getyPos() - cue.getStartY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Check that start of cue is within cue ball
        if (distance < ball.getRadius()) {
            // Collide ball with cue
            double hitxVel = (cue.getStartX() - cue.getEndX()) * FORCEFACTOR;
            double hityVel = (cue.getStartY() - cue.getEndY()) * FORCEFACTOR;

            ball.setxVel(hitxVel);
            ball.setyVel(hityVel);
        }

        cueSet = false;

    }

    /**
     * Changes values of balls based on collision (if ball is null ignore it)
     * 
     * @param changes
     * @param ballA
     * @param ballB
     */
    private void calculateChanges(Pair<Point2D, Point2D> changes, Ball ballA, Ball ballB) {
        ballA.setxVel(changes.getKey().getX());
        ballA.setyVel(changes.getKey().getY());
        if (ballB != null) {
            ballB.setxVel(changes.getValue().getX());
            ballB.setyVel(changes.getValue().getY());
        }
    }

    /**
     * Sets the cue to be drawn on click, and manages cue actions
     * Manages key presses.
     * 
     * @param pane
     */
    private void setClickEvents(Pane pane) {
        pane.setOnMousePressed(event -> {
            cue = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            cueSet = false;
            cueActive = true;
        });

        pane.setOnMouseDragged(event -> {
            cue.setEndX(event.getX());
            cue.setEndY(event.getY());
        });

        pane.setOnMouseReleased(event -> {
            cueSet = true;
            cueActive = false;
            savedStates++;
            saveState();
        });

        pane.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.R) {
                cheatRemove("red");
            } else if (event.getCode() == KeyCode.Y) {
                cheatRemove("yellow");
            } else if (event.getCode() == KeyCode.G) {
                cheatRemove("green");
            } else if (event.getCode() == KeyCode.X) {
                cheatRemove("brown");
            } else if (event.getCode() == KeyCode.B) {
                cheatRemove("blue");
            } else if (event.getCode() == KeyCode.P) {
                cheatRemove("purple");
            } else if (event.getCode() == KeyCode.Z) {
                cheatRemove("black");
            } else if (event.getCode() == KeyCode.O) {
                cheatRemove("orange");
            }

            else if (event.getCode() == KeyCode.U) {
                // Do not allow undo if no moves
                if (savedStates > 0 && !winFlag) {
                    undo();
                }
            }
        });
    }

    /**
     * Saves the current game state (balls, score, and time)
     */
    public void saveState() {
        originator.setBalls(saveBalls());
        originator.setScore(score);
        originator.setTime(gameTime);
        caretaker.add(originator.saveStateToMemento());
    }

    /**
     * Clones the current balls into a new balls list that will be saved
     * 
     * @return The cloned list of balls.
     */
    public ArrayList<Ball> saveBalls() {
        ArrayList<Ball> ballsToSave = new ArrayList<Ball>();
        PocketStrategy strategyToSave;
        for (Ball ball: balls) {
            if (ball.isActive()) {
                strategyToSave = ball.getPocketStrategy();
                if (ball.getColourString().equals("white")) {
                    strategyToSave = new BallStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("blue")) {
                    strategyToSave = new BlueStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("green")) {
                    strategyToSave = new BlueStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("purple")) {
                    strategyToSave = new BlueStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("black")) {
                    strategyToSave = new BlackBrownStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("brown")) {
                    strategyToSave = new BlackBrownStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("red")) {
                    strategyToSave = new BallStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("orange")) {
                    strategyToSave = new BallStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                } else if (ball.getColourString().equals("yellow")) {
                    strategyToSave = new BallStrategy();
                    strategyToSave.setLives(ball.getPocketStrategy().getLives());
                }
                Ball ballToSave = new Ball(ball.getColourString(), ball.getxPos() - TABLEBUFFER, ball.getyPos() - TABLEBUFFER, ball.getxVel(), ball.getyVel(), 
                ball.getMass(), ball.isCue(), strategyToSave, ball.getScoreValue());
                ballsToSave.add(ballToSave);
            }
        }
        return ballsToSave;
    }

    /**
     * Undos the current game state to the last move
     */
    public void undo() {
        originator.getStateFromMemento(caretaker.get());
        setBalls(originator.getBalls());
        score = originator.getScore();
        gameTime = originator.getTime();
    }

    /**
     * Removes the ball/balls of a user specified colour when that key colour is pressed.
     * 
     * @param colour the colour of the ball/balls to be removed.
     */
    public void cheatRemove(String colour) {
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            if (ball.getColourString().equals(colour)) {
                if (ball.isActive()) {
                    Observer observer = observers.get(i);
                    ball.update();
                    updateBallScore(observer);
                    ball.cheatRemove();
                    score += this.ballScoreValue;
                }
            }
        }
    }

    /**
     * Checks if two balls are colliding.
     * Used Exercise 6 as reference.
     * 
     * @param ballA
     * @param ballB
     * @return true if colliding, false otherwise
     */
    private boolean checkCollision(Ball ballA, Ball ballB) {
        if (ballA == ballB) {
            return false;
        }

        return Math.abs(ballA.getxPos() - ballB.getxPos()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getyPos() - ballB.getyPos()) < ballA.getRadius() + ballB.getRadius();
    }

    /**
     * Collision function adapted from assignment, using physics algorithm:
     * http://www.gamasutra.com/view/feature/3015/pool_hall_lessons_fast_accurate_.php?page=3
     *
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA     The mass of ball A (for the moment this should always be the
     *                  same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB     The mass of ball B (for the moment this should always be the
     *                  same as ball A)
     *
     * @return A Pair in which the first (key) Point2D is the new
     *         delta x,y vector for ball A, and the second (value) Point2D is the
     *         new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA,
            Point2D positionB, Point2D velocityB, double massB, boolean isCue) {

        // Find the angle of the collision - basically where is ball B relative to ball
        // A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this
        // allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide
        // again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the
        // original velocities
        if (vB <= 0 && vA >= 0 && !isCue) {
            return new Pair<>(velocityA, velocityB);
        }

        // This is the optimisation function described in the gamasutra link. Rather
        // than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their
        // final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        return new Pair<>(velAPrime, velBPrime);
    }
}
