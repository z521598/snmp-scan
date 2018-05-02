package com.dcc.view;


import com.dcc.listener.ScanButtonListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2018/4/23.
 */
public class MainFrame extends JFrame {

    private JLabel ipLabel = new JLabel("ip:");
    public static JTextField ipField = new JTextField("127.0.0.1",10);
    private JPanel ipPanel = new JPanel();

    private JButton startButton = new JButton("开始扫描");
    private JPanel commandPanel = new JPanel();

    private JPanel consolePanel = new JPanel();
    public static JTextArea consoleTextArea = new JTextArea(35, 60);

    public static MainFrame mainFrame;

    public MainFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setName("telnet管理中心");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // 命令区域
        ipPanel.add(ipLabel);
        ipPanel.add(ipField);
        commandPanel.add(ipPanel);
        startButton.addActionListener(new ScanButtonListener());
        commandPanel.add(startButton);
        add(commandPanel, BorderLayout.NORTH);
        // 控制台区域
        consolePanel.add(new JScrollPane(consoleTextArea), JFrame.CENTER_ALIGNMENT);
        add(consolePanel, BorderLayout.CENTER);

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