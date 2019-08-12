#!/bin/sh
JAVA_HOME=/usr/local/java/jdk1.8.0_161
WEB_HOME=/home/testdeadlock/asyncdemoNoGit
jstack="$JAVA_HOME/bin/jstack"
processId=$1
#如果没有输入进程ID，提示用户输入
if [ ! -n "$processId" ] ;then
    echo "please input a java processId "
	read processId
fi
echo $processId
#如果是标准输出则变成红色，否则正常输出
redEcho(){
   if [ -c /dev/stdout ] ; then
       echo -e "\033[1;31m$@\033[0m"
   else
       echo "$@"
   fi
}
showProcessAllthreads(){
   while read eachLine ; do
      threadId=`echo $eachLine | awk '{print $1}'`
      threadId0x=`printf %x ${threadId}`
	  pcpu=`echo ${eachLine} | awk '{print $2}'`
      totalTime=`echo ${eachLine} | awk '{print $3}'`
      #存储文件
      jstackFile=/tmp/${uniqueId}_${processId}
	  sudo -u root $jstack ${processId} > ${jstackFile} || {
	      redEcho "failed to use jstack commond on java process ${processId}"
		  rm -rf ${jstackFile}
		  continue
	  }
      redEcho "java processId is [${processId}] : current thread is(${threadId}/${threadId0x}) : cost total process time is [${totalTime}] : use total cup precent is [${pcpu}]"
      sed "/nid=0x${threadId0x}/,/^$/p" -n ${jstackFile}
   done
}
#为当前的程序的文件生成一个唯一的地址(时间+随机数+进程号(注意这个进程号是当前程序的进程号，不是java城市的进程号))
uniqueId=`date +%s`_${RANDOM}_$$
echo $uniqueId
top -p $processId -H -n 1 -b | sed -n "8,1000p" | awk '{print $1,$9,$11}' | showProcessAllthreads
rm -rf /tmp/${uniqueId}