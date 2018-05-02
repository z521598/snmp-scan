package com.dcc.listener;

import com.dcc.util.SnmpData;
import com.dcc.view.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ScanButtonListener implements ActionListener {
    private String ip;
    private final String community = "public";


    public void actionPerformed(ActionEvent e) {
        ip = MainFrame.ipField.getText().trim();
        String oidval = "1.3.6.1.2.1.1.6.0";
        SnmpData.snmpGet(ip, community, oidval);
        List<String> oidList = new ArrayList<String>();
        oidList.add("1.3.6.1.2.1.1.5.0");
        oidList.add("1.3.6.1.2.1.1.7.0");
        SnmpData.snmpGetList(ip, community, oidList);

        List<String> oidList2 = new ArrayList<String>();
        oidList2.add("1.3.6.1.2.1");
        oidList2.add("1.3.6.1.2.12");
        SnmpData.snmpAsynGetList(ip, community, oidList2);
        SnmpData.snmpWalk(ip, community, "1.3.6.1.2.1.1.5.0");
        SnmpData.snmpAsynWalk(ip, community, "1.3.6.1.2.1.25.4.2.1.2");

    }
}
