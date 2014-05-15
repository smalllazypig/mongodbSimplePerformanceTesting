package com.wy.mongoPTT.threadPool

import org.apache.commons.lang3.StringUtils
import org.slf4j._
import scala.util.control.Breaks._
import com.wy.mongoPTT.dao.StaffInfoDAOImpl
import java.util.concurrent.atomic.AtomicLong

class MongodbReadTask(dao: StaffInfoDAOImpl, empId: Int, counter: AtomicLong) extends Runnable{


	override def run() {
	  while(isRun) {
	    try{
		  val staff = dao.findByEmpId(empId)
		  
	    } catch {
	      case e: Exception =>
	        e.printStackTrace()
	    }
		  counter.incrementAndGet()
	  }
	}

	def stop() = {
      isRun = false
	}

	@volatile private var isRun: Boolean = true
	private val log = LoggerFactory.getLogger(classOf[MongodbReadTask])
}
