package util.files;

import java.io.*;


public class ObjectSerializer {

    public static void save(Object obj, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException();
        }

        try {
            assert oos != null;
            oos.writeObject(obj);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException();
        }

        try {
            oos.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static <U> U load(String fileName) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException();
        }

        U u = null;
        try {
            assert ois != null;
            u = (U) ois.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException();
        }

        try {
            ois.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException();
        }

        return u;
    }
}
