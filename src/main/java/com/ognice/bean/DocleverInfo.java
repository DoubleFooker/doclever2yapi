package com.ognice.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author doublefooker
 **/

@Data
@Accessors(chain = true)
public class DocleverInfo {

    private String flag;
    private InfoBean info;
    private GlobalBean global;
    //分组
    private List<DataBean> data;


    @Data
    @Accessors(chain = true)
    public static class InfoBean {
        private String name;
        private String description;
    }


    @Data
    @Accessors(chain = true)
    public static class GlobalBean {
        private String before;
        private String after;
        private List<BaseurlBean> baseurl;
        private List<?> article;
        private List<?> status;
        private List<?> template;


        @Data
        @Accessors(chain = true)
        public static class BaseurlBean {
            private String url;
            private String remark;
        }
    }


    @Data
    @Accessors(chain = true)
    public static class DataBean {
        private String name;
        private int type;
        private String id;
        private List<InfoData> data;
    }

    @Data
    @Accessors(chain = true)
    public static class InfoData {
        private List<InfoData> data;
        private List<Param> param;
        private int finish;
        private int sort;
        private String name;
        private String url;
        private String remark;
        private String method;
        private String id;
    }

    @Data
    @Accessors(chain = true)
    public static class Param {
        private Inject before;
        private Inject after;
        private String name;
        private String id;
        private String remark;
        private List<Header> header;
        private List<QueryParam> queryParam;
        private List<BodyParam> bodyParam;
        private BodyInfo bodyInfo;
        private List<OutParam> outParam;
        private OutInfo outInfo;
        private List restParam;
        private List example;
    }

    @Data
    @Accessors(chain = true)
    public static class OutInfo {
        private int type;
        private String rawRemark;
        private String rawMock;
        private int jsonType;
    }

    @Data
    @Accessors(chain = true)
    public static class OutParam {
        private String name;
        private int type;
        private String remark;
        private int must;
        private String mock;
        private List<OutParam> data;

    }

    @Data
    @Accessors(chain = true)
    public static class BodyInfo {
        private String rawTextRemark;
        private String rawFileRemark;
        private String rawText;
        private int type;
        private int rawType;

        private List<RawJSON> rawJSON;


    }

    @Data
    @Accessors(chain = true)
    public static class RawJSON {
        private String name;
        private String remark;
        private String mock;
        private int must;
        private int type;
        private List<RawJSON> data;


    }

    @Data
    @Accessors(chain = true)
    public static class QueryParam {
        private String name;
        private int must;
        private String remark;

    }

    @Data
    @Accessors(chain = true)
    public static class BodyParam {
        private String name;
        private int type;
        private int must;
        private String remark;
    }

    @Data
    @Accessors(chain = true)
    public static class Header {
        private String name;
        private String value;
        private String remark;

    }

    @Data
    @Accessors(chain = true)
    public static class Inject {
        private int mode;
        private String code;

    }
}
