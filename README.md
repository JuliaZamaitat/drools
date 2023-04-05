Docker image used for the MySQL Database:

docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -p 3306:3306 -d mysql:8.0.28


