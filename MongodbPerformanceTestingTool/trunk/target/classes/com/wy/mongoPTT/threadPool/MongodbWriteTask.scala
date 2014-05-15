package com.wy.mongoPTT.threadPool

import org.apache.commons.lang3.StringUtils
import org.slf4j._
import scala.util.control.Breaks._
import com.wy.mongoPTT.dao.StaffInfoDAOImpl
import com.wy.mongoPTT.bean._
import java.util.UUID

class MongodbWriteTask(dao: StaffInfoDAOImpl) extends Runnable{


	override def run() {
	  while(isRun) {
	    try {
		  dao.insert(new StaffInfo(UUID.randomUUID().toString()))
		  
	    } catch {
	      case e: Exception =>
	        e.printStackTrace()
	    }
	  }
	}

	def stop() = {
      isRun = false
	}
	
	@volatile private var isRun: Boolean = true
	private val log = LoggerFactory.getLogger(classOf[MongodbWriteTask])
}
