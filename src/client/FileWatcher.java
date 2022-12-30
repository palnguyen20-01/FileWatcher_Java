package client;

/*reference: https://dzone.com/articles/listening-to-fileevents-with-java-nio*/

import java.io.IOException;
import java.nio.*;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher extends Thread{
   public String filePath;
    public FileWatcher(String name){    
        filePath=name;
}
    
    @Override
	public void run() {
        try{
            WatchService watchService = FileSystems.getDefault().newWatchService();
Path path = Paths.get(filePath);
path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
boolean poll = true;
while (poll) {
  WatchKey key = watchService.take();
  for (WatchEvent<?> event : key.pollEvents()) {
      Main.connection.sender.write("event found");
        Main.connection.sender.newLine();
                    Main.connection.sender.flush();
                    
//    System.out.println("Event kind : " + event.kind()+ " - File : " + event.context());
    Main.connection.sender.write(event.context().toString());
        Main.connection.sender.newLine();
                    Main.connection.sender.flush();

            Main.connection.sender.write(event.kind().toString());
        Main.connection.sender.newLine();
            Main.connection.sender.flush();


  }
  poll = key.reset();
}
        }catch(Exception ex){
            ex.printStackTrace();
        }
        }
}