package jmx;

public interface InputMBean {
    void submit(String name, String classpath, String mainClass, int period); //выполнить задачу

    void status(String name, String mainClass); //получить статус задачи

    void cancel(String name, String mainClass); //закрыть задачу

    void startProfiling(String name, String mainClass); //профилирование задач (время выполн)

    void stopProfiling(String name);

    boolean isIsProfiling();

    void setIsProfiling(boolean isProfiling);
}
