running instructions


Normal execution:


Directory from where you will run commands is the “src” directory in the project.
   Example = C:\Users\malya\Scalable\Scalable4\src>

1.  Start multiple server by using below command in different terminals

    C:\Users\malya\Scalable\Scalable4\src> java PaxosCoordinator localhost 1111

    C:\Users\malya\Scalable\Scalable4\src> java PaxosCoordinator localhost 1112

    C:\Users\malya\Scalable\Scalable4\src> java PaxosCoordinator localhost 1113

    C:\Users\malya\Scalable\Scalable4\src> java PaxosCoordinator localhost 1114

    C:\Users\malya\Scalable\Scalable4\src> java PaxosCoordinator localhost 1115

2. Now start one or more clients and connect to the available servers.

   C:\Users\malya\Scalable\Scalable4\src>java Client localhost
   2022-12-07T15:08:21.033 : Enter n no of servers available for client to connect at this IP address
   5
   2022-12-07T15:08:22.611 : Enter 5 of server port nos
   1111
   1112
   1113
   1114
   1115
   We have 5 machines, enter one machine port address from above to connect
   1111
   Connected to the server on port  1111

   u can any start any no of clients based on the request.

3. do any transactions.


                         ********************************************************



Running On docker instructions :

1. create a network first
   --> docker network create <NETWORK_NAME>
   --> docker network create project

2. first create server docker image by running below command (here we provided server as docker image name)

   --> docker build -t <IMAGE_NAME> --target server-build .
   --> docker build -t server --target server-build .

3. next create client docker image by running below command (here we provided client as docker image name)

   --> docker build -t <IMAGE_NAME> --target server-build .
   --> docker build -t client --target client-build .

4. Now start server and client docket containers

   (for server)
   --> docker run -it --rm --name server --network project <docker image id of server>
   --> docker run -it --rm --name server --network project 0690ac30272c

   and exec commands on docker container to start some servers like below
   --> docker exec -it d2248cfc5ced /bin/sh -c "java PaxosCoordinator 1111 & java PaxosCoordinator 1112 & java PaxosCoordinator 1113 & java PaxosCoordinator 1114 & java PaxosCoordinator 1115"
   --> d2248cfc5ced - container id

5. now you see 4 servers started by using different ports

6. now start a client to connect to any one of the available servers by using below command

        ( docker run -it --rm --name client --network project <client docker image id> java Client <server ip address> )
   -->  docker run -it --rm --name client --network project eb5c53604f5f java Client 48632e3a284c

7. here sometime the client cannot able to find host name by its address (may be the network or we need to populate the ipaddress and hostname in address)
   but over functionality is fine.



















Notes:

1. check all server logs to check what background operations are done.
