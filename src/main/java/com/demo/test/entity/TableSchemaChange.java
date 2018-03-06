package com.demo.test.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * ${table.comment}
 * </p>
 *
 * @author author
 * @since 2018-03-01
 */
@TableName("TableSchemaChange")
public class TableSchemaChange extends Model<TableSchemaChange> {
	//private static final long serialVersionUID = 1L;

 	@TableField("Sn")
	private Integer Sn;

	@TableField("TableName")
	private String TableName;

	@TableField("Scripts")
	private String Scripts;

	@TableField("CreatedAt")
	private Date CreatedAt;

	public String getTableName() {
		return TableName;
	}

	public void setTableName(String tableName) {
		this.TableName = tableName;
	}

	public String getScripts() {
		return Scripts;
	}

	public void setScripts(String scripts) {
		this.Scripts = scripts;
	}

	public Integer getSn() {
		return Sn;
	}

	public void setSn(Integer sn) {
		this.Sn = sn;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(Date inpDate) {
		this.CreatedAt = inpDate;
	}

	@Override
	protected Serializable pkVal() {
		return this.Sn;
	}
}
