/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package winrarbatch;
import java.io.*;

/**
 *
 * @author Mat
 */
public class BatchCommands {
    
    public static void createBatchFile(String batchLoc, String winRarLoc, String[] files){
        //"C:\Program Files (x86)\WinRAR\rar.exe" a -r -ep1 -md4096 "D:\ogg\trance\music.rar" "D:\ogg\trance\"
        File winrarBatchFile = new File(batchLoc);
        PrintWriter out;
        String line;

        try{
            
            out = new PrintWriter(new BufferedWriter(new FileWriter(winrarBatchFile)));
            for (String f : files){
                line = "\"" + winRarLoc + "\" a -r -ep1 -md4096 \"" + f + ".rar\" \"" + f + "\"";
                System.out.println(line);
                out.println(line);
            }
            out.close();
            
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
    public static int executeBatch(String winRarLoc, String[] files){
        String line;
        Process proc;
        
        try {
        
            for (String f : files){
                line = "\"" + winRarLoc + "\" a -r -ep1 -md4096 \"" + f + ".rar\" \"" + f + "\"";
                System.out.println(line);
                proc = Runtime.getRuntime().exec(line);
                //int exitVal = proc.exitValue();
                //System.out.println("Process exitValue: " + exitVal);
                
                // any error message?
                StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");            

                // any output?
                StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

                // kick them off
                errorGobbler.start();
                outputGobbler.start();

                // any error???
                int exitVal = proc.waitFor();
                System.out.println("ExitValue: " + exitVal);               
            }
            
        } catch (IOException ioe) {
            System.out.println(ioe);
            return 1;
        } catch (Throwable t) {
            t.printStackTrace();
            return 1;
        }
        
        return 0;
    }
}

class StreamGobbler extends Thread {
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }
    
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null) {
                //System.out.println(is.read());
                System.out.println(type + ">" + line);
            }
                    
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }
}
