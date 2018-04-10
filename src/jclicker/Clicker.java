package jclicker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

public class Clicker{
    private long sleepDuration = 0;
    private int clickType = InputEvent.BUTTON1_DOWN_MASK;
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
                        if(!isRunLoop()){
                            worker.cancel(true);
                        }

                        // Simulate a click
                        clickBot.mousePress(clickType);
                        clickBot.mouseRelease(clickType);

                        // If we're double clicking, "click" again!
                        if (doubleClick) {
                            clickBot.mousePress(clickType);
                            clickBot.mouseRelease(clickType);
                        }

                        try {
                            System.out.println("Sleeping for " + sleepDuration + " millis");
                            Thread.sleep(sleepDuration);
                        } catch (InterruptedException e) {
                        }
                    }
                } else {
                    Thread.sleep(1000);
                    // While we haven't pressed the stop button
                    while (isRunLoop()) {
                        System.out.println("Clicking!");
                        clickBot.mousePress(clickType);
                        clickBot.mouseRelease(clickType);

                        // If we're double clicking, "click" again!
                        if (doubleClick) {
                            clickBot.mousePress(clickType);
                            clickBot.mouseRelease(clickType);
                        }

                        try {
                            Thread.sleep(sleepDuration);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                return null;
            }
        };

        worker.execute();
    }

    public void setSleepDuration(long sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public void setClickType(int clickType) {
        this.clickType = clickType;
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

    public boolean isRunLoop() {
        return runLoop;
    }
}
