package com.wy.mongoPTT.web

import java.io.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.context.support.WebApplicationContextUtils
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import java.io.ByteArrayOutputStream
import java.io.InputStream
import com.wy.mongoPTT.threadPool.MongodbReaderPool
import org.springframework.context.ApplicationContext
import com.wy.mongoPTT.threadPool.MongodbWriterPool
import org.apache.commons.lang3.StringUtils

class HttpServletHandler extends HttpServlet {

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    doPost(request, response)
  }

  override def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    response.setContentType("text/html;charset=UTF-8");
    var command = request.getParameter("command")
    var readthreads = request.getParameter("readthreads")
    var writethreads = request.getParameter("writethreads")
    
    val context : ApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext())
	  val mongodbWriterPool = context.getBean("mongodbWriterPool").asInstanceOf[MongodbWriterPool]
	  //
	  val mongodbReaderPool = context.getBean("mongodbReaderPool").asInstanceOf[MongodbReaderPool]
	  //
    
    var responseString = ""
    try {
      command match{
      case "read" => 
        if(StringUtils.isNumeric(readthreads)) {
        			mongodbReaderPool.init(readthreads.toInt)
    		  		responseString = mongodbReaderPool.readInfo
      	}
      case "write" => if(StringUtils.isNumeric(writethreads)) {
        mongodbWriterPool.init(writethreads.toInt)
        responseString = mongodbWriterPool.writeInfo
      }
      case "readstop" => mongodbReaderPool.shutdown
      case "writestop" => mongodbWriterPool.shutdown
      case "readInfo" => responseString = mongodbReaderPool.readInfo
      case "writeInfo" => responseString = mongodbWriterPool.writeInfo
    }
    } catch {
      case e: Exception => log.error("接收请求出现异常：", e)
    } finally {
      try {
        response.getWriter().write(responseString)
        response.getWriter().flush()
        response.getWriter().close()
      } catch {
        case e: IOException => log.error("回写响应时异常：", e)
      }
    }
  }

  private val log: Logger = LoggerFactory.getLogger(classOf[HttpServletHandler])
}