# Spring IOC简易实现与理解

代码地址：https://github.com/dyywinner/MySpringIOC

- IOC是一种程序设计思想，是反射的良好工业应用，带IOC的开发框架有以下几点好处：

    1. 减少重复代码，更多关心程序核心设计
    2. 更好地避免多线程带来的问题
    3. 更方便地管理对象，有效减少频繁GC，提升程序性能
    4. 统一开发的设计模式

    

- 以下用经典的echo操作为例，对比OOP与IOC

## 没有IOC之前的普通OOP设计模式

通过对象之间的关联、依赖、聚合、组合，组成一个耦合的echo程序。

- IMessageService接口

    ```
    public interface IMessageService {

    	public String echo(String message);

    }
    ```

  

- MessageServiceImpl实现类

    ```
    public class MessageServiceImpl implements IMessageService {

        @Override
        public String echo(String message){
            return message;
        }

    }

    ```

- Main主函数

  ```
  public class Main {
      public static void main(String[] args){
          // 通过new关键字实例化IMessageService对象
          IMessageService messageService = new MessageServiceImpl();
          System.out.println(messageService.echo("I am echo from OOP"));
      }
  }
  ```

- 最后输出

  ```
  I am echo from OOP
  ```

  这就是OOP的设计思路，接口实现，实现类组合进client的调用方法里，是一种耦合的设计思路。

##  拥有IOC后的设计思路

OOP的设计思路有诸多缺陷，比如new对象的耦合性，赋值的各种重复代码，各种依赖处理。以下通过一个个问题去改进去解决。

1. **问题**：不想通过new得到实例对象，需要解耦
	
	```
	IMessageService messageService = new MessageServiceImpl();
	```
   
	- **改进**：工厂设计模式，设计时考虑为所有接口对象服务，所以工厂FactoryBean类最佳实践是结合使用反射与泛型。通过另一个client使用FactoryBean得到Service接口的实例化对象。
```
     public FactoryBean() {}
       
        public static <T> T getInstance(Class<?> clazz){
            try {
                return (T) clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
```

2. **问题**: 通过工厂设计可以隐藏具体实现子类，但是带来了新问题，每次都需要传递具体子类代码。我们不想传递具体子类的Class对象, 因为传递Object对象也是一种耦合。

 ```
    // 硬编码的得到具体实现类
    IMessageService messageService = FactoryBean.getInstance(MessageServiceImpl.class);
 ```
- **改进**：借助文件形式完成，比如应"*.ini"文件格式简易实现代码解. 文件分为两部分实力定义[ioc]和关联[di]

  1. 将对象名与对象类从ini文件读取并放入LinkedHashMap


      private void readMapObject(String context){
              switch (this.flag){
                  case "[ioc]":{
                      String res[] = context.split("=");
                      try{
                      	// res[0]是类名; res[1]是类的包位置,将其转换为Class对象
                          this.Objectmap.put(res[0], Class.forName(res[1]));
                      } catch (ClassNotFoundException e) {
                          e.printStackTrace();
                      }
                      break;
                  }
              }
      }

  





  2. FactoryBean可以改进为: 通过beanName直接获取实例化对象

  ```
  public class FactoryBean {
  	// 读取ini文件
      private static iniFileReader inireader = new iniFileReader();
  
      public FactoryBean() {}
  
      public static <T> T getInstance(String beanName){
          try {
              // 通过beanName直接获取实例化对象
              return (T) inireader.getObjectMap().get(beanName);
          } catch (Exception e) {
              return null;
          }
      }
  }
  ```

- 这样就完成了通过bean名称获得Bean对象, 在整个程序中进行Bean对象的HashMap加载.
- 其中工厂类有一大创新: 工厂类不传递Object Class,而是通过String beanName 查找到类.
- 想一想为什么要用LinkedHashMap<String, Class<?>>
- **Note**: 解决了问题1与2后，**加载类不再需要具体的子类，这时的程序已经没有耦合性，达到了解耦的小目标**。但是同时带来了设计单例模式的应用问题，每次加载都会重新加载一个新的bean，这会不会有问题？


​    

