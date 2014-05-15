package com.wy.mongoPTT.threadPool

import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
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

@Component("mongodbWriterPool")
class MongodbWriterPool {

	def init(poolSize: Int) {
	  if(threadPool != null) {
	    this.shutdown()
	  }
		threadPool = new WriteThreadPool(poolSize, poolSize, 60000, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue[Runnable](), new WriteThreadFactory())
		counter.set(0L)
		oldcounter.set(0L)
		initcounter.set(0L)
		startWrite(poolSize)
		ses = Executors.newSingleThreadScheduledExecutor()
		
		ses.scheduleAtFixedRate(new Thread() {
		  override def run() {
		    if(counter.compareAndSet(0L, dao.count())) {
		      //println("totalwrite:" + 0 + "total：" + dao.count() + "write in 1sec:" + 0);
		      initcounter.set(dao.count())
		      oldcounter.set(initcounter.get())
		    } else {
		      counter.set(dao.count())
		      writeInfo = "totalwrite:" + (counter.get()-initcounter.get()) + "	total:" + counter.get() + "	write in 1sec:" + (counter.get()-oldcounter.get());
		      oldcounter.set(counter.get())
		    }
		    
		  }
		}, 1000, 1000, TimeUnit.MILLISECONDS)
	}

   def shutdown() {
	    for(task <- list) {
	      task.stop
	    }
	    list = scala.collection.mutable.ListBuffer.empty[MongodbWriteTask]
		if(threadPool != null)
	      threadPool.shutdownNow()
	      ses.shutdown()
	}

	def startWrite(poolSize: Int) {
	  for(i <- 0 until poolSize) {
		  var task = new MongodbWriteTask(dao)
		  list += task
		  threadPool.execute(task)
	  }
	}

	private class WriteThreadPool(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long,
				unit: TimeUnit, workQueue: BlockingQueue[Runnable], threadFacotry: ThreadFactory) extends ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFacotry) {
		
	}
	
	private class WriteThreadFactory extends ThreadFactory {

		override def newThread(r: Runnable): Thread = {
			new WriteThread(r, "write " + "-" + created.incrementAndGet())
		}

		private val created = new AtomicInteger()
	}
	
	private class WriteThread(r: Runnable, poolName: String) extends Thread(r, poolName) {
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
	private var threadPool: WriteThreadPool = _
	private var counter = new AtomicLong
	private var oldcounter = new AtomicLong
	private var initcounter = new AtomicLong
	var writeInfo:String = ""
	private var ses = Executors.newSingleThreadScheduledExecutor()
	private var list = scala.collection.mutable.ListBuffer.empty[MongodbWriteTask]
	@Resource(name="staffInfoDAOImpl") var dao: StaffInfoDAOImpl = _
	private val log = LoggerFactory.getLogger(classOf[MongodbWriterPool])
}