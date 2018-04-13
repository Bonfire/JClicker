package jclicker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

public class Clicker{
    private long sleepDuration = 0;
    private int mouseButton = InputEvent.BUTTON1_DOWN_MASK;
    private boolean doubleClick = false;
    private boolean repeatUntil = false;
    private int repeatCount = 0;
    private boolean runLoop = false;

    Robot clickBot;
    SwingWorker worker;

    public Clicker(){
        try {
            clickBot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void simulateClicks() {
        setRunLoop(true);

        worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                // If we're going until a certain number of clicks, we can just use a for loop
                if (repeatUntil) {
                    Thread.sleep(1000);
                    System.out.println("Clicking " + repeatCount + " times");
                    for (int i = 0; i < repeatCount; i++) {
                        // If we've pressed stop, go ahead and cancel the work
                        if(!runLoop){
                            worker.cancel(true);
                            break;
                        }

                        // Simulate a click
                        clickBot.mousePress(mouseButton);
                        clickBot.mouseRelease(mouseButton);

                        // If we're double clicking, "click" again!
                        if (doubleClick) {
                            clickBot.mousePress(mouseButton);
                            clickBot.mouseRelease(mouseButton);
                        }

                        try {
                            System.out.println("Clicked, now sleeping for " + sleepDuration + " millis");
                            Thread.sleep(sleepDuration);
                        } catch (InterruptedException e) {
                            worker.cancel(true);
                            Thread.currentThread().interrupt();
                        }
                    }

                    runLoop = false;

                } else {
                    Thread.sleep(1000);
                    // While we haven't pressed the stop button
                    while (runLoop) {
                        System.out.println("Clicking!");
                        clickBot.mousePress(mouseButton);
                        clickBot.mouseRelease(mouseButton);

                        // If we're double clicking, "click" again!
                        if (doubleClick) {
                            clickBot.mousePress(mouseButton);
                            clickBot.mouseRelease(mouseButton);
                        }

                        try {
                            Thread.sleep(sleepDuration);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    runLoop = false;
                }
                return null;
            }
        };

        worker.execute();
    }

    public void setSleepDuration(long sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public void setMouseButton(int mouseButton) {
        this.mouseButton = mouseButton;
    }

    public void isDoubleClick(boolean doubleClick) {
        this.doubleClick = doubleClick;
    }

    public void isRepeatUntil(boolean repeatUntil) {
        this.repeatUntil = repeatUntil;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setRunLoop(boolean runLoop) {
        this.runLoop = runLoop;
    }

    public boolean getRunLoop() {
        return runLoop;
    }
}
