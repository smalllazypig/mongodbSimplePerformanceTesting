mvn clean

echo ~~~~~~~~~~~~~clean finished!~~~~~~~~~~~~~~~~

mvn install -Dmaven.test.skip=true

echo ~~~~~~~~~~~~~install finished!~~~~~~~~~~~~~~~~

mvn glassfish:undeploy

echo ~~~~~~~~~~~~~undeploy finished!~~~~~~~~~~~~~~~~

mvn glassfish:deploy


