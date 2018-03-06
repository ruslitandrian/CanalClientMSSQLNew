package com.tutorabc.test.service;

import com.baomidou.mybatisplus.service.IService;
import com.tutorabc.test.entity.TableSchemaChange;

public interface ITableSchemaChangeService extends IService<TableSchemaChange> {

    void Init();
}
