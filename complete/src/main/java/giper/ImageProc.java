package giper;

import java.io.*;

/**
 * Created by prochiy on 8/21/15.
 */
public class ImageProc {

    static final public String path = System.getProperty("user.home") +
            System.getProperty("file.separator");
    static final public String serverPath = path + "/ServerImages/";

    public static byte[] read(String name){
        String fileName = path + "Firefox_wallpaper.jpg";
        byte[] image = null;
        File file = new File(fileName);
        image = new byte[(int)file.length()];
        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);) {

            dis.readFully(image, 0, (int) file.length());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String write(byte[] image) throws IOException {

        File file = File.createTempFile("imag", ".jpg", new File(path + serverPath));

        //File file = new File(fileName);
        try(FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);) {

            dos.write(image, 0, image.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getName();
    }

    public static void main(String[] args){
        ImageProc imageProc = new ImageProc();
        imageProc.read("");
        //imageProc.write("");
    }


}
