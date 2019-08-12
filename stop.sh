#!/bin/bash

PROJECTNAME=你的项目名称
pid=`ps -ef | grep $PROJECTNAME |grep -v "grep" |awk '{print $2}'`
if [ $pid ];then
   echo "$PROJECTNAME is running and pid=$pid"
   kill -9 $pid
   if [[ $? -eq 0 ]];then
      echo "success to stop $PROJECTNAME "
   else
      echo "fail to stop $PROJECTNAME "
   fi
fi