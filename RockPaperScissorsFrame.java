import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class RockPaperScissorsFrame extends JFrame {
    // Declare the components of the frame
    JButton rockButton, paperButton, scissorsButton, quitButton;
    JPanel mainPanel, playPanel, statsPanel, resultPanel;
    JLabel titleLabel, playerWinsLabel, computerWinsLabel, tiesLabel, totalLabel;
    ImageIcon titleIcon, rockIcon, paperIcon, scissorsIcon, quitIcon;
    JTextField playerWinsField, computerWinsField, tiesField, totalField;
    JTextArea resultArea;
    JScrollPane resultScrollPane;
    int playerWins, computerWins, ties, total;
    Color backgroundColor = new Color(158, 194, 220);

    int playerRockMoveCount, playerPaperMoveCount, playerScissorsMoveCount;
    Move playerLastMove,playerCurrentMove, compterMove;
    ArrayList<Strategy> allStrategies = new ArrayList<Strategy>();
    ArrayList<Strategy> noCheatingStrategies = new ArrayList<Strategy>();
    String currentStrategy;
    int cheatCount;

    Random random = new Random();

    // Constructor to set up the frame
    RockPaperScissorsFrame() {
        // Set the properties of the frame
        setTitle("Rock Paper Scissors Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));
        // Create panels to hold the components

        createStatsPanel();
        mainPanel.add(statsPanel);

        createResultPanel();
        mainPanel.add(resultPanel);

        createPlayPanel();
        mainPanel.add(playPanel);

        add(mainPanel);

        createStrategies();

        setVisible(true); //this has to be the last line of the constructor so that all components are added before the frame is displayed

    }


    //methods to create the panels
    private void createPlayPanel() {
        playPanel = new JPanel();
        playPanel.setLayout(new GridLayout(1, 4));
        playPanel.setBorder(new TitledBorder(new EtchedBorder(), "Play Games"));
        playPanel.setBackground(backgroundColor);

        // Create an instance of JButton to add to the frame
        rockButton = new JButton("Rock");
        rockButton.addActionListener((ActionEvent event) -> {
            playerCurrentMove = Move.Rock;
            playerRockMoveCount++;
            compterMove = determineComputerMove();
            if (compterMove == Move.Rock) {
                ties++;
                tiesField.setText(Integer.toString(ties));
                resultArea.append("Rock vs Rock: It's a tie! ("+ currentStrategy +") \n");
            }
            else if (compterMove == Move.Paper) {
                computerWins++;
                computerWinsField.setText(Integer.toString(computerWins));
                resultArea.append("Rock vs Paper: Computer wins! ("+ currentStrategy +") \n");
            }
            else {
                playerWins++;
                playerWinsField.setText(Integer.toString(playerWins));
                resultArea.append("Rock vs Scissors: Player wins! ("+ currentStrategy +") \n");
            }
            playerLastMove = Move.Rock;
        });
        paperButton = new JButton("Paper");
        paperButton.addActionListener((ActionEvent event) -> {
            playerCurrentMove = Move.Paper;
            playerPaperMoveCount++;
            compterMove = determineComputerMove();
            if (compterMove == Move.Rock) {
                playerWins++;
                playerWinsField.setText(Integer.toString(playerWins));
                resultArea.append("Paper vs Rock: Player wins! ("+ currentStrategy +") \n");
            }
            else if (compterMove == Move.Paper) {
                ties++;
                tiesField.setText(Integer.toString(ties));
                resultArea.append("Paper vs Paper: It's a tie! ("+ currentStrategy +") \n");
            }
            else {
                computerWins++;
                computerWinsField.setText(Integer.toString(computerWins));
                resultArea.append("Paper vs Scissors: Computer wins! ("+ currentStrategy +") \n");
            }
            playerLastMove = Move.Paper;
        });
        scissorsButton = new JButton("Scissors");
        scissorsButton.addActionListener((ActionEvent event) -> {
            playerCurrentMove = Move.Scissors;
            playerScissorsMoveCount++;
            compterMove = determineComputerMove();
            if (compterMove == Move.Rock) {
                computerWins++;
                computerWinsField.setText(Integer.toString(computerWins));
                resultArea.append("Scissors vs Rock: Computer wins! ("+ currentStrategy +") \n");
            }
            else if (compterMove == Move.Paper) {
                playerWins++;
                playerWinsField.setText(Integer.toString(playerWins));
                resultArea.append("Scissors vs Paper: Player wins! ("+ currentStrategy +") \n");
            }
            else {
                ties++;
                tiesField.setText(Integer.toString(ties));
                resultArea.append("Scissors vs Scissors: It's a tie! ("+ currentStrategy +") \n");
            }
            playerLastMove = Move.Scissors;
        });
        quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        rockIcon = new ImageIcon("src/Rock.png");
        paperIcon = new ImageIcon("src/Paper.png");
        scissorsIcon = new ImageIcon("src/Scissors.png");
        quitIcon = new ImageIcon("src/Quit.png");

        rockButton.setIcon(rockIcon);
        paperButton.setIcon(paperIcon);
        scissorsButton.setIcon(scissorsIcon);
        quitButton.setIcon(quitIcon);

        playPanel.add(rockButton);
        playPanel.add(paperButton);
        playPanel.add(scissorsButton);
        playPanel.add(quitButton);
    }

    private Move determineComputerMove() {
        Strategy strategyToUse;
        if (total == 0) {
            strategyToUse = allStrategies.get(random.nextInt(allStrategies.size()));

            Strategy lastMoveStrategy = () -> performLastMoveStrategy();
            allStrategies.add(lastMoveStrategy);
            noCheatingStrategies.add(lastMoveStrategy);
        }
        else if ((float) cheatCount / total < 0.1) {
            strategyToUse = allStrategies.get(random.nextInt(allStrategies.size()));
        }
        else {
            strategyToUse = noCheatingStrategies.get(random.nextInt(noCheatingStrategies.size()));
        }
        Move computerMove = strategyToUse.determineMove();
        total++;
        totalField.setText(Integer.toString(total));
        return computerMove;
    }

    private int getComputerMove() {
        int computerMove = (int) (Math.random() * 3);
        return computerMove;
    }


    private void createStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 3));
        statsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        statsPanel.setBackground(backgroundColor);

        // Create an instance of JLabel to add to the frame
        playerWinsLabel = new JLabel("Player Wins");
        computerWinsLabel = new JLabel("Computer Wins");
        tiesLabel = new JLabel("Ties");
        totalLabel = new JLabel("Total Games");


        // Create an instance of JTextField to add to the frame
        playerWinsField = new JTextField();
        computerWinsField = new JTextField();
        tiesField = new JTextField();
        totalField = new JTextField();

        statsPanel.add(playerWinsLabel);
        statsPanel.add(computerWinsLabel);
        statsPanel.add(tiesLabel);
        statsPanel.add(totalLabel);
        statsPanel.add(playerWinsField);
        statsPanel.add(computerWinsField);
        statsPanel.add(tiesField);
        statsPanel.add(totalField);

    }

    private void createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Results"));
        resultPanel.setBackground(backgroundColor);
        resultArea = new JTextArea(13, 60);
        resultScrollPane = new JScrollPane(resultArea);


        resultArea.setEditable(false);
        resultPanel.add(resultScrollPane);
    }

    private void createStrategies() {
        Strategy randomStrategy = () -> performRandomStrategy();
        allStrategies.add(randomStrategy);
        noCheatingStrategies.add(randomStrategy);

        Strategy leastUsedStrategy = () -> performLeastUsedStrategy();
        allStrategies.add(leastUsedStrategy);
        noCheatingStrategies.add(leastUsedStrategy);

        Strategy mostUsedStrategy = () -> performMostUsedStrategy();
        allStrategies.add(mostUsedStrategy);
        noCheatingStrategies.add(mostUsedStrategy);

        Strategy cheatStrategy = () -> performCheatStrategy();
        allStrategies.add(cheatStrategy);
    }

    private Move performRandomStrategy() {
        currentStrategy = "Random";
        Move[] moves = Move.values();
        return moves[random.nextInt(moves.length)];
    }

    private Move performLeastUsedStrategy() {
        currentStrategy = "Least Used";
        Move leastUsedMove;
        if (playerRockMoveCount <= playerPaperMoveCount && playerRockMoveCount <= playerScissorsMoveCount) {
            leastUsedMove = Move.Rock;
        } else if (playerPaperMoveCount <= playerRockMoveCount && playerPaperMoveCount <= playerScissorsMoveCount) {
            leastUsedMove = Move.Paper;
        } else {
            leastUsedMove = Move.Scissors;
        }

        if (leastUsedMove == Move.Rock) {
            return Move.Paper;
        }
        else if (leastUsedMove == Move.Paper) {
            return Move.Scissors;
        }
        else {
            return Move.Rock;
        }
    }

    private Move performMostUsedStrategy() {
        currentStrategy = "Most Used";
        Move mostUsedMove;
        if (playerRockMoveCount >= playerPaperMoveCount && playerRockMoveCount >= playerScissorsMoveCount) {
            mostUsedMove = Move.Rock;
        } else if (playerPaperMoveCount >= playerRockMoveCount && playerPaperMoveCount >= playerScissorsMoveCount) {
            mostUsedMove = Move.Paper;
        } else {
            mostUsedMove = Move.Scissors;
        }
        if (mostUsedMove == Move.Rock) {
            return Move.Paper;
        }
        else if (mostUsedMove == Move.Paper) {
            return Move.Scissors;
        }
        else {
            return Move.Rock;
        }
    }

    private Move performCheatStrategy() {
        currentStrategy = "Cheat";
        cheatCount++;
        if (playerCurrentMove == Move.Rock) {
            return Move.Paper;
        } else if (playerCurrentMove == Move.Paper) {
            return Move.Scissors;
        } else {
            return Move.Rock;
        }
    }

    private Move performLastMoveStrategy() {
        currentStrategy = "Last Move";
        if (playerLastMove == Move.Rock) {
            return Move.Paper;
        } else if (playerLastMove == Move.Paper) {
            return Move.Scissors;
        } else {
            return Move.Rock;
        }
    }
}