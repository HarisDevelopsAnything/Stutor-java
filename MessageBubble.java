package stutor;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageBubble extends JPanel {

    private boolean isSender;
    static ServerConnector userdataConn;

    public MessageBubble(String senderID, String receiverID, String messageBody, String timestamp, String currentUserID) {
    	userdataConn= new ServerConnector("user_data");
        this.isSender = senderID.equals(currentUserID);

        // Set background color based on sender
        setBackground(isSender ? new Color(144, 238, 144) : new Color(211, 211, 211)); // Green for sender, grey for receiver
        setOpaque(true);

        // Create labels for the message and timestamp
        JLabel messageLabel = new JLabel("<html><h3>" + messageBody + "</h3></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.BLACK);
        
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String formattedDate = sdf.format(new Date());
        JLabel timestampLabel = new JLabel(timestamp);
        timestampLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        timestampLabel.setForeground(Color.DARK_GRAY);

        // Combine message and timestamp in a bubble
        JPanel bubbleContent = new JPanel();
        bubbleContent.setLayout(new BoxLayout(bubbleContent, BoxLayout.Y_AXIS));
        bubbleContent.setBackground(getBackground());
        String rec="";
        try {
        	ResultSet rs= userdataConn.executeQuery("SELECT name from common where uid="+receiverID);
        	if(rs.next()) {
        		rec= rs.getString("name");
        	}
        	else {
        		rec= "Unknown";
        	}
        }
        catch(Exception e) {
        	System.out.println(e.getMessage());
        }
        bubbleContent.add(isSender?new JLabel("You"):new JLabel(rec));
        bubbleContent.add(new JLabel("<html><p> to "+(isSender?rec:"you")+"</p></html>"));
        bubbleContent.add(messageLabel);
        bubbleContent.add(timestampLabel);
        bubbleContent.add(Box.createVerticalStrut(5)); // Space between message and timestamp
        

        // Set fixed size for bubbles
        int bubbleHeight = 60; // Fixed height
        int maxBubbleWidth = 250; // Limit the width
        //bubbleContent.setPreferredSize(new Dimension(maxBubbleWidth, bubbleHeight));
        //bubbleContent.setMinimumSize(new Dimension(maxBubbleWidth, bubbleHeight));
        //bubbleContent.setMaximumSize(new Dimension(maxBubbleWidth, bubbleHeight));

        if (isSender) {
            setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align right for sender
        } else {
            setLayout(new FlowLayout(FlowLayout.LEFT)); // Align left for receiver
        }
        add(bubbleContent);
    }

    // Factory method to create a bubble
    public static JPanel createMessageBubble(String senderID, String receiverID, String messageBody, String timestamp, String currentUserID) {
        return new MessageBubble(senderID, receiverID, messageBody, timestamp, currentUserID);
    }
}
