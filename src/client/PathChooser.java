/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.io.*;
import java.util.*;

/**
 *
 * @author Anh Dat
 */
public class PathChooser {
    File[] roots;
    
    public PathChooser(){
        roots = File.listRoots(); 
    }
    
    public File[] getRootsFile(){
        return roots;
    }
    
    public File[] showChild(String path){
        File directoryPath=new File(path);
        File[]childList= directoryPath.listFiles();
        return childList;
    }
}
