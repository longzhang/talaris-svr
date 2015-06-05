package me.ele.talaris.main;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.napos.getorder.task.GetOrderFromNaposTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) throws UserException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "coffee-hermes.xml",
                "context-talaris-svr.xml", "talaris-napos.xml" });

        GetOrderFromNaposTask getOrderFromNaposTask = (GetOrderFromNaposTask) applicationContext
                .getBean("getOrderFromNaposTask");
        while (true) {
            getOrderFromNaposTask.startTask();
        }

    }

}
