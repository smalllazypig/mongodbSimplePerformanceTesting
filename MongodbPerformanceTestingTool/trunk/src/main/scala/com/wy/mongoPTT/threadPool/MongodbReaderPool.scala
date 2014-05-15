package com.wy.mongoPTT.threadPool

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.Resource
import org.slf4j._
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong
import com.wy.mongoPTT.dao.StaffInfoDAOImpl
import java.util.ArrayList
import com.wy.mongoPTT.bean.StaffInfo
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService

@Component("mongodbReaderPool")
class MongodbReaderPool {

	def init(poolSize: Int) {
	  if(threadPool != null) {
	    this.shutdown()
	  }
		threadPool = new ReadThreadPool(poolSize, poolSize, 60000, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue[Runnable](), new QueryThreadFactory())
		oldcounter.set(0L)
		counter.set(0L)
		startRead(poolSize)
		
		ses = Executors.newSingleThreadScheduledExecutor()
		ses.scheduleAtFixedRate(new Thread() {
		  override def run() {
		    
		    readInfo = "totalread:" + counter.get() + "	total:" + dao.count + "	read in 1 sec:" + (counter.get() - oldcounter.get());
		    oldcounter.set(counter.get())
		    
		  }
		}, 1000, 1000, TimeUnit.MILLISECONDS)
	}

   def shutdown() {
	    for(task <- list) {
	      task.stop
	    }
	    list = scala.collection.mutable.ListBuffer.empty[MongodbReadTask]
		if(threadPool != null)
	      threadPool.shutdown()
	    ses.shutdown()
	}

	def startRead(poolSize: Int) {
	  var staff = new StaffInfo
	  staff.empId = "1"
	  dao.insert(staff)
	  for(i <- 0 until poolSize) {
		  var task = new MongodbReadTask(dao, 1,counter)
		  list += task
		  threadPool.execute(task)
	  }
	}

	private class ReadThreadPool(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long,
				unit: TimeUnit, workQueue: BlockingQueue[Runnable], threadFacotry: ThreadFactory) extends ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFacotry) {
		
	}
	
	private class QueryThreadFactory extends ThreadFactory {

		override def newThread(r: Runnable): Thread = {
			new QueryThread(r, "read " + "-" + created.incrementAndGet())
		}

		private val created = new AtomicInteger()
	}
	
	private class QueryThread(r: Runnable, poolName: String) extends Thread(r, poolName) {
		setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			override def uncaughtException(t: Thread, e: Throwable) {
				log.error("线程[" + t.getName() + "]发生未捕获异常:", e)
			}
		})
		override def run() {
			log.warn("线程[" + this.getName() + "]运行")
			try {
				ALIVE.incrementAndGet()
				super.run()
			} finally {
				ALIVE.decrementAndGet()
				log.warn("线程[" + this.getName() + "]退出")
			}
		}
	}
	
	private val ALIVE = new AtomicInteger()
	private var threadPool: ReadThreadPool = _
	private var counter = new AtomicLong()
	private var oldcounter = new AtomicLong(0)
	private var ses = Executors.newSingleThreadScheduledExecutor()
	var readInfo: String = ""
	private var list = scala.collection.mutable.ListBuffer.empty[MongodbReadTask]
	@Resource(name="staffInfoDAOImpl") var dao: StaffInfoDAOImpl = _
	private val log = LoggerFactory.getLogger(classOf[MongodbReaderPool])
}