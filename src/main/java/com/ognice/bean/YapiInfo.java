package com.ognice.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;

/**
 * @author doublefooker
 **/


@Data
@Accessors(chain = true)
public class YapiInfo {

    private int index;
    private String name;
    private String desc;
    private int addTime;
    private int upTime;
    private List<ListBean> list;


    @Data
    @Accessors(chain = true)
    public static class ListBean {
        private QueryPathBean queryPath;
        private int editUid;
        private String status;
        private String type;
        private boolean reqBodyIsJsonSchema;
        private boolean resBodyIsJsonSchema;
        private boolean apiOpened;
        private int index;
        private int id;
        private String method;
        private int catid;
        private String title;
        private String path;
        private int projectId;
        private String resBodyType;
        private int uid;
        private int addTime;
        private int upTime;
        private int v;
        private String markdown;
        private String desc;
        private String resBody;
        private String reqBodyOther;
        private String reqBodyType;
        private List<?> tag;
        private List<?> reqParams;
        private List<ReqQuery> reqQuery;
        private List<ReqHeadersBean> reqHeaders;
        private List<ReqQuery> reqBodyForm;

        @Data
        @Accessors(chain = true)
        public static class Result {
            private String type;
            private String title;
            private HashMap<String, Propertie> properties;
            private List<String> required;

        }

        @Data
        @Accessors(chain = true)
        public static class Propertie {
            private String type;
            private String description;
            private Result items;
            private HashMap<String, Propertie> properties;
            private Mock mock;


        }

        @Data
        @Accessors(chain = true)
        public static class Mock {
            private String mock;

        }

        @Data
        @Accessors(chain = true)
        public static class ReqQuery {
            private int required;
            private String name;
            private String example;
            private String desc;
        }

        @Data
        @Accessors(chain = true)
        public static class ReqBodyForm {
            private String required;
            private String name;
            private String type;
        }

        @Data
        @Accessors(chain = true)
        public static class QueryPathBean {


            private String path;
            private List<?> params;
        }


        @Data
        @Accessors(chain = true)
        public static class ReqHeadersBean {
            private String required;
            private String id;
            private String name;
            private String value;
        }
    }
}
