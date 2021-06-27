package jmx;

public class Status {
    private String name;

    public void status() {
        try {
            if (Tasks.statusStore.get(name).equals(Thread.State.RUNNABLE)) {
                //по имени задачи из хранилища статусов сравниваем ее статус с runnable
                System.out.println("\nTask <" + name + "> is running\n");
            } else {
                System.out.println("\nTask <" + name + "> is not in a state of execution!\n");
            }
        } catch (NullPointerException e) {
            System.out.println("\nTask <" + name + "> not found...\n");
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}

