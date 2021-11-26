package com.example.camundajavasdk;

import org.camunda.bpm.client.ExternalTaskClient;

import java.awt.*;
import java.net.URI;
import java.util.logging.Logger;

/**
 * @author Administrator
 */
public class CamundaJavaSdkMain {

    private static final Logger LOGGER = Logger.getLogger(CamundaJavaSdkMain.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("register")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    // Get a process variable
                    String enterpriseName = externalTask.getVariable("enterprise_name");
                    String regCode = externalTask.getVariable("reg_code");
                    LOGGER.info("企业：" + enterpriseName + ", regCode：" + regCode + "审批通过。");

                    try {
                        Desktop.getDesktop().browse(new URI("https://www.baidu.com"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }

}
