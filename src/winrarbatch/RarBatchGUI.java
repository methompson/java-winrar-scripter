/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winrarbatch;

import java.awt.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
/**
 *
 * @author Mathew E. Thompson
 */

interface Command {
    void execute();
}

public class RarBatchGUI {
    
}

class Mediator {
    
    Logger logger = new Logger();
    ClearFilesBtn clearFilesBtn;
    
    void showLog() {
        logger.setVisible(true);
    }
}

class Logger extends JFrame{
    
    Mediator med;
    JTextArea log = new JTextArea(7, 20);
    JPanel panel = new JPanel();
    
    Logger() {
        panel.add(log);
        this.add(panel);
        this.pack();
    }
    
    void addLog(String entry) {
        log.append(entry + "\n");
    }
}

class ClearFilesBtn extends JButton implements Command {
    
    Mediator med;
    
    ClearFilesBtn(ActionListener al, Mediator m) {
        super("Clear Files");
        addActionListener(al);
        med = m;
        med.registerClear(this);
    }
    
    public void execute() {
        med.clearFiles();
    }
}

class BrowseWinRarBtn extends JButton implements Command {
    
    Mediator med;
    
    BrowseWinRarBtn(ActionListener al, Mediator m) {
        super("Browse");
        addActionListener(al);
        med = m;
        med.registerBrowseWinRar(this);
    }
    
    public void execute() {
        med.browseWinRar();
    }
}

class SaveBatchBtn extends JButton implements Command {
    
    Mediator med;
    
    SaveBatchBtn(ActionListener al, Mediator m) {
        super("Execute");
        addActionListener(al);
        med = m;
        med.registerSave(this);
    }
    
    public void execute() {
        med.saveBatch();
    }
}

class ExecuteBtn extends JButton implements Command {
    
    Mediator med;
    
    ExecuteBtn(ActionListener al, Mediator m) {
        super("Execute");
        addActionListener(al);
        med = m;
        med.registerExecute(this);
    }
    
    public void execute() {
        med.execute();
    }
}