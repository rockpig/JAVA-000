package com.distributed.demo.demo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomClassLoader extends ClassLoader {

    public static void main(String[] args)
            throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException,
            InvocationTargetException {
        byte[] bytes = toByteArray("D://Download/Chrome/Hello_xlass/Hello.xlass");

        byte[] clazzBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            clazzBytes[i] = (byte) (255 - bytes[i]);
        }

        Class aClass = new CustomClassLoader()
                .defineClass(null, clazzBytes, 0, clazzBytes.length);
        Object instance = aClass.newInstance();
        Method hello = aClass.getMethod("hello");
        hello.invoke(instance);
    }

    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }
}
