package com.wy.mongoPTT.dao;

import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

@Data
class MongoTemplateSupport {
	@Autowired
	var mongoTemplate: MongoTemplate = _;
}
