/* The purpose of the script to collect jvm heap space
   data and display those in a format usable from Cacti
   monitoring tool.
   This script reads the following jvm values
   Heap Used, Non Heap,Max Heap,Heap Memory Usage
   and prints those in a title:value format.

   Input : hostname,port, username,password
*/

#!/usr/bin/env groovy
import javax.management.ObjectName
import javax.management.remote.JMXConnectorFactory as JmxFactory
import javax.management.remote.JMXServiceURL as JmxUrl
import javax.management.remote.*


def hostname = this.args[0]
def port = this.args[1]
def serverUrl = 'service:jmx:rmi:///jndi/rmi://' + hostname + ":" + port + '/jmxrmi'

def jmxEnv = null
if (this.args.size() > 3 ) {
	def username = this.args[2]
	def pass = this.args[3]

	jmxEnv = [(JMXConnector.CREDENTIALS): (String[])[username, pass]]
}

def server = JmxFactory.connect(new JmxUrl(serverUrl),jmxEnv).MBeanServerConnection
def query = new ObjectName('java.lang:type=Memory')


String[] allNames = server.queryNames(query, null)
if (allNames.size()==1) {
        mem = new GroovyMBean(server, allNames[0])

        // force a single output puffer
        String output = "heapused:" + mem.HeapMemoryUsage.used + " nonheap:" + mem.NonHeapMemoryUsage.used+" maxheap:" + mem.HeapMemoryUsage.max
		print output
}





