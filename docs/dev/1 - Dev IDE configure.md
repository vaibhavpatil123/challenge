# Dev guide

## IDE configure checkstyle for code 
Install code style plugin - https://plugins.jetbrains.com/plugin/1065-checkstyle-idea

## IDE configure findbugs plugin
https://plugins.jetbrains.com/plugin/14014-spotbugs

## IDE configure auto code formatter which will fix lots of checkstyle issues
Formatted by Jindent --- http://www.jindent.com

## IDE configure Sonar Lint 
1. option in DEV mode 
https://plugins.jetbrains.com/plugin/7973-sonarlint
2. we can integrate it with centralised development service and put al configs/ rule file at single place
docker run -d --name sonarqube -p 9000:9000 sonarqube
Refer 
https://yashints.dev/blog/2020/02/23/sonarqube-docker  



