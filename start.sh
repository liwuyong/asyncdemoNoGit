#!/bin/bash
PROJECTNAME=你的项目名称
pid=`ps -ef |grep $PROJECTNAME |grep -v "grep" |awk '{print $2}'`

if [ $pid ];then
   echo "$PROJECTNAME is running and pid=$pid"
else
   echo "Start success to start $PROJECTNAME ..."

   nohup java -jar 项目名称.jar >> catalina.out 2>&1 &

fi