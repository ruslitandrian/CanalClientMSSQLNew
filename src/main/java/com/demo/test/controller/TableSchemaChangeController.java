package com.demo.test.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.demo.test.entity.TableSchemaChange;
import com.demo.test.service.ITableSchemaChangeService;
import com.vipabc.basic.common.web.controller.BaseController;
import com.vipabc.basic.common.web.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author author
 * @since 2018-03-01
 */
@RestController
@RequestMapping("/tableSchemaChange")
public class TableSchemaChangeController extends BaseController {

    @Autowired
    private ITableSchemaChangeService service;

    @RequestMapping(value = "", method = RequestMethod.POST)
    // public ResponseVO add(@RequestBody TableSchemaChange entity) {
     public ResponseVO add(@RequestBody String entity) {
        //service.insert(entity);
        return getSuccess();
    }

    public void Insert(){

        return;
    }


}
