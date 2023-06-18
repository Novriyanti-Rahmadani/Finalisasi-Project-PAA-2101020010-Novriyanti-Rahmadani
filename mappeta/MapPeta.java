package mappeta;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MapPeta extends JFrame {
    private int previousRedDroidX;
    private int previousRedDroidY;
    private static final int CELL_SIZE = 30;
    private static final int MAP_SIZE = 15;
    private static final int WINDOW_SIZE = CELL_SIZE * MAP_SIZE;
    private boolean isMoving = true;
    private Timer timer;
    private int redDroidX = 2;
    private int redDroidY = 2;
    private int greenDroidX = 12;
    private int greenDroidY = 12;
    private boolean[][] walls;
    private int redDroidCount = 1;
    private int greenDroidSight = 1; // Jarak pandang droid hijau (default: 1)
    private JSlider sightSlider; // Slider untuk mengatur jarak pandang droid hijau

    

    public MapPeta() {
        setTitle("Map Peta");
        setSize(WINDOW_SIZE, WINDOW_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        sightSlider = new JSlider(1, 5, 1);
        sightSlider.setMajorTickSpacing(1);
        sightSlider.setSnapToTicks(true);
        sightSlider.setPaintTicks(true);
        sightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                greenDroidSight = sightSlider.getValue();
            }
        });

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMoving = true;
                startDroidMovement();
            }
        });
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMoving = false;
                stopDroidMovement();
            }
        });
        JButton acakPetaButton = new JButton("Acak Peta");
        acakPetaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                acakPeta();
                repaint();
            }
        });
        JButton acakRedDroidButton = new JButton("Acak Droid Merah");
        acakRedDroidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleRedDroid();
                repaint();
            }
        });
        JButton acakGreenDroidButton = new JButton("Acak Droid Hijau");
        acakGreenDroidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleGreenDroid();
                repaint();
            }
        });
        JButton addRedDroidButton = new JButton("Tambah Droid Merah");
        addRedDroidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRedDroid();
                repaint();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(acakPetaButton);
        buttonPanel.add(acakRedDroidButton);
        buttonPanel.add(acakGreenDroidButton);
      
        
        add(buttonPanel, BorderLayout.SOUTH);

        // Inisialisasi peta dengan pola tembok default
        walls = new boolean[MAP_SIZE][MAP_SIZE];
        setDefaultWalls();
    }
    private void setDefaultWalls() {
        boolean[][] defaultWalls = {
                {false, true, true, true, true, true, true, true, true, true, true, true, true, false, true},
                {false, true, true, false, true, false, false, false, false, false, false, true, false, false, true},
                {false, false, true, true, true, true, true, false, true, true, true, true, true, true, true},
                {true, true, true, false, true, true, false, true, false, true, false, false, true, false, true},
                {true, false, true, false, true, true, true, true, true, true, true, false, true, false, true},
                {true, false, true, true, true, false, false, false, false, true, false, false, true, false, true},
                {true, true, false, false, true, false, true, true, false, true, true, true, true, false, true},
                {true, false, true, false, true, false, true, false, true, false, false, false, true, false, true},
                {true, false, true, true, true, true, true, true, false, true, true, false, true, false, true},
                {true, false, true, false, true, false, true, true, false, false, true, false, true, false, true},
                {true, true, true, true, true, false, true, true, true, true, true, false, true, true, true},
                {true, false, false, true, false, false, true, false, true, true, false, false, true, true, true},
                {true, false, true, true, true, true, true, true, true, true, true, true, true, false, true},
                {true, false, true, false, false, true, false, true, true, false, false, false, true, false, true},
                {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}
            };
        walls = defaultWalls;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int row = 0; row < MAP_SIZE; row++) {
        for (int col = 0; col < MAP_SIZE; col++) {
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;
                // Jika posisi adalah jalan, isi dengan warna putih
                if (walls[row][col]) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    // Jika tembok, isi dengan warna hitam
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
                // Menggambar droid merah
                    g.setColor(Color.RED);
                        for (int i = 0; i < redDroidCount; i++) {
                        int offsetX = (CELL_SIZE / 2) * i;
                    g.fillOval((redDroidX + i) * CELL_SIZE + offsetX, redDroidY * CELL_SIZE, CELL_SIZE / 2, CELL_SIZE / 2);
            }
                // Menggambar droid hijau
                    g.setColor(Color.GREEN);
                    g.fillOval(greenDroidX * CELL_SIZE, greenDroidY * CELL_SIZE, CELL_SIZE / 2, CELL_SIZE / 2);
            }

    private void startDroidMovement() {
        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMoving) {
                    moveRedDroid();
                    moveGreenDroid();
                    checkGameOver();
                    repaint();
                }
            }
        });
        timer.start();
    }

  private void moveRedDroid() {
    // Memeriksa jarak horizontal dan vertikal antara droid merah dan droid hijau
    int dx = greenDroidX - redDroidX;
    int dy = greenDroidY - redDroidY;
    // Menentukan arah pergerakan droid merah berdasarkan jarak horizontal dan vertikal
    if (Math.abs(dx) > Math.abs(dy)) {
        if (dx > 0) {
            // Droid hijau berada di sebelah kanan droid merah
        if (redDroidX + 1 < MAP_SIZE && walls[redDroidY][redDroidX + 1]) {
            redDroidX++;
            }
        } else if (dx < 0) {
            // Droid hijau berada di sebelah kiri droid merah
        if (redDroidX - 1 >= 0 && walls[redDroidY][redDroidX - 1]) {
            redDroidX--;
            }
        }
        
        } else {
        if (dy > 0) {
            // Droid hijau berada di bawah droid merah
            if (redDroidY + 1 < MAP_SIZE && walls[redDroidY + 1][redDroidX]) {
                redDroidY++;
            }
        } else if (dy < 0) {
            // Droid hijau berada di atas droid merah
            if (redDroidY - 1 >= 0 && walls[redDroidY - 1][redDroidX]) {
                redDroidY--;
            }
        }
    }

    // Mencegah droid merah melewati tembok hitam
    if (!walls[redDroidY][redDroidX]) {
        // Jika droid merah berada pada tembok hitam, kembalikan posisi sebelumnya
        redDroidX = previousRedDroidX;
        redDroidY = previousRedDroidX;
    } else {
        // Jika droid merah bergerak ke posisi valid, simpan posisi sebelumnya
        previousRedDroidX = redDroidX;
        previousRedDroidY = redDroidY;
    }
}
      private void moveGreenDroid() {
        // Implementasi logika pergerakan droid hijau
        // Menggunakan logika acak untuk mengubah arah pergerakan droid hijau
        // Menggeser posisi droid hijau sesuai arah yang ditentukan secara acak
        Random random = new Random();
        int direction = random.nextInt(4);
        int dx = 0;
        int dy = 0;
        
        // Mengatur jarak pandang berdasarkan nilai greenDroidSight
        switch (direction) {
            case 0: // Up
                for (int i = 1; i <= greenDroidSight; i++) {
                    if (greenDroidY - i >= 0 && walls[greenDroidY - i][greenDroidX]) {
                        dy = -i;
                    }
                }
                break;
            case 1: // Down
                for (int i = 1; i <= greenDroidSight; i++) {
                    if (greenDroidY + i < MAP_SIZE && walls[greenDroidY + i][greenDroidX]) {
                        dy = i;
                    }
                }
                break;
            case 2: // Left
                for (int i = 1; i <= greenDroidSight; i++) {
                    if (greenDroidX - i >= 0 && walls[greenDroidY][greenDroidX - i]) {
                        dx = -i;
                    }
                }
                break;
            case 3: // Right
                for (int i = 1; i <= greenDroidSight; i++) {
                    if (greenDroidX + i < MAP_SIZE && walls[greenDroidY][greenDroidX + i]) {
                        dx = i;
                    }
                }
                break;
        }
        greenDroidX += dx;
        greenDroidY += dy;
        }
        private void checkGameOver() {
        if (redDroidX == greenDroidX && redDroidY == greenDroidY) {
            isMoving = false;
            stopDroidMovement();
            JOptionPane.showMessageDialog(this, "Game Over");
        }
    }
    private void stopDroidMovement() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
    private void acakPeta() {
        // Implementasi logika pengacakan peta
        // Mengacak posisi tembok dengan acak boolean[][] walls
        Random random = new Random();
        boolean[][] newWalls = new boolean[MAP_SIZE][MAP_SIZE];
        for (int row = 0; row < MAP_SIZE; row++) {
            for (int col = 0; col < MAP_SIZE; col++) {
                newWalls[row][col] = random.nextBoolean();
            }
        }
        walls = newWalls;
    }

    private void shuffleRedDroid() {
        // Mengacak posisi droid merah dengan mengacak redDroidX dan redDroidY
        Random random = new Random();
        redDroidX = random.nextInt(5);
        redDroidY = random.nextInt(5);
    }
    private void shuffleGreenDroid() {
        // Mengacak posisi droid hijau dengan mengacak greenDroidX dan greenDroidY
        Random random = new Random();
        greenDroidX = random.nextInt(5);
        greenDroidY = random.nextInt(5);
    }
     private void addRedDroid() {     
    }
     
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MapPeta mapPeta = new MapPeta();
                mapPeta.setVisible(true);
            }
        });
    }
}