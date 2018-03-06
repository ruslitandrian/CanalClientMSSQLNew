package com.demo.test.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.vipabc.basic.common.web.vo.ResponseVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.demo.test.service.ILessonQuestionTempService;
import com.demo.test.entity.LessonQuestionTemp;
import com.vipabc.basic.common.web.controller.BaseController;


/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author author
 * @since 2018-03-01
 */
@RestController
@RequestMapping("/lessonQuestionTemp")
public class LessonQuestionTempController extends BaseController {
    @Autowired
    private ILessonQuestionTempService service;
    /**
     * 实体查询
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
        public ResponseVO search(@PathVariable Long id) {
        return getFromData(service.selectById(id));
    }

    /**
     * 实体删除
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
        public ResponseVO del(@PathVariable Long id) {
        service.deleteById(id);
        return getSuccess();
    }

    /**
     * 实体添加
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseVO add(@RequestBody LessonQuestionTemp entity) {
        service.insert(entity);
        return getSuccess();
    }


    /**
     * 实体修改
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
        public ResponseVO update(@PathVariable Long id,@RequestBody LessonQuestionTemp entity) {
        //entity.setId(id);
        //service.updateById(entity);
        return getSuccess();
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseVO selectPage(Page<LessonQuestionTemp> page) {
        page = page==null?new Page<>(1,10):page;
        return getFromData(service.selectPage(page));
    }
}
