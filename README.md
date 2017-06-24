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



### 导入数据

#### 导入用户数据
```
mongoimport -d test -c user2 --file user.json
mongoexport -d test -c user  -o user.json
```


#### 导入下游企业
``` mongoimport -d test -c downstreamCompany --file ./docs/downStreamCompany.json ```


```scala

trait Migration[A, B] {
   def apply(a: A): B
}

implicit class MigrationOps[A](a: A) {
  def migrateTo[B](implicit migration: Migration[A, B]) = migration(a)
}

case class From(a: Int, b: Int,         c: Option[Int], d: Int     )
case class To(          b: Option[Int], c:Int,          d: Int, e: Int)
val to = From(a = 1, b = 2, c = Some(3), d = 4).migrateTo[To]

val toExp = To(b = Some(2), c = 3, d = 4, e = 0)

```
