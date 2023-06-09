package com.changhong.sei.demo.controller;

import com.changhong.sei.core.controller.BaseEntityController;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.service.BaseEntityService;
import com.changhong.sei.demo.api.FlowFormApi;
import com.changhong.sei.demo.dto.FlowFormDto;
import com.changhong.sei.demo.entity.FlowForm;
import com.changhong.sei.demo.flow.BaseFlowController;
import com.changhong.sei.demo.flow.BaseFlowEntityService;
import com.changhong.sei.demo.service.FlowFormService;
import com.changhong.sei.edm.sdk.DocumentManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程表单(FlowForm)控制类
 *
 * @author sei
 * @since 2022-04-12 11:02:45
 */
@RestController
@Api(value = "FlowFormApi", tags = "流程表单服务")
@RequestMapping(path = FlowFormApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class FlowFormController extends BaseFlowController<FlowForm, FlowFormDto> implements FlowFormApi {
    /**
     * 流程表单服务对象
     */
    @Autowired
    private FlowFormService service;

    @Autowired
    private DocumentManager documentManager;

    @Override
    public BaseFlowEntityService<FlowForm> getService() {
        return service;
    }

    @Override
    public ResultData<FlowFormDto> save(FlowFormDto dto) {
        ResultData<FlowFormDto> result = super.save(dto);
        if(result.getSuccess() && !CollectionUtils.isEmpty( dto.getAttachmentIdList())){
            documentManager.bindBusinessDocuments(result.getData().getId(), dto.getAttachmentIdList());
        }
        return result;
    }

    @Override
    protected void addProperties(Map<String, String> map) {
        map.put("code", "代码");
    }

    @Override
    public Map<String, Object> getPropertyValue(FlowForm flowForm) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("code", flowForm.getCode());
        return map;
    }

    @Override
    protected void addInitValue(Map<String, Object> map) {
        map.put("code", "111");
    }

    @Override
    protected String getWorkCaption(FlowForm flowForm) {
        return "演示工作摘要-" + flowForm.getCode();
    }

    @Override
    protected String getFlowName(FlowForm flowForm) {
        return "演示流程名称-" + flowForm.getCode();
    }

    @Override
    protected String getBusinessCode(FlowForm flowForm) {
        return flowForm.getCode();
    }

    @Override
    public ResultData<PageResult<FlowFormDto>> findByPage(Search search) {
        return convertToDtoPageResult(service.findByPage(search));
    }
}
