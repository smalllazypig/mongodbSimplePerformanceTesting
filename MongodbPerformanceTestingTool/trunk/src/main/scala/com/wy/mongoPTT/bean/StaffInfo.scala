package com.wy.mongoPTT.bean

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="staffInfos")
class StaffInfo(emp: String) {

  def this() = this("2")
  var empId: String = emp
  
}