3. **问题**：FactoryBean生成的对象一多，就会影响GC性能。

    ```
    // 定义两个简单java类DEPT与EMP
    // 其中emp依赖depy
    public class DEPT implements Serializable {
        private Long deptno;
        private String dname;
    }
    
    public class EMP implements Serializable {
        private Long empno;
        private String ename;
        private dept dept;
    }
    
    ```

    

    - **改进**：将Bean做成单例。加入DI类关联机制，以[di]为ini文件中的标志

      - ioc.ini

      ```
      [ioc]
      msgecho=com.ioc.service.impl.MessageServiceImpl
      emp=com.ioc.example.EMP
      dept=com.ioc.example.DEPT
      [field]
      emp.empno=100
      emp.ename=Smith
      dept.deptno=10
      dept.dname=Account
      [di]
      emp.dept=dept
      ```

      

      - 定义专属Bean类配置操作类，获取对象，配置对象属性，其中属性需要接口标准

      ```
      public class BeanConfig {
      
          private BeanConfig(){}
          public static void configFiled(Object obj, String fieldName, Object val){
              try {
                  Field field = obj.getClass().getDeclaredField(fieldName);
                  field.setAccessible(true);
                  // 赋值与类型匹配
                  if (field.getType() == Byte.class){
                      if(val instanceof String){
                          field.set(obj, Byte.parseByte(val.toString()));
                      }
                  }else if(field.getType() == String.class) {
                          if(val instanceof String){
                              field.set(obj, val);
                          }
                  // 基本数据类型外即使Object类
                  }else {
                      field.set(obj, val);
                  }
              }catch (NoSuchFieldException e ){
                  e.printStackTrace();
              }catch (IllegalAccessException e){
                  e.printStackTrace();
              }
          }
      }
      
      ```
      
      - 在iniFileReader类中加入处理[di]的逻辑，将依赖对象从LinkedHashMap中拿出赋予给类对象属性
      ```
      public class iniFileReader {    
        public static String File_Name = "ioc.ini";
        public static String Base_Dir = System.getProperty("user.dir");
        public static String Sub_Path = "src\\com\\resources\\" + File_Name;
        public Map<String, Object> Objectmap = new LinkedHashMap<>();
      private String flag = null;
      
        public iniFileReader() {
            this.readIni();
      }
      
        public Map<String, Object> getObjectMap(){
            return Objectmap;
      }
      
        public void readIni(){
            File file = new File(Base_Dir, Sub_Path);
            //System.getProperties().list(System.out);
            //System.out.println(file.isFile());
            try {
                Scanner scan = new Scanner(file);
                while (scan.hasNext()){
                    String context = scan.next();
              //System.out.println(context);
                    // read "[]"
                    if(context.matches("\\[.+\\]")){
                      if("[ioc]".equalsIgnoreCase(context)){
                            flag = "[ioc]";
                        }else if("[field]".equalsIgnoreCase(context)){
                            flag = "[field]";
                        }else if("[di]".equalsIgnoreCase(context)){
                          flag = "[di]";
                  }
                    }else{
                        this.readMapObject(context);
                  }
          }
            } catch (FileNotFoundException e){
                e.printStackTrace();
          }
}
      
        private void readMapObject(String context){
            switch (this.flag){
                case "[ioc]":{
                    String res[] = context.split("=");
                    try{
                        this.Objectmap.put(res[0], Class.forName(res[1]).getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "[field]":{
                    String res[] = context.split("=");
                    String objectInfo[]  = res[0].split("\\.");
                    Object obj = this.Objectmap.get(objectInfo[0]);
                    if(obj != null){
                        try{
                            BeanConfig.configFiled(obj, objectInfo[1], res[1]);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
              case "[di]":{
              String res[] = context.split("=");
                    String objectInfo[]  = res[0].split("\\.");
                    Object obj = this.Objectmap.get(objectInfo[0]);
                    if(obj != null){
                        try{
                        	  // 与[field]唯一不同的是 this.Objectmap.get(res[1]))
                            BeanConfig.configFiled(obj, objectInfo[1], this.Objectmap.get(res[1]));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
      
      ```



## 总结

最后开始测试：

```
public class TestObject {
    public static void main(String[] args){
        //DEPT dept = FactoryBean.getInstance("dept");
        EMP emp = FactoryBean.getInstance("emp");
        //System.out.println(dept);
        System.out.println(emp);
        //System.out.println(dept.getDeptno());
        //System.out.println(dept.getDname());
        System.out.println(emp.getEname());
        System.out.println(emp.getEmpno());
        System.out.println(emp.getDept());
    }
}
```

得到结果：

```
com.ioc.example.EMP@39aeed2f
Smith
100
com.ioc.example.DEPT@724af044
```

DEPT被赋予给EMP并且是单例的，解决了依赖问题。

最后，这次简单的Spring IOC实现主要用到了java的反射机制，文件IO流读取，仅用这两项技术就实现了传统OOP做不到了解耦性。

还有很多可以改进的地方：

1. 用*.ini文件读取配置比较低效，仅仅是实惠简单方便少许行数配置的操作，配置一复杂，还是用xml技术比较好。

2. 用配置[di]解决单例依赖的方案，可以用设计一个earlyExposeSingletonPool解决依赖问题的方案替代。

代码地址：https://github.com/dyywinner/MySpringIOC
