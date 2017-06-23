## 接口文档

### 可以使用 PAW 软件替代 POSTMAN 发请求, 用 PAW 打开 paw 文件夹下配置文件

###  文档格式

文档使用 [apiblueprint] 格式，类似Markdown。具体用法参考[文档](https://apiblueprint.org/documentation/tutorial.html)

###  文档生成 HTML

官方提供多种生成HTML的[工具](https://apiblueprint.org/tools.html#renderers)
例如 Go语言开发的[snowboard](https://github.com/subosito/snowboard)


这里使用 nodejs开发的 [aglio](https://github.com/danielgtaylor/aglio) 生成

#### 安装 aglio 

```npm install -g aglio ``` 

或在 docs目录下 运行 ``` npm install```

#### 生成 HTML

在 docs目录下 运行 ```npm start ``` 然后查看 page.html 为页面路由，api.html和api2.html为API文档。

#### 实时预览服务器

在 docs目录下 运行 ```npm run server ```  后，在http://localhost:3000/ 开启实时预览



[apiblueprint]: https://apiblueprint.org/



### 阿里云OSS上传
```
{
    "policy":"xxxxxx" ,
    "callback":"xxxx",
    "signature":"xxxa",
    "OSSAccessKeyId":"xxxx",
    "file":"文件域"
}

```
           