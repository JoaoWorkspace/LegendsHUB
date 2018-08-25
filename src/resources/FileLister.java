package resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class FileLister extends ArrayList<File>{

	private HashMap<File, byte[]> fileData = new HashMap<File, byte[]>();
	/**
	 * If the file is a directory, it recursively calls itself until every content of the directory is added to the list,
	 * and if one of the contents is a Folder, it also gets all of it's contents extracted.
	 * @param folder - The folder you wanna know the contents from.
	 * @return Returns a list of ALL the files inside the folder.
	 * @author João Mendonça
	 */
	public FileLister(File folder) {
		listDirectory(folder);
	}
	
	
	private void listDirectory(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(File currentFile : files) {
                listDirectory(currentFile);
            }
        }
        else {
//        	System.out.println(file);
            add(file);
        }
    }
	
	public HashMap<File, byte[]> getFileData() {
		for(File file : this) {
			try {
				byte[] thisFileData = Files.readAllBytes(file.toPath());
				fileData.put(file, thisFileData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileData;
	}
}
