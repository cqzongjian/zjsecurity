###项目结构

	zjsecurity:主模块
	│  
	├─zjsecurity-core:核心业务逻辑
	│              
	├─zjsecurity-browser:浏览器安全特定代码
	│              
	├─zjsecurity-app:app相关特定代码
	│              
	└─zjsecurity-demo:样例程序

### maven打包

在demo的pom.xml中添加

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>1.3.3.RELEASE</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <finalName>demo</finalName>
    </build>

然后，在命令行执行打包命令：

    mvn package
    
生成demo.jar文件。在命令行中执行：

    java -jar demo.jar


### 使用Spring MVC开发RESTful API

	- 查询  /user/query?name=zongjian    GET     /user?name=zongjian  GET
	- 详情  /user/getInfo?id=1           GET     /user/1              GET
	- 创建  /user/create?name=zongjian   POST    /user                POST
	- 修改  /user/update?id=1&name=jack  POST    /user/1              PUT
	- 删除  /user/delete?id=1            GET     /user/1              DELETE

#### 编写查询请求

常用注释

	@RestController  标明此Controller提供RestAPI

	@RequestMapping及其变体  映射http请求url到java方法

	@RequestParam  映射请求参数到java方法的参数

	@PageableDefault  指定分页参数默认值

[JsonPath 参考文档](https://github.com/json-path/JsonPath "JsonPath")

#### 编写用户详情服务

- @PathVariable  映射url片段到java方法的参数

 		@GetMapping("/{id:\\d+}")
		public User getInfo(@PathVariable String id) {
			// do something...
		}

- 在url声明中使用正则表达式

		@GetMapping("/{id:\\d+}")

- @JsonView控制json输出内容

> 	@JsonView使用步骤：
> 		（1）使用接口来声明多个视图；
> 		（2）在值对象的get方法上指定视图；
> 		（3）在Controller方法上指定视图。

