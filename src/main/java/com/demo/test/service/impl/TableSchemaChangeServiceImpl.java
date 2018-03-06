package com.demo.test.service.impl;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.demo.test.entity.TableSchemaChange;
import com.demo.test.mapper.TableSchemaChangeMapper;
import com.demo.test.service.ITableSchemaChangeService;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.List;

@Service
public class TableSchemaChangeServiceImpl extends ServiceImpl<TableSchemaChangeMapper, TableSchemaChange> implements ITableSchemaChangeService {

        @Override
        public void Init() {

                // String host = "172.16.234.117"; //AddressUtils.getHostIp()
                String host = AddressUtils.getHostIp();
                System.out.println(host);
                CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(host,
                        11111), "example", "", "");

                int batchSize = 5 * 1024;

                try {
                        connector.connect();
                        connector.subscribe(".*\\..*");
                        // connector.rollback();
                        while (true) {
                                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                                long batchId = message.getId();
                                int size = message.getEntries().size();
                                if (batchId == -1 || size == 0) {
                                        // try {
                                        //     Thread.sleep(1000);
                                        //  } catch (InterruptedException e) {
                                        //      e.printStackTrace();
                                        //   }
                                } else {
                                        printEntry(message.getEntries());
                                }

                                connector.ack(batchId); // 提交确认
                                // connector.rollback(batchId); // 处理失败, 回滚数据
                        }
                } catch (Exception e) {
                        System.out.println(e);
                } finally {
                        connector.disconnect();
                }

                System.out.println("In service");
        }

        private void printEntry( List<CanalEntry.Entry> entrys) {
                for (CanalEntry.Entry entry : entrys) {
                        if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                                continue;
                        }

                        CanalEntry.RowChange rowChage = null;
                        try {
                                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                        } catch (Exception e) {
                                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                                        e);
                        }

                        CanalEntry.EventType eventType = rowChage.getEventType();
                        System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                                entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                                entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                                eventType));

                        // CREATE TABLE
                        if (eventType == CanalEntry.EventType.QUERY || rowChage.getIsDdl()) {
                                System.out.println(" sql ----> " + rowChage.getSql());
                                if( eventType == CanalEntry.EventType.ERASE) {
                                        DropTable(entry, rowChage.getSql());
                                }else if( eventType == CanalEntry.EventType.RENAME){
                                        AlterTable(entry, rowChage.getSql());
                                }else{
                                        CreateTable(entry, rowChage.getSql());
                                }

                                continue;
                        }

                        for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                                if (eventType == CanalEntry.EventType.DELETE) {
                                        Delete(entry, rowData.getBeforeColumnsList());
                                } else if (eventType == CanalEntry.EventType.INSERT) {
                                        Insert(entry, rowData.getAfterColumnsList());
                                } else {
                                        Update(entry, rowData.getAfterColumnsList());
                                        System.out.println("-------> before");
                                        printColumn(rowData.getBeforeColumnsList());
                                        System.out.println("-------> after");
                                }
                        }
                }
        }

        private void printColumn( List<CanalEntry.Column> columns) {
                for (CanalEntry.Column column : columns) {
                        System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
                }
        }

        public void Delete(CanalEntry.Entry entry, List<CanalEntry.Column> columns)
        {
                String dbName = entry.getHeader().getSchemaName();
                String tableName = entry.getHeader().getTableName();
                StringBuilder sql = new StringBuilder();
                StringBuilder sqlColumn = new StringBuilder();
                sql.append("DELETE FROM  `"+ dbName + "`.`"+ tableName + "`  ");
                int index = 0;
                for (CanalEntry.Column column : columns) {
                        if (index > 0) {
                                sqlColumn.append(" AND ");
                        }
                        sqlColumn.append(column.getName() + " = \'\'" + column.getValue() + "\'\'");
                        index++;
                }

                sql.append(" WHERE " + sqlColumn.toString());
                System.out.println(sql.toString());
                if(columns.size()>0) {
                        Exec(GenInsertSQL(tableName, sql.toString()));
                }
        }

        public void Update(CanalEntry.Entry entry, List<CanalEntry.Column> columns)
        {
                String dbName = entry.getHeader().getSchemaName();
                String tableName = entry.getHeader().getTableName();
                StringBuilder sql = new StringBuilder();
                StringBuilder sqlColumn = new StringBuilder();
                StringBuilder sqlKey = new StringBuilder();
                int index = 0;
                for (CanalEntry.Column column : columns) {

                        if(column.getIsKey())
                        {
                                sqlKey.append(column.getName() + " = \'\'" + column.getValue() + "\'\'");
                        }
                        else
                        {
                                sqlColumn.append(column.getName() + " =\'\'" + column.getValue() + "\'\'");
                        }

                        if(index > 1){
                                sqlColumn.append(',');
                        }
                        index++;
                }

                sql.append(" UPDATE   `"+ dbName + "`.`"+ tableName + "`  ");
                if(columns.size()>0) {
                        sql.append(" SET " + sqlColumn.toString());
                        sql.append(" WHERE " + sqlKey.toString());
                        // System.out.println(sql.toString());
                        Exec(GenInsertSQL(tableName, sql.toString()));
                }else{
                        System.out.println("Statement Empty!");
                }
        }

        public void Insert(CanalEntry.Entry entry, List<CanalEntry.Column> columns)
        {
                String dbName = entry.getHeader().getSchemaName();
                String tableName = entry.getHeader().getTableName();
                StringBuilder sql = new StringBuilder();
                StringBuilder sqlColumn = new StringBuilder();
                StringBuilder sqlValue = new StringBuilder();
                sql.append("INSERT INTO `"+ dbName + "`.`"+ tableName + "` (");
                int index = 0;
                for (CanalEntry.Column column : columns) {
                        if(index > 0){
                                sqlColumn.append(',');
                                sqlValue.append(',');
                        }
                        sqlColumn.append(column.getName());
                        sqlValue.append("\'\'" + column.getValue() + "\'\'");
                        index++;
                }
                sql.append(sqlColumn.toString() + " ) VALUES(" + sqlValue.toString() +");");
                System.out.println(sql.toString());
                if(columns.size()>0) {
                        Exec(GenInsertSQL(tableName, sql.toString()));
                }
        }

        public void DropTable(CanalEntry.Entry entry, String sqlStatement)
        {
                // String dbName = entry.getHeader().getSchemaName();
                String tableName = entry.getHeader().getTableName();
                Exec(GenInsertSQL(tableName, sqlStatement));
        }

        public void CreateTable(CanalEntry.Entry entry, String sqlStatement)
        {
                // String dbName = entry.getHeader().getSchemaName();
                String tableName = entry.getHeader().getTableName();
                Exec(GenInsertSQL(tableName, sqlStatement));
        }

        public void AlterTable(CanalEntry.Entry entry, String sqlStatement)
        {
                // String dbName = entry.getHeader().getSchemaName();
                String tableName = entry.getHeader().getTableName();
                Exec(GenInsertSQL(tableName, sqlStatement));
        }

        private TableSchemaChange GenInsertSQL(String tableName, String sqlStatement ){

                TableSchemaChange newEntity = new TableSchemaChange();
                newEntity.setTableName(tableName);
                newEntity.setScripts(sqlStatement);

                return newEntity;
        }

        private void Exec(TableSchemaChange entity)
        {
                super.insert(entity);
        }
}
