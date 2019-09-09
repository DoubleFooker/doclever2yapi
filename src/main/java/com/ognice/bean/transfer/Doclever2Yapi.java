package com.ognice.bean.transfer;

import com.alibaba.fastjson.JSON;
import com.ognice.bean.DocleverInfo;
import com.ognice.bean.YapiInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author doublefooker
 **/

public class Doclever2Yapi {
    private static Map<Integer, String> keys = new HashMap<>();

    static {
        keys.put(0, "string");
        keys.put(1, "number");
        keys.put(2, "boolean");
        keys.put(3, "array");
        keys.put(4, "object");
    }

    /**
     * 设置Query类型参数
     *
     * @param queryParam
     * @param reqQueries
     */
    private void setQueryParams(List<DocleverInfo.QueryParam> queryParam, List<YapiInfo.ListBean.ReqQuery> reqQueries) {
        if (queryParam != null) {
            for (DocleverInfo.QueryParam qp : queryParam) {
                YapiInfo.ListBean.ReqQuery rq = new YapiInfo.ListBean.ReqQuery();
                rq.setName(qp.getName())
                        .setRequired(qp.getMust())
                        .setDesc(qp.getRemark());
                reqQueries.add(rq);
            }
        }
    }

    /**
     * 设置header参数
     *
     * @param header
     * @param reqHeaders
     */
    private void setHeaderParams(List<DocleverInfo.Header> header, List<YapiInfo.ListBean.ReqHeadersBean> reqHeaders) {
        for (DocleverInfo.Header h : header) {
            YapiInfo.ListBean.ReqHeadersBean headersBean = new YapiInfo.ListBean.ReqHeadersBean();
            headersBean.setName(h.getName())
                    .setRequired("1")
                    .setValue(h.getValue());
            reqHeaders.add(headersBean);
        }
    }


    /**
     * 多级目录提升
     */
    private void dirUp(List<DocleverInfo.InfoData> infoDataList, List<DocleverInfo.DataBean> newData, String parentName) {


        for (DocleverInfo.InfoData infoData : infoDataList) {
            if (infoData.getParam() == null) {
                DocleverInfo.DataBean dataBean1 = new DocleverInfo.DataBean();
                //if (infoData.getName().length() > 5) {
                //    infoData.setName(infoData.getName().substring(0, 4));
                //    System.out.println(infoData.getName());
                //}
                dataBean1.setData(infoData.getData()).setName(parentName + "-" + infoData.getName());
                newData.add(dataBean1);

                dirUp(infoData.getData(), newData, dataBean1.getName());
            } else {

            }
        }
    }

