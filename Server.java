import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

class Server extends JFrame{
    ServerSocket server;
    Socket socket1;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    public Server()
    {
        try {
            server=new ServerSocket(7777);
            System.out.println("server is ready to accept request");
            System.out.println("waiting...");
            socket1=server.accept();
            br=new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            out=new PrintWriter(socket1.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();
            
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        

    }
    private void handleEvents() 
    {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("key released " + e.getKeyCode());
                if(e.getKeyCode() == 10)
                {
                    //System.out.println("You have pressed ENTER button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });
    }

    private void createGUI()
    {
        this.setTitle("Server Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("clogo.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment((SwingConstants.CENTER));

        this.setLayout(new BorderLayout());

        //adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);

        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        
        
        this.setVisible(true);

    }
    public void startReading() {
        Runnable r1=()->{
            System.out.println("reader started");
            try{
                while(true)
                {
                        String msg=br.readLine();
                        if(msg.equals("exit"))
                        {
                            System.out.println("client terminated chat");
                            JOptionPane.showMessageDialog(this,"Client terminated chat");
                            messageInput.setEnabled(false);
                            socket1.close(); 
                            break;
                        }
                       // System.out.println("client: "+msg);
                        messageArea.append("Client: "+msg + "\n");

                    }        
                    } catch (Exception e) {
                        //TODO: handle exception
                       System.out.println("connection closed");
                    }
                   
                
        };
        new Thread(r1).start();
        
    }
    public void startWriting() {
        Runnable r2=()->{
            System.out.println("writer  started");
            try{
                while(!socket1.isClosed())
                {
                       BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                       String content=br1.readLine();
                       out.println(content);
                       out.flush();
                       if(content.equals("exit"))
                       {
                           socket1.close();
                           break;
                       }
                 
                        
                    }
                 }
             catch (Exception e) {
                        //TODO: handle exception
                        System.out.println("connection closed");
                    }
        };
        new Thread(r2).start();

    }
    public static void main(String[] args) {
        System.out.println("this is server..going to be ready");
        new Server();
    }
}
