package com.dc18TokenExchange.Resourceserver;

import com.dc18TokenExchange.Resourceserver.model.Workplace;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
public class ImageHandling {

    //Saves image as bytes
    public byte[] saveImage(String path){
        File file = new File(path);
        byte[] bFile = new byte[(int) file.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bFile;
    }

    //Saves image to a local storage on a hard drive (not used)
    /*public void getImage(String newPath, Workplace workplace){
        byte[] workplaceImage = workplace.getImage();

        try{
            FileOutputStream fos = new FileOutputStream(newPath);
            fos.write(workplaceImage);
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}
