package ru.prochiy.main;

import java.io.*;

/**
 * Created by prochiy on 8/21/15.
 */
public class ImageProc {

    static final public String path = System.getProperty("user.home") +
            System.getProperty("file.separator");
    static final public String serverPath = path + "/ServerImages/";

    public static byte[] read(String fileName) throws IOException {
        //String fileName = path + "Firefox_wallpaper.jpg";
        byte[] image = null;
        File file = new File(fileName);
        image = new byte[(int)file.length()];
        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);) {

            dis.readFully(image, 0, (int) file.length());

        }
        return image;
    }

    public static String write(byte[] image) throws IOException {

        File file = null;

        file = File.createTempFile("imag", ".jpg", new File(path + serverPath));

        //File file = new File(fileName);
        try(FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);) {

            dos.write(image, 0, image.length);

        }
        return file.getName();
    }

}
