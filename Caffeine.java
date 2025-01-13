import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Caffeine {

    private static boolean capsLockOn = false; // Track the state of Caps Lock dynamically

    public Caffeine() {
    }

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        Random random = new Random();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // Create a hidden frame that listens for key events
        Frame hiddenFrame = new Frame();
        hiddenFrame.setSize(1, 1); // Very small size so it’s not visible
        hiddenFrame.setUndecorated(true); // Remove window decorations
        hiddenFrame.setVisible(true); // Make the frame invisible

        // Add a KeyListener to detect Caps Lock state change
        hiddenFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Detect when Caps Lock key is pressed
                if (e.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
                    // Check the actual state of Caps Lock when the key is pressed
                    capsLockOn = isCapsLockOn();
                    System.out.println("Caps Lock state changed: " + (capsLockOn ? "ON" : "OFF"));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No action needed on key release
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // No action needed on key typed
            }
        });

        executor.scheduleAtFixedRate(() -> {
            // Check the current Caps Lock state
            if (capsLockOn) {
                System.out.println("Caps Lock is ON. Moving mouse...");
                // Ensure the mouse is within the screen bounds
                int x = random.nextInt(1920); // Adjust to your screen width
                int y = random.nextInt(1080); // Adjust to your screen height
                robot.mouseMove(x, y); // Move the mouse randomly
            } else {
                System.out.println("Please turn Caps Lock ON to move the mouse");
            }

        }, 0L, 10L, java.util.concurrent.TimeUnit.SECONDS); // Run every 10 seconds
    }

    private static boolean isCapsLockOn() {
        // Check if Caps Lock is ON using the KeyEvent's current state
        return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
    }
}
