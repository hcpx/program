package com.scchuangtou.entity;

public class PublishIllnessHelpReqEntity {
	public String token;
	//项目
	public int project_type;
	public int help_type;
	public double capital_goal;
	public int days;
	public String capital_purpose;
	public String title;
	public String detail_description;
	//收款人
	public String payee_name;
	public String payee_id_num;
	public String payee_phone_num;
	//收款账号
	public String capital_id;
	//受助人信息
	public String aided_person_name;
	public String aided_person_id;
	public String aided_person_phone;
	//受助人医院
	public String aided_person_hospital;
	public String aided_person_hospital_phone;
	//组织信息
	public String organize_name;
	public String organize_phone;
	//项目描述图片最多八张
}