    /**
     * 按分组转化doclover to yapi
     *
     * @param docleverInfo
     * @param yapiInfos
     * @return
     */
    public void transfer2Yapi(DocleverInfo docleverInfo, List<YapiInfo> yapiInfos) {
        //doc分组
        List<DocleverInfo.DataBean> data = docleverInfo.getData();
        List<DocleverInfo.DataBean> newData = new ArrayList<>();
        CollectionUtils.addAll(newData, new Object[data.size()]);
        Collections.copy(newData, data);
        //二级目录情况 直接封装到一级目录
        for (DocleverInfo.DataBean dataBean : data) {

            dirUp(dataBean.getData(), newData, dataBean.getName());
            //for (DocleverInfo.InfoData infoData : dataBean.getData()) {
            //    if (infoData.getParam() == null) {
            //        DocleverInfo.DataBean dataBean1 = new DocleverInfo.DataBean();
            //        dataBean1.setData(infoData.getData()).setName(infoData.getName());
            //        newData.add(dataBean1);
            //    }
            //}
            //newData.add(dataBean);
        }
        for (DocleverInfo.DataBean dataBean : newData) {
            //一个yapi分组
            YapiInfo yapiInfo = new YapiInfo();
            yapiInfo.setAddTime(new Long(System.currentTimeMillis()).intValue())
                    .setIndex(0).setUpTime(yapiInfo.getAddTime()).setName(dataBean.getName()).setDesc(dataBean.getName());
            //分组接口 doc一个接口参数
            List<YapiInfo.ListBean> listBeans = new ArrayList<>();
            for (DocleverInfo.InfoData infoData : dataBean.getData()) {
                YapiInfo.ListBean listBean = new YapiInfo.ListBean();
                listBean.setQueryPath(new YapiInfo.ListBean.QueryPathBean()
                        //.setPath(StringUtils.isEmpty(infoData.getUrl()) ? "/demo" : infoData.getUrl())
                        .setParams(Collections.emptyList()))
                        .setEditUid(0)
                        .setStatus(infoData.getFinish() == 1 ? "done" : "undone")
                        .setType("static")
                        .setReqBodyIsJsonSchema(true)
                        .setResBodyIsJsonSchema(true).setApiOpened(true)
                        .setIndex(0)
                        .setTag(Collections.EMPTY_LIST)
                        .setMethod(infoData.getMethod()).setCatid(18)
                        .setTitle(infoData.getName())
                        //.setPath(infoData.getUrl())
                        .setProjectId(18)
                        .setReqParams(Collections.EMPTY_LIST)
                        .setResBodyType("json")
                        .setUid(94)
                        .setAddTime(new Long(System.currentTimeMillis()).intValue())
                        .setUpTime(new Long(System.currentTimeMillis()).intValue())
                        .setPath(StringUtils.isEmpty(infoData.getUrl()) ? "/null-" + UUID.randomUUID() : infoData.getUrl().startsWith("/") ? infoData.getUrl() : "/" + infoData.getUrl());
                //请求参数 doc多个参数组合
                if (infoData.getParam() != null) {
                    DocleverInfo.Param param = infoData.getParam().get(0);
                    List<DocleverInfo.BodyParam> bodyParam = param.getBodyParam();
                    List<DocleverInfo.Header> header = param.getHeader();
                    List<YapiInfo.ListBean.ReqQuery> reqQueries = new ArrayList<>();
                    List<DocleverInfo.QueryParam> queryParam = param.getQueryParam();
                    this.setQueryParams(queryParam, reqQueries);
                    // this.setBodyParam(bodyParam, reqBodys);
                    List<YapiInfo.ListBean.ReqHeadersBean> reqHeaders = new ArrayList<>();
                    this.setHeaderParams(header, reqHeaders);
                    List<DocleverInfo.OutParam> outParam = param.getOutParam();
                    YapiInfo.ListBean.Result result = new YapiInfo.ListBean.Result();
                    result.setTitle("empty object")
                            .setType("object")
                            .setProperties(new HashMap<>(32))
                            .setRequired(new ArrayList<>());
                    outParam.parallelStream().forEach(o -> this.setOutParams(o, result));
                    YapiInfo.ListBean.Result reqResult = new YapiInfo.ListBean.Result();
                    reqResult.setType("object")
                            .setProperties(new HashMap<>(32))
                            .setRequired(new ArrayList<>());
                    if (bodyParam != null) {
                        listBean.setReqBodyType("json");
                        bodyParam.parallelStream().forEach(o -> this.setReqParams(o, reqResult));
                    }
                    if (param.getBodyInfo() != null && param.getBodyInfo().getRawJSON() != null) {
                        param.getBodyInfo().getRawJSON().parallelStream().forEach(o -> this.setReqParamsByInfo(o, reqResult));
                    }
                    listBean.setReqQuery(reqQueries)
                            .setReqHeaders(reqHeaders)
                            // .setReqBodyForm(reqBodys)
                            .setMarkdown("")
                            .setDesc(infoData.getRemark())
                            .setResBody(JSON.toJSONString(result))
                            .setResBodyType("json")
                            .setReqBodyOther(JSON.toJSONString(reqResult));
                }
                listBeans.add(listBean);
            }
            yapiInfo.setList(listBeans);
            yapiInfos.add(yapiInfo);
        }
        Set<String> paths = new HashSet<>();
        Map<String, Integer> repeats = new HashMap<>();
        for (YapiInfo y : yapiInfos) {
            for (YapiInfo.ListBean l : y.getList()) {
                boolean exits = paths.add(l.getMethod() + "-" + l.getPath());
                if (!exits) {
                    if (repeats.get(l.getMethod() + "-" + l.getPath()) == null) {
                        repeats.put(l.getMethod() + "-" + l.getPath(), 0);

                    } else {
                        repeats.put(l.getMethod() + "-" + l.getPath(), repeats.get(l.getMethod() + "-" + l.getPath()) + 1);
                    }
                    l.setPath(l.getPath() + "-repeat-" + repeats.get(l.getMethod() + "-" + l.getPath()));
                }
            }
        }
        Iterator<YapiInfo> iterator0 = yapiInfos.iterator();
        while (iterator0.hasNext()) {
            YapiInfo y = iterator0.next();
            if (y.getList() == null || y.getList().isEmpty()) {
                iterator0.remove();
            } else {
                y.getList().removeIf(b -> b.getPath().contains("/null-"));

            }
        }
        yapiInfos.removeIf(y -> y.getList() == null || y.getList().isEmpty());

    }

