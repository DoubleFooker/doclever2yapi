### Doclever2Yapi -json格式转化工具

jar-file：doclever2yapi-json-1.0-SNAPSHOT.jar

version：v1.0.0
JDK:1.8

### 使用说明

- step 1：进入doclever项目->设置->导出->json导出。

- step 2：将导出json文件放到jar包运行目录下。

- step 3：命令行运行jar包 ，程序会遍历执行在运行目录下以.json后缀结尾且非yapi-前缀的文件，转化为yapi-开头的输出文件。

  ```shell
  java -jar doclever2yapi-json-1.0-SNAPSHOT.jar	
  ```

- step 4：将生成 yapi-XXX.json文件导入yapi平台。