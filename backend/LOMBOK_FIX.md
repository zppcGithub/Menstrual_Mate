# Eclipse Lombok 问题解决

## 问题描述
Eclipse中Lombok注解（@Data、@Getter、@Setter）不起作用，导致编译错误

## 解决方案

### 方法1：手动安装Lombok到Eclipse
1. 下载Lombok jar包：
   ```bash
   mvn dependency:copy -Dartifact=org.projectlombok:lombok:1.18.30 -DoutputDirectory=.
   ```

2. 运行安装器：
   ```bash
   java -jar lombok-1.18.30.jar
   ```
   选择Eclipse安装目录，点击安装

3. 重启Eclipse

### 方法2：使用Maven命令行测试
如果Eclipse配置有问题，可以先用命令行验证：
```bash
cd backend
mvn clean compile
mvn test
```

### 方法3：临时解决方案
如果Lombok仍然有问题，可以临时在实体类添加构造函数：

#### 为User.java添加：
```java
// 临时添加构造器和getter/setter
public User() {}
public User(String username) {
    this.username = username;
}
// 手动添加所有getter/setter方法
```

## 验证步骤
1. **Eclipse重启**：安装Lombok后必须重启Eclipse
2. **项目清理**：Project → Clean → Clean all projects
3. **Maven更新**：右键项目 → Maven → Update Project
4. **测试编译**：mvn clean compile

## 最终解决方案
如果仍然有问题，建议：
1. 使用IntelliJ IDEA（对Lombok支持更好）
2. 或者手动添加getter/setter方法（作为最后手段）