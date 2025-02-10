package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class HexGUI extends JFrame {

    private JButton[][] buttons;
    private JLabel statusLabel;
    private JPanel aiDecisionsPanel;
    private boolean playerTurn;
    private static final int HEX_SIZE = 70;
    private static final int SIDEBAR_HEX_SIZE = 30; // Smaller hex size for sidebar
    private static final Color PRIMARY_COLOR = new Color(34, 40, 49);
    private static final Color SECONDARY_COLOR = new Color(57, 62, 70);
    private static final Color RED_COLOR = new Color(235, 94, 91);
    private static final Color BLUE_COLOR = new Color(76, 159, 219);
    private static final Color HEX_COLOR = new Color(46, 52, 64); // Default hex color
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private List<AIDecision> currentAIDecisions = new ArrayList<>();
    private JPanel[][] sidebarHexPanels; // To hold hex panels in sidebar
    private JLabel[][] sidebarHeuristicLabels; // To hold heuristic labels in sidebar


    public HexGUI() {
        initializeGame();
        setupGUI();
        startGame();
    }

    private static class AIDecision {
        int x;
        int y;
        int score;

        public AIDecision(int x, int y, int score) {
            this.x = x;
            this.y = y;
            this.score = score;
        }
    }

    private void initializeGame() {
        Main.nodes = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 7; j++) {
                Main.nodes.add(new Node(i, j));
            }
        }
        Main.setNeighbors(Main.nodes);
    }

    private void setupGUI() {
        setTitle("Hex Master");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        getContentPane().setBackground(PRIMARY_COLOR);

        JSplitPane mainSplitPane = new JSplitPane();
        mainSplitPane.setDividerLocation(1000);
        mainSplitPane.setDividerSize(2);
        mainSplitPane.setBackground(PRIMARY_COLOR);

        JPanel gamePanel = createGameBoard();
        mainSplitPane.setLeftComponent(gamePanel);

        JPanel sidebar = createSidebar();
        mainSplitPane.setRightComponent(sidebar);

        add(mainSplitPane);
    }

    private JPanel createGameBoard() {
        JPanel boardPanel = new JPanel(null);
        boardPanel.setBackground(PRIMARY_COLOR);

        double hexWidth = HEX_SIZE * Math.sqrt(3);
        double hexHeight = HEX_SIZE * 1.5;

        buttons = new JButton[7][7];
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                final int x = row + 1;
                final int y = col + 1;

                int xOffset = (int) ((col * hexWidth) + (row * hexWidth / 2)) + 100;
                int yOffset = (int) (row * hexHeight * 0.75) + 80;

                JButton button = createHexButton(x, y);
                button.setBounds(xOffset, yOffset, (int) hexWidth, (int) hexHeight);
                boardPanel.add(button);
                buttons[row][col] = button;
            }
        }

        JLabel redDirection = new JLabel("RED: Connect Top ↔ Bottom");
        styleDirectionLabel(redDirection, RED_COLOR);
        redDirection.setBounds(500, 20, 300, 30);
        boardPanel.add(redDirection);

        JLabel blueDirection = new JLabel("BLUE: Connect Left ↔ Right");
        styleDirectionLabel(blueDirection, BLUE_COLOR);
        blueDirection.setBounds(500, 820, 300, 30);
        boardPanel.add(blueDirection);

        return boardPanel;
    }

    private void styleDirectionLabel(JLabel label, Color color) {
        label.setForeground(color);
        label.setFont(MAIN_FONT.deriveFont(Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SECONDARY_COLOR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel aiTitle = new JLabel("AI Thinking Visualization");
        aiTitle.setForeground(Color.WHITE);
        aiTitle.setFont(MAIN_FONT.deriveFont(Font.BOLD, 18));
        aiTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(aiTitle);

        JPanel thinkingPanel = new JPanel();
        thinkingPanel.setBackground(SECONDARY_COLOR);
        thinkingPanel.setLayout(new BoxLayout(thinkingPanel, BoxLayout.X_AXIS));
        JLabel thinkingLabel = new JLabel("AI is thinking...");
        thinkingLabel.setForeground(new Color(200, 200, 200));
        thinkingLabel.setFont(MAIN_FONT);
        thinkingPanel.add(thinkingLabel);
        thinkingPanel.setVisible(false);
        sidebar.add(thinkingPanel);

        aiDecisionsPanel = new JPanel();
        aiDecisionsPanel.setBackground(SECONDARY_COLOR);
        aiDecisionsPanel.setLayout(new BoxLayout(aiDecisionsPanel, BoxLayout.Y_AXIS));
        sidebar.add(aiDecisionsPanel);
        aiDecisionsPanel.setVisible(false); // Hide the top choices panel

        // Add Hex Grid visualization to sidebar
        JPanel sidebarBoardPanel = createSidebarBoard();
        sidebar.add(sidebarBoardPanel);


        sidebar.add(Box.createVerticalGlue());

        statusLabel = new JLabel();
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(MAIN_FONT.deriveFont(Font.BOLD, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(statusLabel);

        return sidebar;
    }


    private JPanel createSidebarBoard() {
        JPanel sidebarBoardPanel = new JPanel(null); // Null layout for hex grid
        sidebarBoardPanel.setBackground(SECONDARY_COLOR);
        sidebarHexPanels = new JPanel[7][7];
        sidebarHeuristicLabels = new JLabel[7][7];

        double hexWidth = SIDEBAR_HEX_SIZE * Math.sqrt(3);
        double hexHeight = SIDEBAR_HEX_SIZE * 1.5;

        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                final int x = row + 1;
                final int y = col + 1;

                int xOffset = (int) ((col * hexWidth) + (row * hexWidth / 2)) + 10; // Adjusted X offset
                int yOffset = (int) (row * hexHeight * 0.75) + 10; // Adjusted Y offset

                JPanel hexPanel = createSidebarHexPanel();
                hexPanel.setBounds(xOffset, yOffset, (int) hexWidth, (int) hexHeight);
                sidebarBoardPanel.add(hexPanel);
                sidebarHexPanels[row][col] = hexPanel;

                JLabel heuristicLabel = createHeuristicLabel(""); // Initially empty
                heuristicLabel.setBounds(0, 0, (int) hexWidth, (int) hexHeight);
                heuristicLabel.setHorizontalAlignment(SwingConstants.CENTER);
                heuristicLabel.setVerticalAlignment(SwingConstants.CENTER);
                hexPanel.add(heuristicLabel);
                sidebarHeuristicLabels[row][col] = heuristicLabel;
            }
        }
        return sidebarBoardPanel;
    }

    private JPanel createSidebarHexPanel() {
        JPanel panel = new JPanel(null) { // Null layout for label inside
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Polygon hex = new Polygon();
                int width = getWidth();
                int height = getHeight();

                hex.addPoint(width / 2, 0);
                hex.addPoint(width, height / 4);
                hex.addPoint(width, height * 3 / 4);
                hex.addPoint(width / 2, height);
                hex.addPoint(0, height * 3 / 4);
                hex.addPoint(0, height / 4);

                // Use the panel's background color to fill the hexagon
                g2.setColor(getBackground());
                g2.fillPolygon(hex);

                g2.setColor(new Color(255, 255, 255, 50)); // White border
                g2.setStroke(new BasicStroke(1)); // Thinner border
                g2.drawPolygon(hex);

                g2.dispose();
            }
        };
        panel.setOpaque(false); // Make panel transparent to see background
        return panel;
    }


    private JLabel createHeuristicLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(MAIN_FONT.deriveFont(Font.BOLD, 10)); // Smaller font for sidebar
        return label;
    }


    private JButton createHexButton(int x, int y) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Polygon hex = new Polygon();
                int width = getWidth();
                int height = getHeight();

                hex.addPoint(width/2, 0);
                hex.addPoint(width, height/4);
                hex.addPoint(width, height*3/4);
                hex.addPoint(width/2, height);
                hex.addPoint(0, height*3/4);
                hex.addPoint(0, height/4);

                g2.setColor(getBackground());
                g2.fillPolygon(hex);

                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(2));
                g2.drawPolygon(hex);

                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillPolygon(hex);
                }

                g2.dispose();
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(HEX_COLOR);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.printf("[Player Move] Selected: (%d,%d)%n", x, y);
                handlePlayerMove(x, y);
            }
        });
        return button;
    }

    private void startGame() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to make the first move? (Red Player)",
                "New Game", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        Main.redstart = (choice == JOptionPane.YES_OPTION);
        playerTurn = Main.redstart;
        Main.gameover = false;
        Main.win = false;
        Main.loose = false;
        updateGameState();
        updateSidebarHeuristics(); // Initialize sidebar heuristics to empty
    }

    private void updateGameState() {
        for (Node node : Main.nodes) {
            JButton button = buttons[node.x - 1][node.y - 1];
            button.setEnabled(playerTurn && node.color == '\0');

            if (node.color == 'R') {
                button.setBackground(RED_COLOR);
                button.setEnabled(false);
            } else if (node.color == 'B') {
                button.setBackground(BLUE_COLOR);
                button.setEnabled(false);
            } else {
                button.setBackground(HEX_COLOR);
            }
        }

        statusLabel.setText(playerTurn ? "Your Turn (Red)" : "AI Thinking...");
        revalidate();
        repaint();
    }

    private void handlePlayerMove(int x, int y) {
        if (!playerTurn) return;

        Main.entered_coordinate = x * 10 + y;
        Main.checkInput(Main.entered_coordinate);

        if (Main.Notvalid) {
            JOptionPane.showMessageDialog(this, "Invalid move! Try again.");
            Main.Notvalid = false;
        } else {
            playerTurn = false;
            updateGameState();
            checkGameOver();
            if (!Main.gameover) {
                aiMove();
            }
        }
    }

    private void aiMove() {
        currentAIDecisions.clear();
        updateAIDecisionDisplay(); // clear previous AI decisions display
        updateSidebarHeuristics();  // clear sidebar heuristics

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // AI calculates its move and updates heuristics in Main.nodes
                Main.update();
                // Update the sidebar with the computed heuristic values before they are reset
                updateSidebarHeuristicsInSwingWorker();

                // Process additional AI decision logic if needed
                List<Node> consideredNodes = new ArrayList<>();
                for (Node node : Main.nodes) {
                    if (node.color == '\0' && node.heuristicvalue != -1000) {
                        consideredNodes.add(node);
                    }
                }
                consideredNodes.sort(Comparator.comparingInt(Node::getHeuristicvalue).reversed());
                currentAIDecisions.clear();
                int count = 0;
                if (!consideredNodes.isEmpty()) {
                    int maxHeu = consideredNodes.get(0).heuristicvalue;
                    for (Node node : consideredNodes) {
                        if (count < 6) { // limit to top 6
                            currentAIDecisions.add(new AIDecision(node.x, node.y, node.heuristicvalue));
                            count++;
                        } else {
                            break;
                        }
                    }
                }
                System.out.println("Number of AI decisions calculated: " + currentAIDecisions.size());

                // Now, after the GUI has been updated with the computed values,
                // reset the heuristic values so that subsequent AI computations start clean.
                Main.resetnodesHeuristic(Main.nodes);

                return null;
            }

            @Override
            protected void done() {
                Node lastAIMove = null;
                for (Node node : Main.nodes) {
                    if (node.color == 'B') {
                        lastAIMove = node;
                    }
                }
                if (lastAIMove != null) {
                    System.out.println("Blue chose node : " + lastAIMove.x + " " + lastAIMove.y);
                }
                updateAIDecisionDisplay();  // update decision panel (if needed)
                playerTurn = true;
                updateGameState();
                checkGameOver();
            }
        };
        worker.execute();
    }


    private void updateSidebarHeuristics() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                sidebarHeuristicLabels[i][j].setText(""); // Clear heuristic labels
            }
        }
    }

    private void updateSidebarHeuristicsInSwingWorker() {
        System.out.println("Updating Sidebar Heuristics in SwingWorker...");

        // Collect unoccupied nodes with valid heuristics
        List<Node> unoccupiedNodes = new ArrayList<>();
        for (Node node : Main.nodes) {
            if (node.color == '\0' && node.heuristicvalue != -1000) {
                unoccupiedNodes.add(node);
            }
        }

        // Calculate max positive and min negative heuristics
        int maxPositive = unoccupiedNodes.stream()
                .filter(n -> n.heuristicvalue > 0)
                .mapToInt(n -> n.heuristicvalue)
                .max()
                .orElse(0);

        int minNegative = unoccupiedNodes.stream()
                .filter(n -> n.heuristicvalue < 0)
                .mapToInt(n -> n.heuristicvalue)
                .min()
                .orElse(0);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                int nodeIndex = i * 7 + j;
                if (nodeIndex < Main.nodes.size()) {
                    Node node = Main.nodes.get(nodeIndex);
                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            Color backgroundColor = HEX_COLOR;
                            if (node.color == 'R') {
                                backgroundColor = RED_COLOR;
                            } else if (node.color == 'B') {
                                backgroundColor = BLUE_COLOR;
                            } else if (node.heuristicvalue != -1000) {
                                if (node.heuristicvalue > 0) {
                                    if (maxPositive > 0) {
                                        float ratio = (float) node.heuristicvalue / maxPositive;
                                        int green = 255 - (int) (200 * ratio);
                                        backgroundColor = new Color(0, green, 0);
                                    }
                                } else if (node.heuristicvalue < 0) {
                                    if (minNegative < 0) {
                                        float ratio = (float) node.heuristicvalue / minNegative;
                                        int red = 255 - (int) (200 * ratio);
                                        backgroundColor = new Color(red, 0, 0);
                                    }
                                }
                            }
                            sidebarHexPanels[node.x - 1][node.y - 1].setBackground(backgroundColor);

                            // Update heuristic label (hide zero values)
                            if (node.heuristicvalue != -1000 && node.heuristicvalue != 0) {
                                sidebarHeuristicLabels[node.x - 1][node.y - 1]
                                        .setText(String.valueOf(node.heuristicvalue));
                            } else {
                                sidebarHeuristicLabels[node.x - 1][node.y - 1].setText("");
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Error: Node index out of bounds: " + nodeIndex);
                }
            }
        }
        System.out.println("Sidebar Heuristics Update Completed.");
    }




    private void updateAIDecisionDisplay() {
        System.out.println("Updating AI Decision Display - Panel is now hidden, this will do nothing visually."); // Debug print
        aiDecisionsPanel.removeAll(); // Still clear it in case you want to re-enable it later

        // No header or decision list display anymore
    }


    private JLabel createDecisionLabel(String text, boolean isHeader) {
        JLabel label = new JLabel(text);
        label.setForeground(isHeader ? new Color(200, 200, 255) : Color.WHITE);
        label.setFont(MAIN_FONT.deriveFont(isHeader ? Font.BOLD : Font.PLAIN));
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }

    private void checkGameOver() {
        Main.winCheck(Main.nodes);
        if (Main.gameover) {
            String message = Main.win ? "Congratulations! You Won!" : "AI Has Prevailed!";
            statusLabel.setText(message);
            JOptionPane.showMessageDialog(this, message);
            disableAllButtons();
        }
    }

    private void disableAllButtons() {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            HexGUI gui = new HexGUI();
            gui.setVisible(true);
        });
    }
}