    /**
     * 设置Body请求参数包含对象的情况
     *
     * @param rawJSON
     * @param result
     */
    private void setReqParamsByInfo(DocleverInfo.RawJSON rawJSON, YapiInfo.ListBean.Result result) {
        YapiInfo.ListBean.Propertie propertie = new YapiInfo.ListBean.Propertie();
        if (rawJSON.getMust() == 1) {
            result.getRequired().add(rawJSON.getName());
        }
        if (rawJSON.getData() == null || rawJSON.getData().isEmpty()) {
            result.getProperties().put(rawJSON.getName() == null ? "" : rawJSON.getName(), propertie.setType(keys.get(rawJSON.getType())).setDescription(rawJSON.getRemark()));
        }
        if (rawJSON.getType() == 4) {
            YapiInfo.ListBean.Result itemResult = new YapiInfo.ListBean.Result();
            itemResult.setType("object")
                    .setProperties(new HashMap<>())
                    .setRequired(new ArrayList<>());
            for (DocleverInfo.RawJSON itemOutparam : rawJSON.getData()) {
                this.setReqParamsByInfo(itemOutparam, itemResult);

            }
            YapiInfo.ListBean.Propertie propertie1 = propertie.setType(keys.get(rawJSON.getType())).setDescription(rawJSON.getRemark()).setProperties(itemResult.getProperties());
            if (rawJSON.getName() == null) {
                result.setType("object");
                for (String key : propertie1.getProperties().keySet()) {
                    result.getProperties().put(key, propertie1.getProperties().get(key));

                }
            } else {
                result.setType(keys.get(rawJSON.getType()));
                result.getProperties().put(rawJSON.getName(), propertie1);
            }
        }

        return;

    }

    /**
     * 设置Body请求参数
     *
     * @param result
     */
    private void setReqParams(DocleverInfo.BodyParam outParam, YapiInfo.ListBean.Result result) {
        YapiInfo.ListBean.Propertie propertie = new YapiInfo.ListBean.Propertie();
        if (outParam.getMust() == 1) {
            result.getRequired().add(outParam.getName());
        }
        result.getProperties().put(outParam.getName() == null ? "" : outParam.getName(), propertie.setType(keys.get(outParam.getType())).setDescription(outParam.getRemark()));
        return;

    }

    /**
     * 返回结果封装
     *
     * @param outParam
     * @param result
     */
    private void setOutParams(DocleverInfo.OutParam outParam, YapiInfo.ListBean.Result result) {
        YapiInfo.ListBean.Propertie propertie = new YapiInfo.ListBean.Propertie();
        if (outParam.getMust() == 1) {
            result.getRequired().add(outParam.getName());
        }
        if (outParam.getData() == null || outParam.getData().isEmpty()) {
            YapiInfo.ListBean.Propertie detailProp = propertie.setType(keys.get(outParam.getType())).setDescription(outParam.getRemark()).setMock(new YapiInfo.ListBean.Mock().setMock(outParam.getMock()));
            result.getProperties().put(outParam.getName() == null ? "" : outParam.getName(), detailProp);
            return;
        } else {

            if (outParam.getType() == 3) {
                YapiInfo.ListBean.Result itemResult = new YapiInfo.ListBean.Result();
                itemResult.setType("array")
                        .setProperties(new HashMap<>())
                        .setRequired(new ArrayList<>());
                for (DocleverInfo.OutParam itemOutparam : outParam.getData()) {
                    this.setOutParams(itemOutparam, itemResult);

                }
                propertie.setItems(itemResult);
                result.getProperties()
                        .put(outParam.getName() == null ? "" : outParam.getName(), propertie
                                .setType(keys.get(outParam.getType()))
                                .setDescription(outParam.getRemark()));

            } else if (outParam.getType() == 4) {
                YapiInfo.ListBean.Result itemResult = new YapiInfo.ListBean.Result();
                itemResult.setType("object")
                        .setProperties(new HashMap<>())
                        .setRequired(new ArrayList<>());
                for (DocleverInfo.OutParam itemOutparam : outParam.getData()) {
                    this.setOutParams(itemOutparam, itemResult);

                }
                YapiInfo.ListBean.Propertie propertie1 = propertie.setType(keys.get(outParam.getType())).setDescription(outParam.getRemark()).setProperties(itemResult.getProperties());
                if (outParam.getName() == null) {
                    result.setType("object");
                    for (String key : propertie1.getProperties().keySet()) {
                        result.getProperties().put(key, propertie1.getProperties().get(key));
                    }
                } else {
                    result.setType(keys.get(outParam.getType()));
                    result.getProperties().put(outParam.getName(), propertie1);
                }
            } else {
//理论上不存在
                for (DocleverInfo.OutParam outPara : outParam.getData()) {
                    this.setOutParams(outPara, result);

                }

            }

        }

    }

}
