### 项目结构

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

- `@PathVariable`  映射url片段到java方法的参数

 		@GetMapping("/{id:\\d+}")
		public User getInfo(@PathVariable String id) {
			// do something...
		}

- 在url声明中使用正则表达式

		@GetMapping("/{id:\\d+}")

- `@JsonView`控制json输出内容

 	`@JsonView`使用步骤：

	（1）使用接口来声明多个视图；

		public class User {
		
		    public interface UserSimpleView {}
		    public interface UserDetailView extends UserSimpleView {}
		
		    private String username;
		    private String password;
		
			...
	
		}

	（2）在值对象的get方法上指定视图；

		@JsonView(UserSimpleView.class)
		public String getUsername() {
		    return username;
		}

	（3）在Controller方法上指定视图。

		@GetMapping("/{id:\\d+}")
		@JsonView(User.UserDetailView.class)
		public User getInfo(@PathVariable String id) {
		    User user = new User();
		    user.setUsername("tom");
		    return user;
		}


#### 处理创建请求

- `@RequestBody`映射请求体到java方法的参数

		@Test
		public void whenCreateSuccess() throws Exception {
		    Date date = new Date();
		    System.out.println(date.getTime());
		    String content = "{\"username\":\"tom\",\"password\":null,\"birthday\":"+ date.getTime() +"}";
		    String result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
		            .contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
		            .andExpect(MockMvcResultMatchers.status().isOk())
		            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
		            .andReturn().getResponse().getContentAsString();
		    System.out.println(result);
		
		}

		// 如果没有@RequestBody，将无法获取请求传来的参数，所有输出内容都为null。
		@PostMapping
	    public User create(@RequestBody User user) {
	
	        System.out.println(user.getId());
	        System.out.println(user.getUsername());
	        System.out.println(user.getPassword());
	        System.out.println(user.getBirthday());
	
	        user.setId(1);
	        return user;
	    }

- 日期类型参数的处理 ——传递时间戳

		public class User {
		    ...
		    private Date birthday;
			...
		}

		@PostMapping
	    public User create(@RequestBody User user) {
			...
	        System.out.println(user.getBirthday());  // 输出内容：1512115592801
			...
	    }

		@Test
		public void whenCreateSuccess() throws Exception {
		    Date date = new Date();
		    System.out.println(date.getTime());  // 输出内容：Fri Dec 01 16:06:32 CST 2017
		    String content = "{\"username\":\"tom\",\"password\":null,\"birthday\":"+ date.getTime() +"}";
		    String result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
		            .contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
		            .andExpect(MockMvcResultMatchers.status().isOk())
		            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
		            .andReturn().getResponse().getContentAsString();
		    System.out.println(result);  //  输出内容：{"id":1,"username":"tom","password":null,"birthday":1512115592801}
		
		}

- `@Valid`注解和`BindingResult`验证请求参数的合法性并处理校验结果

		public class User {
		    ...
		    @NotBlank  // 不为空校验
		    private String password;
		    ...
		}


		@PostMapping
	    public User create(@Valid @RequestBody User user, BindingResult errors) {
	        if (errors.hasErrors()) {
	            errors.getAllErrors()
	                    .stream()
	                    .forEach(error -> System.out.println(error.getDefaultMessage()));
	        }
			...
	    }


#### 开发用户信息修改和删除服务

- 常用的验证注解

