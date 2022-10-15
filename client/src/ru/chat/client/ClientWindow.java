package ru.chat.client;

import ru.chat.network.TCPCConnection;
import ru.chat.network.TCPCConnectionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow  extends JFrame implements ActionListener, TCPCConnectionListener {

    private static final String IP_ADRESS = "127.0.0.1";
    private static final int IP_PORT = 8189;
    private static final int W = 600;
    private static final int H = 400;
    private final JTextArea log = new JTextArea();
    private final JTextField name = new JTextField("Name");
    private final JTextField input = new JTextField("Message...");

    private TCPCConnection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private ClientWindow()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(W,H);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        log.setEditable(false);
        log.setLineWrap(true);
        log.setBackground(new Color(55,51,81));
        input.setBackground(new Color(55,51,81));
        input.setForeground(new Color(138,127,147));//138,127,147
        name.setForeground(Color.white);
        name.setBorder(new LineBorder(new Color(74,71,98),1));//67,64,92
        input.setBorder(new LineBorder(new Color(74,71,98),1));//67,64,92
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setBackground(new Color(55,51,81));
        log.setForeground(Color.white);
        name.setFont(new Font("Courier", Font.BOLD,22));
        log.setFont(new Font("Courier", Font.PLAIN,18));
        input.setFont(new Font("Courier", Font.PLAIN,25));
        input.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        add(name, BorderLayout.NORTH);

        try {
            connection = new TCPCConnection(this,IP_ADRESS,IP_PORT);
        } catch (IOException e) {
            printsms("Connetction exeption" + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();

        if(msg.equals(""))
        {
            return;
        }
        input.setText(null);
        connection.sendMessage(name.getText() + " : " + msg);
    }

    @Override
    public void onConnectionReady(TCPCConnection tcpcConnection) {

        printsms("Connetction ready!");
    }

    @Override
    public void onReceiveString(TCPCConnection tcpcConnection, String value) {

        printsms(value);
    }

    @Override
    public void onDisconnect(TCPCConnection tcpcConnection) {

        printsms("Connetction close");
    }

    @Override
    public void onExeption(TCPCConnection tcpcConnection, Exception exception) {

        printsms("Connetction exeption" + exception);
    }

    private synchronized void printsms(String msg)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
