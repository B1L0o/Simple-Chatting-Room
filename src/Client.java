import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Client extends JFrame implements Runnable
{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private JTextField textField = new JTextField(50);
    private JTextArea messageArea = new JTextArea(16, 50);


    public Client(){
        textField.setEditable(true);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        add(textField, BorderLayout.SOUTH);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);
        pack();

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
            client = new Socket("127.0.0.1",9999);
            out = new PrintWriter(client.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String inMessage;
            while ((inMessage = in.readLine()) != null){
                printMessage(inMessage);
                System.out.println(inMessage);
            }

        }catch (IOException e){
            shutdown();
        }
    }

    public void printMessage(String line)
    {
        messageArea.append(line + "\n");
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