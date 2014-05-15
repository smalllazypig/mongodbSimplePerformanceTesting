package com.wy.mongoPTT.web

import scala.collection.JavaConversions.mapAsScalaMap
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.web.context.support.WebApplicationContextUtils
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import akka.actor.ActorSystem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.wy.mongoPTT.threadPool._
import com.wy.mongoPTT.dao.StaffInfoDAOImpl

class ApplicationStarter extends ServletContextListener{

	override def contextInitialized(sec : ServletContextEvent) = {
	  val context : ApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sec.getServletContext())
	  val mongodbWriterPool = context.getBean("mongodbWriterPool").asInstanceOf[MongodbWriterPool]
	  //mongodbWriterPool.init(4)
	  val mongodbReaderPool = context.getBean("mongodbReaderPool").asInstanceOf[MongodbReaderPool]
	  //mongodbReaderPool.init(4)
	  log.info("Mongodb性能测试工具启动完成")
	}

	override def contextDestroyed(sec : ServletContextEvent)  = {
	  val context : ApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sec.getServletContext())
	  val actorSystem = context.getBean("actorSystem").asInstanceOf[ActorSystem]
	  actorSystem.shutdown
	  val mongodbWriterPool = context.getBean("mongodbWriterPool").asInstanceOf[MongodbWriterPool]
	  mongodbWriterPool.shutdown
	  val mongodbReaderPool = context.getBean("mongodbReaderPool").asInstanceOf[MongodbReaderPool]
	  mongodbReaderPool.shutdown
	  context.getBean("staffInfoDAOImpl").asInstanceOf[StaffInfoDAOImpl].drop
	  log.info("Mongodb性能测试工具停止完成")
	}

	val log = LoggerFactory.getLogger(classOf[ApplicationStarter])
}