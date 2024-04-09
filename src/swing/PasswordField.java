package swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class PasswordField extends JPasswordField {
    ImageIcon caps = new ImageIcon("src\\com\\QLTV\\icon\\letterw.png");
    Image capslocks = caps.getImage().getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
    ImageIcon capsLockIcon = new ImageIcon(capslocks);
    private String hint = "";
    private final Animator animator;
    private float animate;
    private boolean show = true;
    private boolean showe = true;
    private JButton toggleButton;
    public PasswordField() {
        setOpaque(false);
        setBorder(new EmptyBorder(9, 1, 9, 1));
        setBackground(new Color(0, 0, 0, 0));
        setForeground(new Color(255, 255, 255));
        setSelectionColor(new Color(200, 200, 200, 100));
        //setEchoChar('\u2022');
        
        animator = new Animator(350, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (show) {
                    animate = fraction;
                } else {
                    animate = 1f - fraction;
                }
                repaint();
            }

            @Override
            public void end() {
                show = !show;
                repaint();
            }
        });
        animator.setResolution(0);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (getPassword().length != 0) {
                    if (show) {
                        if (animator.isRunning() == false) {
                            stop();
                            animator.start();
                        }
                    } else if (animator.isRunning()) {
                        stop();
                        animator.start();
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (getText().equals("")) {
                    stop();
                    animator.start();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
//        // Tạo nút toggle và đặt vị trí
//        String eyehiden = "src\\com\\QLTV\\icon\\hiddenw.png";
//        String eyeshoww = "src\\com\\QLTV\\icon\\vieww.png";
//        ImageIcon hideIcon = new ImageIcon(eyehiden);
//        ImageIcon showIcon = new ImageIcon(eyeshoww);
//        Image eyehide = hideIcon.getImage().getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
//        Image eyeshow = showIcon.getImage().getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
//        ImageIcon hide = new ImageIcon(eyehide);
//        ImageIcon Eshow = new ImageIcon(eyeshow);
//        toggleButton = new JButton();
//        toggleButton.setIcon(hide);
//        toggleButton.setBorderPainted(false);
//        toggleButton.setContentAreaFilled(false);
//        toggleButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (showe) {
//                    setEchoChar((char) 0); // Hiện mật khẩu
//                    toggleButton.setIcon(Eshow);
//                    //toggleButton.setText("Ẩn mật khẩu");
//                } else {
//                    setEchoChar('\u2022'); // Ẩn mật khẩu
//                    toggleButton.setIcon(hide);
//                    //toggleButton.setText("Hiện mật khẩu");
//                }
//                showe = !showe;
//            }
//        });
//        setLayout(null); // Sử dụng layout tùy chỉnh
//        add(toggleButton);
    }

    private void stop() {
        if (animator.isRunning()) {
            float f = animator.getTimingFraction();
            animator.stop();
            animator.setStartFraction(1f - f);
        } else {
            animator.setStartFraction(0f);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Vẽ trường nhập mật khẩu

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawLine(0, getHeight() - 3, getWidth(), getHeight() - 3);
        if (!hint.equals("")) {
            int h = getHeight();
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            g2.setColor(new Color(232, 232, 232));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - animate));
            g2.drawString(hint, ins.left + (animate * 30), h / 2 + fm.getAscent() / 2 - 1);
        }
        // Đặt vị trí và kích thước của toggleButton
        //updateToggleButtonPosition();
        g2.dispose();
//            if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
//                        // Lấy kích thước của icon Caps Lock
//            int iconWidth = 60;
//            int iconHeight = 25;
//
//            // Tính toán vị trí để vẽ icon Caps Lock
//            int xCapsLock = getWidth() - iconWidth;
//            int yCapsLock = (getHeight() - iconHeight) / 2;
//
//            // Vẽ icon Caps Lock
//            capsLockIcon.paintIcon(this, g, xCapsLock, yCapsLock);
//    }
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
        repaint();
    }

//    private void updateToggleButtonPosition() {
//        int buttonWidth = 60; // Độ rộng của nút toggleButton
//        int buttonHeight = 20; // Độ cao của nút toggleButton
//        int padding = 5; // Khoảng cách giữa nút toggleButton và cạnh phải của PasswordField
//
//        int x = getWidth() - buttonWidth - padding; // Tính toán vị trí x để đặt nút toggleButton
//        int y = (getHeight() - buttonHeight) / 2; // Tính toán vị trí y để đặt nút toggleButton
//
//        toggleButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                setCursor(Cursor.getDefaultCursor());
//            }
//        });
//
//        toggleButton.setBounds(x, y, buttonWidth, buttonHeight); // Đặt vị trí và kích thước của nút toggleButton
//    }
}
