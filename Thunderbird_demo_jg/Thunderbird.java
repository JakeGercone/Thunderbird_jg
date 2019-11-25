/******************************************************************************
 * Copyright (C) 2019 Eric Pogue.
 * 
 * This file and the ThunderbirdLite application is licensed under the 
 * BSD-3-Clause.
 * 
 * You may use any part of the file as long as you give credit in your 
 * source code.
 * 
 * This application utilizes the HttpRequest.java library developed by 
 * Eric Pogue
 * 
 *****************************************************************************/
 
import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.event.MouseInputListener;
import javax.swing.JButton;

import java.awt.Container; 
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;

class ContactTile extends JPanel implements MouseListener {
    private int red, green, blue;
    private ThunderbirdContact contactInSeat = null;

    private Boolean isAnAisle = true;
    public void setAisle() { isAnAisle = false; }
    private Boolean isPouge = false;
    public void setPouge() { isPouge = true; }

    ContactTile() {
        super();
        // JG Removed everything to do with random colors.
        // JG Implemented visually appealing colors for aisles and seats.
    }



    ContactTile(final ThunderbirdContact contactInSeatIn) {
        super();
        addMouseListener(this);
        red = 255;
        blue = 0;
        green = 0;
        contactInSeat = contactInSeatIn;
    }

    public static void infoBox(final String infoMessage, final String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        
    }

    

   
     public void paintComponent(final Graphics g) {
        super.paintComponent(g); 

        final int panelWidth = getWidth();
        final int panelHeight = getHeight();

        if (isPouge){g.setColor(new Color(255,255,0));}
        else if (isAnAisle) {
            g.setColor(new Color(0,0,0));
        } 
         else {
            g.setColor(new Color(red,green,blue));
        }
        
        g.fillRect (1, 1, panelWidth, panelHeight);

        g.setColor(new Color(GetContrastingColor(red),GetContrastingColor(green),GetContrastingColor(blue)));

        final int fontSize=18;
        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        final int stringX = (panelWidth/2)-30;
        final int stringY = (panelHeight/6)+30;
        if (contactInSeat != null) {

            // JG Dispaying preferred name instead of first and last name. 
            final String Name = contactInSeat.getpreferredName();
            g.setColor(new Color(0,255,0));
            g.drawString(Name,stringX,stringY);
            
        }
    }

    private static int GetContrastingColor(final int colorIn) {
        return ((colorIn+128)%256);
    }
    public void mousePressed(final MouseEvent e) {
    }
    public void mouseReleased(final MouseEvent e) {
    }
    public void mouseClicked(final MouseEvent e) { 
        try{
        String contactDetails = contactInSeat.getFirstName() + " " + contactInSeat.getLastName() + "\r\n"+ contactInSeat.getemail();
        infoBox(contactDetails, "Contact Details");
        } catch (Exception exc){}
    }
    public void mouseEntered(final MouseEvent e) {
    }
    public void mouseExited(final MouseEvent e) {
    }

}

class ThunderbirdFrame extends JFrame implements ActionListener {
    private ArrayList<ContactTile> tileList;
    private boolean isReverse;
    private final JPanel contactGridPanel;
    private final ThunderbirdModel tbM ;
    
    public ThunderbirdFrame() {
        isReverse = false;
        setBounds(200,200,1200,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        final JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        final JButton reverseView = new JButton("Reverse View");
        buttonPanel.add(reverseView);
        reverseView.addActionListener(this);

        contactGridPanel = new JPanel();
        contentPane.add(contactGridPanel, BorderLayout.CENTER);

        tbM = new ThunderbirdModel();
        tbM.LoadIndex();
        tbM.LoadContactsThreaded();

        // JG Reviewed ThunderbirdModel in detail and implemented a multithreaded version of loading contacts. 

        System.out.println("Printing Model:");
        tbM.ValidateContacts();   


        addContactsToGrid();
    }

    public void addContactsToGrid(){
        int i;
        contactGridPanel.removeAll();
        contactGridPanel.setLayout(new GridLayout(11,9));
        tileList = new ArrayList<ContactTile>();

        for(int oni=1; oni<100; oni++) {
            //reverse view
            if (isReverse)
                i = 100-oni;
            else
                i = oni;
            final ThunderbirdContact contactInSeat = tbM.findContactInSeat(i);
            if (contactInSeat != null) {
            }

            final ContactTile tile = new ContactTile(contactInSeat);

            if ((i==13)||(i==31)||(i==32)||(i==14)||(i==20)||(i==29)||(i==38) ||(i==47)||(i==56) ||(i==65)||(i==68) ||(i==67)||(i==85)
            ||(i==86)||(i==94)||(i==95)||(i==61)||(i==49)||(i==16) ||(i==17)||(i==25)||(i==26)||(i==34)||(i==35)||(i==50) ||(i==53)||(i==62)
            ||(i==61)||(i==71)||(i==70)||(i==50)||(i==52)) {
                tile.setAisle();
            }
            else if ((i==11)){
                tile.setPouge();
    
            }

            tileList.add(tile);
            contactGridPanel.add(tile);
        }
        contactGridPanel.revalidate();
        contactGridPanel.repaint();
 
    }
 
    public void actionPerformed(final ActionEvent e) {
        isReverse = !isReverse;
        addContactsToGrid();
        repaint();
    }
}

// JG Renamed the following class to Thunderbird.
public class Thunderbird {
    public static void main(final String[] args) {

        // JG Updated the following line so that it reflects the name change to Thunderbird.
        System.out.println("Thunderbird Starting...");
       

        final ThunderbirdFrame myThunderbirdFrame = new ThunderbirdFrame();
        myThunderbirdFrame.setVisible(true);
    }
}