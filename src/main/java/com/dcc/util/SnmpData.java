package com.dcc.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.dcc.view.MainFrame;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import javax.swing.*;

public class SnmpData {

    public static final int DEFAULT_VERSION = SnmpConstants.version2c;
    public static final String DEFAULT_PROTOCOL = "udp";
    public static final int DEFAULT_PORT = 161;
    public static final long DEFAULT_TIMEOUT = 3 * 1000L;
    public static final int DEFAULT_RETRY = 3;

    /**
     * 创建对象communityTarget，用于返回target
     *
     * @param community
     * @return CommunityTarget
     */
    public static CommunityTarget createDefault(String ip, String community) {
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /*根据OID，获取单条消息*/
    public static void snmpGet(String ip, String community, String oid) {

        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            SwingUtilities.invokeLater(new UpdateConsoleTask("-------> 发送PDU <-------"));
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            SwingUtilities.invokeLater(new UpdateConsoleTask("PeerAddress:" + respEvent.getPeerAddress()));
            PDU response = respEvent.getResponse();

            if (response == null) {
                SwingUtilities.invokeLater(new UpdateConsoleTask("response is null, request time out"));
            } else {
                SwingUtilities.invokeLater(new UpdateConsoleTask("response pdu size is " + response.size()));
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    SwingUtilities.invokeLater(new UpdateConsoleTask(vb.getOid() + " = " + vb.getVariable()));
                }

            }
            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP GET one OID value finished !"));
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP Get Exception:" + e));
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }

        }
    }

    /*根据OID列表，一次获取多条OID数据，并且以List形式返回*/
    public static void snmpGetList(String ip, String community, List<String> oidList) {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();

            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            SwingUtilities.invokeLater(new UpdateConsoleTask("-------> 发送PDU <-------"));
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            SwingUtilities.invokeLater(new UpdateConsoleTask("PeerAddress:" + respEvent.getPeerAddress()));
            PDU response = respEvent.getResponse();

            if (response == null) {
                SwingUtilities.invokeLater(new UpdateConsoleTask("response is null, request time out"));
            } else {
                SwingUtilities.invokeLater(new UpdateConsoleTask("response pdu size is " + response.size()));
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    SwingUtilities.invokeLater(new UpdateConsoleTask(vb.getOid() + " = "
                            + vb.getVariable()));
                }

            }
            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP GET one OID value finished !"));
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP Get Exception:" + e));
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }

        }
    }

    /*根据OID列表，采用异步方式一次获取多条OID数据，并且以List形式返回*/
    public static void snmpAsynGetList(String ip, String community, List<String> oidList) {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();

            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            SwingUtilities.invokeLater(new UpdateConsoleTask("-------> 发送PDU <-------"));
            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);
            SwingUtilities.invokeLater(new UpdateConsoleTask("PeerAddress:" + respEvent.getPeerAddress()));
            PDU response = respEvent.getResponse();

            /*异步获取*/
            final CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    PDU response = event.getResponse();
                    PDU request = event.getRequest();
                    SwingUtilities.invokeLater(new UpdateConsoleTask("[request]:" + request));
                    if (response == null) {
                        SwingUtilities.invokeLater(new UpdateConsoleTask("response is null, request time out"));
                    } else if (response.getErrorStatus() != 0) {
                        SwingUtilities.invokeLater(new UpdateConsoleTask("[ERROR]: response status"
                                + response.getErrorStatus() + " Text:"
                                + response.getErrorStatusText()));
                    } else {
                        SwingUtilities.invokeLater(new UpdateConsoleTask("Received response Success!"));
                        for (int i = 0; i < response.size(); i++) {
                            VariableBinding vb = response.get(i);
                            SwingUtilities.invokeLater(new UpdateConsoleTask(vb.getOid() + " = "
                                    + vb.getVariable()));
                        }
                        SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP Asyn GetList OID finished. "));
                        latch.countDown();
                    }
                }
            };

            pdu.setType(PDU.GET);
            snmp.send(pdu, target, null, listener);
            SwingUtilities.invokeLater(new UpdateConsoleTask("asyn send pdu wait for response..."));

            boolean wait = latch.await(30, TimeUnit.SECONDS);
            SwingUtilities.invokeLater(new UpdateConsoleTask("latch.await =:" + wait));

            snmp.close();

            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP GET one OID value finished !"));
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP Get Exception:" + e));
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }

        }
    }

    /*根据targetOID，获取树形数据*/
    public static void snmpWalk(String ip, String community, String targetOid) {
        CommunityTarget target = createDefault(ip, community);
        TransportMapping transport = null;
        Snmp snmp = null;
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();

            PDU pdu = new PDU();
            OID targetOID = new OID(targetOid);
            pdu.add(new VariableBinding(targetOID));

            boolean finished = false;
            SwingUtilities.invokeLater(new UpdateConsoleTask("----> demo start <----"));
            while (!finished) {
                VariableBinding vb = null;
                ResponseEvent respEvent = snmp.getNext(pdu, target);

                PDU response = respEvent.getResponse();

                if (null == response) {
                    SwingUtilities.invokeLater(new UpdateConsoleTask("responsePDU == null"));
                    finished = true;
                    break;
                } else {
                    vb = response.get(0);
                }
                // check finish
                finished = checkWalkFinished(targetOID, pdu, vb);
                if (!finished) {
                    SwingUtilities.invokeLater(new UpdateConsoleTask("==== walk each vlaue :"));
                    SwingUtilities.invokeLater(new UpdateConsoleTask(vb.getOid() + " = "
                            + vb.getVariable()));

                    // Set up the variable binding for the next entry.
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                } else {
                    SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP walk OID has finished."));
                    snmp.close();
                }
            }
            SwingUtilities.invokeLater(new UpdateConsoleTask("----> demo end <----"));
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP walk Exception: " + e));
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
        }
    }

    private static boolean checkWalkFinished(OID targetOID, PDU pdu,
                                             VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            SwingUtilities.invokeLater(new UpdateConsoleTask("[true] responsePDU.getErrorStatus() != 0 "));
            SwingUtilities.invokeLater(new UpdateConsoleTask(pdu.getErrorStatusText()));
            finished = true;
        } else if (vb.getOid() == null) {
            SwingUtilities.invokeLater(new UpdateConsoleTask("[true] vb.getOid() == null"));
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            SwingUtilities.invokeLater(new UpdateConsoleTask("[true] vb.getOid().size() < targetOID.size()"));
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            SwingUtilities.invokeLater(new UpdateConsoleTask("[true] targetOID.leftMostCompare() != 0"));
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            SwingUtilities.invokeLater(new UpdateConsoleTask("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())"));
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            SwingUtilities.invokeLater(new UpdateConsoleTask("[true] Variable received is not "
                    + "lexicographic successor of requested " + "one:"));
            SwingUtilities.invokeLater(new UpdateConsoleTask(vb.toString() + " <= " + targetOID));
            finished = true;
        }
        return finished;

    }

    /*根据targetOID，异步获取树形数据*/
    public static void snmpAsynWalk(String ip, String community, String oid) {
        final CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            SwingUtilities.invokeLater(new UpdateConsoleTask("----> demo start <----"));

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            final PDU pdu = new PDU();
            final OID targetOID = new OID(oid);
            final CountDownLatch latch = new CountDownLatch(1);
            pdu.add(new VariableBinding(targetOID));

            ResponseListener listener = new ResponseListener() {
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);

                    try {
                        PDU response = event.getResponse();
                        if (response == null) {
                            SwingUtilities.invokeLater(new UpdateConsoleTask("response is null, request time out"));
                        } else if (response.getErrorStatus() != 0) {
                            SwingUtilities.invokeLater(new UpdateConsoleTask("[ERROR]: response status"
                                    + response.getErrorStatus() + " Text:"
                                    + response.getErrorStatusText()));
                        } else {
                            SwingUtilities.invokeLater(new UpdateConsoleTask("Received Walk response value :"));
                            VariableBinding vb = response.get(0);

                            boolean finished = checkWalkFinished(targetOID,
                                    pdu, vb);
                            if (!finished) {
                                SwingUtilities.invokeLater(new UpdateConsoleTask(vb.getOid() + " = "
                                        + vb.getVariable()));
                                pdu.setRequestID(new Integer32(0));
                                pdu.set(0, vb);
                                ((Snmp) event.getSource()).getNext(pdu, target,
                                        null, this);
                            } else {
                                SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP Asyn walk OID value success !"));
                                latch.countDown();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        latch.countDown();
                    }

                }
            };

            snmp.getNext(pdu, target, null, listener);
            SwingUtilities.invokeLater(new UpdateConsoleTask("pdu 已发送,等到异步处理结果..."));

            boolean wait = latch.await(30, TimeUnit.SECONDS);
            SwingUtilities.invokeLater(new UpdateConsoleTask("latch.await =:" + wait));
            snmp.close();
            SwingUtilities.invokeLater(new UpdateConsoleTask("----> demo end <----"));
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(new UpdateConsoleTask("SNMP Asyn Walk Exception:" + e));
        }
    }

    /*根据OID和指定string来设置设备的数据*/
    public static void setPDU(String ip, String community, String oid, String val) throws IOException {
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid), new OctetString(val)));
        pdu.setType(PDU.SET);

        DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        snmp.listen();
        SwingUtilities.invokeLater(new UpdateConsoleTask("-------> 发送PDU <-------"));
        snmp.send(pdu, target);
        snmp.close();
    }
}