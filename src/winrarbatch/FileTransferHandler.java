/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package winrarbatch;

/**
 *
 * @author Mat
 */

import java.util.List;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.io.*;

public class FileTransferHandler extends TransferHandler {
    public boolean canImport(TransferHandler.TransferSupport info){
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
            return false;
        }
        
        return true;
    }
    
    public boolean importData(TransferHandler.TransferSupport data) {
        Transferable t = data.getTransferable();

        try {
            
            JList list = (JList)data.getComponent();
            DefaultListModel listModel = (DefaultListModel)list.getModel();
            
            List<File> l = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
            for (File f : l) {
                if (!listModel.contains(f.getAbsolutePath())){
                    listModel.addElement(f.getAbsolutePath());
                }
            }
            
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }
        
        
        
    
}
