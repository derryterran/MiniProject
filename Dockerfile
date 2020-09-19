FROM tomcat:9.0.37
ADD target/MiniProject.war /usr/local/tomcat/webapps
EXPOSE 8085
CMD ["catalina.sh", "run"]