package jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) throws MalformedObjectNameException,
            NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        var settings = new Input(args);
        ManagementFactory.getPlatformMBeanServer().registerMBean(
                settings,
                new ObjectName("jmx:type=Input")
        );


        System.out.println("Waiting..."); //ждем действий
        Thread.sleep(Long.MAX_VALUE);
    }
}