<table border="1" cellspacing="0" cellpadding="0"> 
  <tbody> 
   <tr> 
    <td valign="top"> <p><strong>注解</strong></p> </td> 
    <td valign="top"> <p><strong>适用的数据类型</strong></p> </td> 
    <td valign="top"> <p><strong>说明</strong></p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@AssertFalse</strong></p> </td> 
    <td valign="top"> <p>Boolean, boolean</p> </td> 
    <td valign="top"> <p>验证注解的元素值是false</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@AssertTrue</strong></p> </td> 
    <td valign="top"> <p>Boolean, boolean</p> </td> 
    <td valign="top"> <p>验证注解的元素值是true</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@DecimalMax</strong><strong>（value=x）</strong></p> </td> 
    <td valign="top"> <p>BigDecimal, BigInteger, String, byte,short, int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of Number andCharSequence.</p> </td> 
    <td valign="top"> <p>验证注解的元素值小于等于@ DecimalMax指定的value值</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@DecimalMin</strong><strong>（value=x）</strong></p> </td> 
    <td valign="top"> <p>BigDecimal, BigInteger, String, byte,short, int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of Number andCharSequence.</p> </td> 
    <td valign="top"> <p>验证注解的元素值小于等于@ DecimalMin指定的value值</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Digits(integer=</strong><strong>整数位数, fraction=小数位数)</strong></p> </td> 
    <td valign="top"> <p>BigDecimal, BigInteger, String, byte,short, int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of Number andCharSequence.</p> </td> 
    <td valign="top"> <p>验证注解的元素值的整数位数和小数位数上限</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Future</strong></p> </td> 
    <td valign="top"> <p><a href="http://www.weare.net.cn/1/search.html?title=java" target="_blank">java</a>.util.Date, <a href="http://www.weare.net.cn/1/search.html?title=java" target="_blank">java</a>.util.Calendar; Additionally supported by HV, if the<a href="http://joda-time.sourceforge.net/" rel="external nofollow">Joda Time</a>&nbsp;date/time API is on the class path: any implementations ofReadablePartial andReadableInstant.</p> </td> 
    <td valign="top"> <p>验证注解的元素值（日期类型）比当前时间晚</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Max</strong><strong>（value=x）</strong></p> </td> 
    <td valign="top"> <p>BigDecimal, BigInteger, byte, short,int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type ofCharSequence (the numeric value represented by the character sequence is evaluated), any sub-type of Number.</p> </td> 
    <td valign="top"> <p>验证注解的元素值小于等于@Max指定的value值</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Min</strong><strong>（value=x）</strong></p> </td> 
    <td valign="top"> <p>BigDecimal, BigInteger, byte, short,int, long and the respective wrappers of the primitive types. Additionally supported by HV: any sub-type of CharSequence (the numeric value represented by the char sequence is evaluated), any sub-type of Number.</p> </td> 
    <td valign="top"> <p>验证注解的元素值大于等于@Min指定的value值</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@NotNull</strong></p> </td> 
    <td valign="top"> <p>Any type</p> </td> 
    <td valign="top"> <p>验证注解的元素值不是null</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Null</strong></p> </td> 
    <td valign="top"> <p>Any type</p> </td> 
    <td valign="top"> <p>验证注解的元素值是null</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Past</strong></p> </td> 
    <td valign="top"> <p><a href="http://www.weare.net.cn/1/search.html?title=java" target="_blank">java</a>.util.Date, <a href="http://www.weare.net.cn/1/search.html?title=java" target="_blank">java</a>.util.Calendar; Additionally supported by HV, if the<a href="http://joda-time.sourceforge.net/" rel="external nofollow">Joda Time</a>&nbsp;date/time API is on the class path: any implementations ofReadablePartial andReadableInstant.</p> </td> 
    <td valign="top"> <p>验证注解的元素值（日期类型）比当前时间早</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Pattern(regex=</strong><strong>正则表达式, flag=)</strong></p> </td> 
    <td valign="top"> <p>String. Additionally supported by HV: any sub-type of CharSequence.</p> </td> 
    <td valign="top"> <p>验证注解的元素值与指定的正则表达式匹配</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Size(min=</strong><strong>最小值, max=最大值)</strong></p> </td> 
    <td valign="top"> <p>String, Collection, Map and arrays. Additionally supported by HV: any sub-type of CharSequence.</p> </td> 
    <td valign="top"> <p>验证注解的元素值的在min和max（包含）指定区间之内，如字符长度、集合大小</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Valid</strong></p> </td> 
    <td valign="top"> <p>Any non-primitive type（引用类型）</p> </td> 
    <td valign="top"> <p>验证关联的对象，如账户对象里有一个订单对象，指定验证订单对象</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@NotEmpty</strong></p> </td> 
    <td valign="top"> <p><code>CharSequence</code>,<code>Collection</code>,&nbsp;<code>Map and Arrays</code></p> </td> 
    <td valign="top"> <p>验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0）</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Range(min=</strong><strong>最小值, max=最大值)</strong></p> </td> 
    <td valign="top"> <p><code>CharSequence, Collection, Map and Arrays,BigDecimal, BigInteger, CharSequence, byte, short, int, long and the respective wrappers of the primitive types</code></p> </td> 
    <td valign="top"> <p>验证注解的元素值在最小值和最大值之间</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@NotBlank</strong></p> </td> 
    <td valign="top"> <p><code>CharSequence</code><code></code></p> </td> 
    <td valign="top"> <p>验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Length(min=</strong><strong>下限, max=上限)</strong></p> </td> 
    <td valign="top"> <p><code>CharSequence</code></p> </td> 
    <td valign="top"> <p>验证注解的元素值长度在min和max区间内</p> </td> 
   </tr> 
   <tr> 
    <td valign="top"> <p><strong>@Email</strong></p> </td> 
    <td valign="top"> <p><code>CharSequence</code></p> </td> 
    <td valign="top"> <p>验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式</p> </td> 
   </tr> 
  </tbody> 
 </table>



