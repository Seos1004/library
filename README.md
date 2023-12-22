# Library
>Springboot 3.2.0  
>java 17  
>mybatis
-----
## h2(in memory)
>http://localhost:8080/h2-console

## swagger
>http://localhost:8080/api-docs

## redis
>Active profiles local설정 必

## kafka
주키퍼를 먼저 실행 해야함
>주키퍼 서버 실행
>>C:\\dev\\kafka\\kafka_2.13-3.6.0\\bin\\windows\\zookeeper-server-start.bat C:\\dev\\kafka\\kafka_2.13-3.6.0\\config\\zookeeper.properties

>카프카 서버 실행 
>>C:\dev\kafka\kafka_2.13-3.6.0\bin\windows\kafka-server-start.bat C:\\dev\\kafka\\kafka_2.13-3.6.0\\config\\server.properties      

----
예정
1. 카프카 수동커밋(offset)
2. 실시간 스트리밍
3. ELK


