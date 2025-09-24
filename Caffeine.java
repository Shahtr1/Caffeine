import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // we'll handle closing

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

            // Store components for later updates
            frame.getRootPane().putClientProperty("indicatorPanel", panel);
            frame.getRootPane().putClientProperty("indicatorLabel", label);

            // Add double-click listener to exit program
            frame.addMouseListener(new MouseAdapter() {
                private long lastClickTime = 0;

                @Override
                public void mouseClicked(MouseEvent e) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime < 400) { // double-click detected (within 400ms)
                        System.out.println("Double-click detected. Exiting...");
                        executor.shutdownNow();
                        frame.dispose();
                        System.exit(0);
                    }
                    lastClickTime = currentTime;
                }
            });
        });

        // Periodically poll Caps Lock state and update the UI
        executor.scheduleAtFixedRate(() -> {
            boolean state = false;
            try {
                state = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
            } catch (UnsupportedOperationException ex) {
                System.err.println("Cannot query Caps Lock state: " + ex.getMessage());
            }

            capsLockOn = state;

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
                }
            });
        }, 0L, 200L, TimeUnit.MILLISECONDS);

        // Mouse movement task: only runs when capsLockOn == true
        executor.scheduleAtFixedRate(() -> {
            if (capsLockOn) {
                System.out.println("Caps Lock is ON. Moving mouse...");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x = random.nextInt(screenSize.width);
                int y = random.nextInt(screenSize.height);
                robot.mouseMove(x, y);
            } else {
                System.out.println("Caps Lock is OFF. Not moving mouse.");
            }
        }, 0L, 10L, TimeUnit.SECONDS);

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdownNow));
    }
}