- 自定义消息

		public class User {
		    ...
		    @NotBlank(message = "密码不能为空")  // 不为空校验
		    private String password;
		    @Past(message = "生日必须是过去的时间")  // 校验过去时间
		    private Date birthday;
			...
		}


- 自定义校验注解

		@Target({ElementType.METHOD, ElementType.FIELD})
		@Retention(RetentionPolicy.RUNTIME)
		@Constraint(validatedBy = MyConstraintValidator.class)
		public @interface MyConstraint {
		
		    String message();
		
		    Class<?>[] groups() default { };
		
		    Class<? extends Payload>[] payload() default { };
		
		}


		public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {
		
			@Autowired
			private HelloService helloService;
		
			@Override
			public void initialize(MyConstraint constraintAnnotation) {
				System.out.println("my validator init");
			}
		
			@Override
			public boolean isValid(Object value, ConstraintValidatorContext context) {
				helloService.greeting("tom");
				System.out.println(value);
				return false;
			}
		
		}

		public class User {
		    ...
		    @MyConstraint(message = "测试自定义验证注解")
		    private String username;
			...
		}
		
		
#### 服务异常处理

- 异步处理 REST 服务

spring boot 默认的错误处理机制：

    resources/resources/error/404.html
       
自定义异常处理：

(1) `exception/UserNotExitException.java`

    package site.zongjian.exception;
    
    public class UserNotExitException extends RuntimeException {
    
        private static final long serialVersionUID = -3727308783370998561L;
    
        private String id;
    
        public UserNotExitException(String id) {
            super("用户不存在！");
            this.id = id;
        }
    
    
        public String getId() {
            return id;
        }
    
        public void setId(String id) {
            this.id = id;
        }
    }

(2) `controller/ControllerExceptionHandler.java`
    
    @ControllerAdvice
    public class ControllerExceptionHandler {
    
        @ExceptionHandler(UserNotExitException.class)
        @ResponseBody
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 状态码500
        public Map<String, Object> handleUserNotExistException(UserNotExitException ex) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", ex.getId());
            result.put("message", ex.getMessage());
            return result;
        }
    }
    
(3) `controller/UserController.java` 中处理异常：

    throw new UserNotExitException(id);
    

### 使用切片拦截REST服务

- 过滤器（Filter）

- 拦截器（Interceptor）

    @Configuration
    public class WebConfig extends WebMvcConfigurerAdapter {
    
        @Autowired
        private TimeInterceptor timeInterceptor;
    
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(timeInterceptor);
        }
    
        ...
        
    }
        

- 切片（Aspect）