## 环境部署

环境：

- web：http://localhost:8080/  用户名/密码：demo / demo
- Swagger：http://localhost:8080/swaggerui/

数据库：alpha 环境 camunda 库。

## 演示场景

F2B 在认证环节需要录入或修改企业资料，下面是 F2B 与 MDM 的交互流程。

![image-20211125111320131](https://gitee.com/yanglu_u/ImgRepository/raw/master/image-20211125111320131.png)

## 演示过程

### BPMN 定义

![image-20211125111513572](https://gitee.com/yanglu_u/ImgRepository/raw/master/image-20211125111513572.png)

### 发布工作流

![image-20211125115002478](https://gitee.com/yanglu_u/ImgRepository/raw/master/image-20211125115002478.png)

### 启动工作流

![image-20211126092635545](https://gitee.com/yanglu_u/ImgRepository/raw/master/image-20211126092635545.png)

### 业务处理

```java
public static void main(String[] args) {
    ExternalTaskClient client = ExternalTaskClient.create()
            .baseUrl("http://localhost:8080/engine-rest")
            .asyncResponseTimeout(10000) // long polling timeout
            .build();
    // subscribe to an external task topic as specified in the process
    client.subscribe("register")
            .lockDuration(1000) // the default lock duration is 20 seconds, but you can overrid
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
```



