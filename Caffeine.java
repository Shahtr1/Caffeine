import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Caffeine {

    private static volatile boolean capsLockOn = false; // dynamic state
    private static final int INDICATOR_WIDTH = 80;
    private static final int INDICATOR_HEIGHT = 36;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        Random random = new Random();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        // Create UI on the EDT
        JFrame frame = new JFrame();
        SwingUtilities.invokeLater(() -> {
            frame.setUndecorated(true);
            frame.setSize(INDICATOR_WIDTH, INDICATOR_HEIGHT);
            frame.setAlwaysOnTop(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Place the frame at bottom-right corner (adjust as needed)
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int x = screen.width - INDICATOR_WIDTH - 10;
            int y = screen.height - INDICATOR_HEIGHT - 40;
            frame.setLocation(x, y);

            // Panel as the "flag" indicator
            JPanel panel = new JPanel(new BorderLayout());
            panel.setPreferredSize(new Dimension(INDICATOR_WIDTH, INDICATOR_HEIGHT));
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel label = new JLabel("Caps", SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setForeground(Color.WHITE);
            panel.add(label, BorderLayout.CENTER);

            frame.add(panel);
            frame.pack();
            frame.setVisible(true);

            // Expose components via client properties for updating from outside EDT
            frame.getRootPane().putClientProperty("indicatorPanel", panel);
            frame.getRootPane().putClientProperty("indicatorLabel", label);
        });

        // Periodically poll Caps Lock state and update the UI (runs on background
        // thread)
        executor.scheduleAtFixedRate(() -> {
            boolean state = false;
            try {
                state = Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK);
            } catch (UnsupportedOperationException ex) {
                // Some environments may not support getLockingKeyState
                System.err.println("Cannot query Caps Lock state on this platform: " + ex.getMessage());
            }

            capsLockOn = state;

            // Update Swing UI on EDT
            SwingUtilities.invokeLater(() -> {
                Component panelC = (Component) frame.getRootPane().getClientProperty("indicatorPanel");
                JLabel label = (JLabel) frame.getRootPane().getClientProperty("indicatorLabel");
                if (panelC != null && label != null) {
                    if (capsLockOn) {
                        panelC.setBackground(Color.GREEN.darker());
                        label.setText("CAPS ON");
                    } else {
                        panelC.setBackground(Color.RED.darker());
                        label.setText("caps off");
                    }
                    panelC.repaint();
                }
            });

        }, 0L, 200L, TimeUnit.MILLISECONDS); // poll every 200ms

        // Mouse movement task: only runs when capsLockOn == true
        executor.scheduleAtFixedRate(() -> {
            if (capsLockOn) {
                System.out.println("Caps Lock is ON. Moving mouse...");
                // Ensure the mouse is within reasonable screen bounds
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x = random.nextInt(screenSize.width);
                int y = random.nextInt(screenSize.height);
                robot.mouseMove(x, y);
            } else {
                System.out.println("Caps Lock is OFF. Not moving mouse.");
            }
        }, 0L, 10L, TimeUnit.SECONDS);

        // Add shutdown hook to stop executor when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdownNow();
            System.out.println("Shutting down.");
        }));

        // Optional: close app on window close (frame already set to EXIT_ON_CLOSE)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                executor.shutdownNow();
            }
        });
    }
}
