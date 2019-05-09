package winrarbatch;

import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mat
 */
public class BatchGUI {
    
    public static void main(String[] args) {
        createAndShowGUI();
    }
    
    private static void createAndShowGUI(){
        JFrame frame = new JFrame("WinRAR Batch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        //Adding a listmodel to the code is necessary to allow for extraction
        //of the data later on
        //DefaultListModel listModel = new DefaultListModel();
        //JList list = new JList(listModel);
        //list.setTransferHandler(new FileTransferHandler());
        
        //JPanel panel = new JPanel();
        //panel.setLayout(new BorderLayout());
        //panel.add(new JScrollPane(list), BorderLayout.NORTH);
        //panel.add(new JScrollPane(list));
        
        /*JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new BatchPanel(), BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.SOUTH);*/
        
        //frame.add(panel);
        frame.add(new BatchPanel());
        //frame.add(mainPanel);
        frame.pack();
        
        frame.setResizable(true);
        frame.setVisible(true);
    }
    
}

class BatchPanel extends JPanel implements ActionListener {
    
    private JList filesToRar;
    private JButton clearFilesButton, browseWinRarButton, batchButton, executeButton;
    private JTextField winRarLoc;
    private JLabel filesLabel, winRarLocLabel;
    
    public BatchPanel(){
        this.setLayout(new GridBagLayout());
        
        filesLabel = new JLabel("Files to Compress");
        this.add(filesLabel, getConstraints(0,0,1,1,GridBagConstraints.EAST));
        
        clearFilesButton = new JButton("Clear Files");
        clearFilesButton.addActionListener(this);
        this.add(clearFilesButton, getConstraints(0,1,1,1,GridBagConstraints.EAST));
        
        //Adding a listmodel to the code is necessary to allow for extraction
        //of the data later on
        DefaultListModel listModel = new DefaultListModel();
        
        //Adds the FileTransferHandler for drag and drop functionality
        filesToRar = new JList(listModel);
        filesToRar.setDragEnabled(true);
        filesToRar.setTransferHandler(new FileTransferHandler());

        this.add(new JScrollPane(filesToRar, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), getConstraints(1,0,2,3,GridBagConstraints.WEST));
        
        winRarLocLabel = new JLabel("WinRAR Location");
        this.add(winRarLocLabel, getConstraints(0,4,1,1,GridBagConstraints.EAST));
        
        winRarLoc = new JTextField(24);
        winRarLoc.setEditable(false);
        winRarLoc.setText("");
        this.add(winRarLoc, getConstraints(1,4,2,1,GridBagConstraints.WEST));
        
        browseWinRarButton = new JButton("Browse");
        browseWinRarButton.addActionListener(this);
        this.add(browseWinRarButton, getConstraints(1,5,1,1,GridBagConstraints.WEST));
        
        batchButton = new JButton("Save As Batch File");
        batchButton.addActionListener(this);
        this.add(batchButton, getConstraints(1,6,1,1,GridBagConstraints.WEST));
        
        executeButton = new JButton("Execute Batch");
        executeButton.addActionListener(this);
        this.add(executeButton, getConstraints(2,6,1,1,GridBagConstraints.WEST));
        
    }
    
    private GridBagConstraints getConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.ipadx = 0;
        c.ipady = 0;
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.anchor = anchor;
        return c;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == clearFilesButton){
            
            //Extracts the data from the JList and then clears the data
            DefaultListModel listModel = (DefaultListModel)filesToRar.getModel();
            listModel.clear();
            
        } else if (source == browseWinRarButton) {
            
            //This section checks several folders to see if WinRAR exists in
            //common Windows folders.
            JFileChooser chooser;
            if (new File("C:\\Program Files (x86)\\winrar").exists()) {
                chooser = new JFileChooser(new File("C:\\Program Files (x86)\\winrar"));
            } else if (new File("C:\\Program Files\\winrar").exists()) {
                chooser = new JFileChooser(new File("C:\\Program Files\\winrar"));
            } else {
                chooser = new JFileChooser(new File("C:\\"));
            }
            
            chooser.setAcceptAllFileFilterUsed(false);
            //Filters only applications with an exe extension
            FileFilter type1 = new FileNameExtensionFilter("Applications", "exe");
            chooser.addChoosableFileFilter(type1);
            

            //Allows only files to be selected
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            //Opens the file selection dialogue
            int returnVal = chooser.showOpenDialog(BatchPanel.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                //sets the text in the file location to the selected application
                File rarFile = chooser.getSelectedFile();
                winRarLoc.setText(rarFile.getAbsolutePath());
            }
            
        } else if (source == batchButton) {
            
            if (winRarLoc.getText().length() == 0){
                //Error dialogue if the user didn't select WinRAR's location
                JOptionPane.showMessageDialog(new JFrame(), "You Need to Select WinRAR's Application Location");
                return;
            }
            
            //File selector to choose where to save the batch file with filter
            //to only see batch files
            JFileChooser chooser = new JFileChooser(new File("C:\\"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter type1 = new FileNameExtensionFilter("Batch Files", "bat");
            chooser.addChoosableFileFilter(type1);
            int returnVal = chooser.showSaveDialog(BatchPanel.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File batchFile;
                System.out.println("before: " + chooser.getSelectedFile().getAbsolutePath());
                
                if (chooser.getSelectedFile().getName().length() < 4){
                    batchFile = new File(chooser.getSelectedFile().getAbsolutePath() + ".bat");
                } else if (chooser.getSelectedFile().getAbsolutePath().substring(chooser.getSelectedFile().getName().length()-4) != ".bat") {
                    batchFile = new File(chooser.getSelectedFile().getAbsolutePath() + ".bat");
                } else {
                    batchFile = chooser.getSelectedFile();
                }
                
                System.out.println("after: " + batchFile.getAbsolutePath());
                //Retrieves the data from the JList as an array of Objects
                //Uses a loop to transfer to the String representations of Object files
                //to another array
                DefaultListModel listModel = (DefaultListModel)filesToRar.getModel();
                Object[] files = listModel.toArray();
                String[] fileNames = new String[files.length];
                for (int x = 0; x < files.length; x++){
                    fileNames[x] = files[x].toString();
                    //System.out.println(fileNames[x]);
                }
                
                //Sends data to the Batching Class
                BatchCommands.createBatchFile(batchFile.toString(), winRarLoc.getText(), fileNames);
            }
            
        } else if (source == executeButton) {
            if (winRarLoc.getText().length() == 0){
                //Error dialogue if the user didn't select WinRAR's location
                JOptionPane.showMessageDialog(new JFrame(), "You Need to Select WinRAR's Application Location");
                return;
            }
                
            //Retrieves the data from the JList as an array of Objects
            //Uses a loop to transfer to the String representations of Object files
            //to another array
            DefaultListModel listModel = (DefaultListModel)filesToRar.getModel();
            Object[] files = listModel.toArray();
            String[] fileNames = new String[files.length];
            for (int x = 0; x < files.length; x++){
                fileNames[x] = files[x].toString();
                //System.out.println(fileNames[x]);
            }

            //Sends data to the Batching Class
            int exitVal = BatchCommands.executeBatch(winRarLoc.getText(), fileNames);
            /*if (exitVal == 0){
                JOptionPane.showMessageDialog(new JFrame(), "WinRAR Operation Complete");
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "There was an error during compression");
            }*/
        }
    }
}
