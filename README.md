## 环境搭建


### 安装scala 和 sbt 

``` brew install scala@2.11 sbt ```

### 后端接口自动化测试
```
sudo cpan install JSON
sudo cpan install UUID
```

### 安装 数据库 mongodb
```brew install mongodb```
 
### 安装 数据库 redis
```brew install redis```



### 运行项目 sbt run

编辑  ~/.sbt/repositories 加入以下内容

```
 [repositories]
 
   repox-maven:  http://repox.gtan.com:8078/
   repox-ivy:  http://repox.gtan.com:8078/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]

```



### 前端环境 

进入frontend-admin 目录后 运行 ```npm install ```, 然后运行 ``` npm start ```

### 前端环境编译

``` npm run build ```


### 导入数据

#### 导入用户数据
```
mongoimport -d test -c user2 --file user.json
mongoexport -d test -c user  -o user.json
```


#### 导入下游企业
``` mongoimport -d test -c downstreamCompany --file ./docs/downStreamCompany.json ```
