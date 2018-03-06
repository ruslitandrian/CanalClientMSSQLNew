package com.tutorabc.test.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;


@TableName("lesson_question_temp")
public class LessonQuestionTemp extends Model<LessonQuestionTemp> {

    private static final long serialVersionUID = 1L;

		private Integer valid;
	@TableField("evaluation_type")
		private Integer evaluationType;
		private Integer sn;
	@TableField("question_str")
		private String questionStr;
	@TableField("test_sn")
		private Integer testSn;
	@TableField("total_score")
		private Integer totalScore;
	@TableField("inp_date")
		private Date inpDate;
	@TableField("client_sn")
		private Integer clientSn;
	@TableField("client_level")
		private Integer clientLevel;


	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Integer getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(Integer evaluationType) {
		this.evaluationType = evaluationType;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getQuestionStr() {
		return questionStr;
	}

	public void setQuestionStr(String questionStr) {
		this.questionStr = questionStr;
	}

	public Integer getTestSn() {
		return testSn;
	}

	public void setTestSn(Integer testSn) {
		this.testSn = testSn;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Date getInpDate() {
		return inpDate;
	}

	public void setInpDate(Date inpDate) {
		this.inpDate = inpDate;
	}

	public Integer getClientSn() {
		return clientSn;
	}

	public void setClientSn(Integer clientSn) {
		this.clientSn = clientSn;
	}

	public Integer getClientLevel() {
		return clientLevel;
	}

	public void setClientLevel(Integer clientLevel) {
		this.clientLevel = clientLevel;
	}

	@Override
	protected Serializable pkVal() {
		return this.sn;
	}

}
