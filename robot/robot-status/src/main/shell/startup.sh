#!/bin/sh
#-------------------------------------------------------------------------------------------------------------

[ -z "$STATUS_ROBOT_HOME" ] && STATUS_ROBOT_HOME="$(cd "$(dirname "$0")"; pwd)/.."

#if [ -r "$STATUS_ROBOT_HOME/bin/setenv.sh" ]; then
#  . "$STATUS_ROBOT_HOME/bin/setenv.sh"
#fi

JAVA_OPTS="-Duser.timezone=GMT+8 -server -Xms1024m -Xmx1024m -Xloggc:$STATUS_ROBOT_HOME/log/gc.log"
STATUS_ROBOT_INSTANCE_NAME="talaris-robot-status-1.0"
STATUS_ROBOT_LOG="$STATUS_ROBOT_HOME/log/$STATUS_ROBOT_INSTANCE_NAME.log"
StatusRobotPID=0

getStatusRobotPID(){
    javaps=`$JAVA_HOME/bin/jps -lv | grep $STATUS_ROBOT_INSTANCE_NAME`
    if [ -n "$javaps" ]; then
        StatusRobotPID=`echo $javaps | awk '{print $1}'`
    else
        StatusRobotPID=0
    fi
}

startup(){
    getStatusRobotPID
    echo $STATUS_ROBOT_LOG
    echo "================================================================================================================"
    if [ $StatusRobotPID -ne 0 ]; then
        echo "$STATUS_ROBOT_HOME already started(PID=$StatusRobotPID)"
        echo "================================================================================================================"
    else
        echo -n "Starting $STATUS_ROBOT_INSTANCE_NAME"
        nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar "$STATUS_ROBOT_HOME/lib/$STATUS_ROBOT_INSTANCE_NAME.jar" > $STATUS_ROBOT_LOG &
        getStatusRobotPID
        if [ $StatusRobotPID -ne 0 ]; then
            echo "(PID=$StatusRobotPID)...[Success]"
            echo "================================================================================================================"
        else
            echo "[Failed]"
            echo "================================================================================================================"
        fi
    fi
}

startup
