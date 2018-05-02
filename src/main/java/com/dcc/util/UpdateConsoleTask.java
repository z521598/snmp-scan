package com.dcc.util;

import com.dcc.view.MainFrame;

public class UpdateConsoleTask implements Runnable {

    private String text;

    public UpdateConsoleTask(String text) {
        this.text = text;
    }

    public void run() {
        String oldText = MainFrame.consoleTextArea.getText();
        MainFrame.consoleTextArea.setText(oldText + text + "\n");
    }

}
