package jmx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Tasks implements Runnable {
    protected static HashMap<String, Thread.State> statusStore = new HashMap<>();  //хранилище статусов исполняемых задач
    protected static HashMap<String, String> classpathStore = new HashMap<>(); //хранилище путей исп. задач
    private String mainClass, classpath, name, key;
    private String[] args;
    private ClassLoader loader;
    private Method m;
    private Object obj;

    public Tasks(String name, String classpath, String mainClass, String[] args) {
        this.name = name;
        this.mainClass = mainClass;
        this.classpath = classpath;
        this.args = args;

        classpathStore.put(name + "|" + mainClass + ".class", classpath);

        try {
            loader = newLoader(classpath);
            try {
                var clazz = loader.loadClass(mainClass);  //подгружаем класс задачи
                m = clazz.getMethod(name);  //получаем задачу (метод класса)
                obj = clazz.newInstance();
            } catch (NoSuchMethodException e) {
                System.out.println("\nError: " + e.toString() + "\n");
            }
        } catch (Exception e) {
            System.out.println("\nError: " + e.toString() + "\n");
        }
    }

    private ClassLoader newLoader(String dir) throws MalformedURLException {
        var path = Path.of(dir);
        if (!Files.isDirectory(path))
            throw new RuntimeException();
        return new URLClassLoader(new URL[]{path.toUri().toURL()});
    }

    @Override
    public void run() {
        try {
            m.invoke(obj, null);  //выполняем задачу
        } catch (IllegalAccessException e) {
            System.out.println("\nError: " + e.toString() + "\n");
        } catch (InvocationTargetException e) {
            System.out.println("\nError: " + e.toString() + "\n");
        }
        Thread.currentThread().setName("Task <" + name + ">");
        key = name + "|" + mainClass + ".class";
        statusStore.put(key, Thread.currentThread().getState()); //сохраняем статус задачи в хранилище
    }
}
