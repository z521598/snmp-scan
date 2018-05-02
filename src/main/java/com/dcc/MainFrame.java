package com.dcc;


import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2018/4/23.
 */
public class MainFrame extends JFrame {

    private JPanel commandPanel = new JPanel();

    private JPanel consolePanel = new JPanel();
    public static JTextArea consoleTextArea = new JTextArea(25, 39);

    private JPanel exitPanel = new JPanel();
    private JButton exitButton = new JButton("退出");
    public static MainFrame mainFrame;

    public MainFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setName("telnet管理中心");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // 命令区域
        commandPanel.setLayout(new GridLayout(3, 3));


        add(commandPanel, BorderLayout.NORTH);
        // 控制台区域
        consolePanel.add(new JScrollPane(consoleTextArea), JFrame.CENTER_ALIGNMENT);
        add(consolePanel, BorderLayout.CENTER);

        exitPanel.add(exitButton);
        add(exitPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame = new MainFrame();
            }
        });
    }

}