import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.PortUnreachableException;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;



public class Client extends JFrame implements Runnable
{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private JTextField textField = new JTextField(50);
    private JTextArea messageArea = new JTextArea(16, 50);
    private String IP = "";
    private int PORT = 9999;

    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private FileHandler fh;


    public Client(){

        try {
            fh = new FileHandler("client.log", false);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info("Client started");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        textField.setEditable(true);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        add(textField, BorderLayout.SOUTH);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);
        pack();
        IP = JOptionPane.showInputDialog("Enter the IP address:");

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                out.println(message);
                textField.setText("");
                if (message.equals("bye"))
                    System.exit(0);
            }
        });

        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(600, 400);

    }

    public void run() {
        try{
            client = new Socket(IP,PORT);
            out = new PrintWriter(client.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String inMessage;
            while ((inMessage = in.readLine()) != null){
                printMessage(inMessage);
            }

        }catch (IOException e){
            logger.severe("Error in client: " + e.getMessage());
            shutdown();
        }
    }

    public void printMessage(String line)
    {
        messageArea.append(line + "\n");
        logger.info(line);
    }

    public void shutdown(){
        try {
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Client client = new Client();
        client.run();
    }
}