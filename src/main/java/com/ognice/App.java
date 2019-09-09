package com.ognice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.ognice.bean.DocleverInfo;
import com.ognice.bean.YapiInfo;
import com.ognice.bean.transfer.Doclever2Yapi;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String jarDir = System.getProperties().getProperty("user.dir");
        System.out.println("当前运行目录：" + System.getProperties().getProperty("user.dir"));
        File file = new File(jarDir);
        Doclever2Yapi doclever2Yapi = new Doclever2Yapi();
        boolean hasFile = false;
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.getName().endsWith(".json") && !f.getName().startsWith("yapi-")) {
                System.out.println("待转化文件：" + f.getName());
                hasFile = true;
                try {
                    DocleverInfo docleverInfo = JSON.parseObject(FileUtils.readFileToString(f, "UTF-8"), DocleverInfo.class);
                    List<YapiInfo> yapiInfos = new ArrayList<>();
                    doclever2Yapi.transfer2Yapi(docleverInfo, yapiInfos);
                    SerializeConfig config = new SerializeConfig();
                    config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                    FileUtils.writeStringToFile(new File(jarDir + "/yapi-" + f.getName()), JSON.toJSONString(yapiInfos, config), "UTF-8");
                    System.out.println("转化完成文件：" + "yapi-" + f.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("运行异常：" + e.getMessage());
                }
            }

        }
        if (!hasFile) {
            System.out.println("当前目录不存在.json文件，无需转化！");
        }
    }
}
