package jmx;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Input implements InputMBean {
    public static boolean isProfiling;
    private static Status threadStatus = new Status();
    private final String[] args;
    private String ClassName;
    private HashMap<String, ScheduledExecutorService> serviceStore = new HashMap<>(); //хранилище сервисов
    private HashMap<String, Integer> periodStore = new HashMap<>(); //хранилище периодов

    public Input(String[] args) {
        this.args = args;
    }

    @Override
    public void submit(String name, String classpath, String mainClass, int period) {
        Tasks newTask = new Tasks(name, classpath, mainClass, args);
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(newTask, 0, period, TimeUnit.SECONDS); //запускаем периодическое выполнение задачи
        ClassName = mainClass;
        serviceStore.put(name + "|" + ClassName + ".class", service);
        periodStore.put(name + "|" + ClassName + ".class", period);
    }

    @Override
    public void status(String name, String mainClass) {
        threadStatus.setName(name + "|" + mainClass + ".class"); //устанавливаем имя задачи
        threadStatus.status();
    }

    @Override
    public void cancel(String name, String mainClass) {
        if (serviceStore.containsKey(name + "|" + mainClass + ".class")) { //поиск задачи по имени в хранилище
            serviceStore.get(name + "|" + mainClass + ".class").shutdown();
            System.out.println("\nTask " + name + "|" + mainClass + ".class completed successfully!\n");
            serviceStore.remove(name + "|" + mainClass + ".class");
            Tasks.statusStore.remove(name + "|" + mainClass + ".class"); //задание не runnable
        } else {
            System.out.println("\nIncorrect task input\n");
        }
    }

    @Override
    public void startProfiling(String name, String mainClass) {
        if (Tasks.statusStore.get(name + "|" + mainClass + ".class").equals(Thread.State.RUNNABLE)) {
            var classpath = Tasks.classpathStore.get(name + "|" + mainClass + ".class");
            cancel(name, mainClass);
            try {
                setIsProfiling(true);
                submit(name, Tasks.classpathStore.get(name + "|" + mainClass + ".class"),
                        mainClass, periodStore.get(name + "|" + mainClass + ".class"));
                setIsProfiling(false);
            } catch (NullPointerException e) {
                System.out.println("\nTask <" + name + "> not found...\n");
            }
        } else {
            System.out.println("\nTask <" + name + "> is not in a state of execution!\n");
        }
    }

    @Override
    public boolean isIsProfiling() {
        return isProfiling;
    }

    @Override
    public void setIsProfiling(boolean isProfiling) {
        Input.isProfiling = isProfiling;
    }

    @Override
    public void stopProfiling(String name) {

    }
}
