package com.wy.mongoPTT.dao;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.wy.mongoPTT.bean.StaffInfo;

@Component("staffInfoDAOImpl")
class StaffInfoDAOImpl extends MongoTemplateSupport {

	def insert(staffInfo: StaffInfo) {
			this.mongoTemplate.save(staffInfo);
	}

	def findByEmpId(empId: Int): StaffInfo = {
		this.mongoTemplate.findOne(Query.query(Criteria.where("empId").is(empId+ "")), classOf[StaffInfo]);
	}
	
	def count():Long = {
	  this.mongoTemplate.count(new Query(), classOf[StaffInfo])
	}
	
	def drop() {
	  this.mongoTemplate.dropCollection(classOf[StaffInfo])
	}
	
}
