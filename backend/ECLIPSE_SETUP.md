# Eclipse 配置指南

## Lombok 在 Eclipse 中的配置步骤

### 1. 安装 Lombok 插件
1. 下载 Lombok jar包：
   ```bash
   # 从Maven仓库下载
   mvn dependency:copy -Dartifact=org.projectlombok:lombok:1.18.30 -DoutputDirectory=.
   ```

2. 运行安装程序：
   ```bash
   java -jar lombok-1.18.30.jar
   ```
   或者手动安装：
   - 双击运行 lombok-1.18.30.jar
   - 选择你的 Eclipse 安装目录
   - 点击 "Install/Update"
   - 重启 Eclipse

### 2. Eclipse 项目配置
1. 右键项目 → Properties → Java Build Path
2. 确认 Lombok 在 Libraries 中
3. 检查 Annotation Processing：
   - Properties → Java Compiler → Annotation Processing
   - 确保 "Enable annotation processing" 已勾选

### 3. 验证 Lombok 工作
在 Eclipse 中打开任意实体类（如 `CycleRecord.java`），确认：
- `@Data` 注解生成的 getter/setter 方法可用
- 无 "Lombok annotation not found" 错误

### 4. 常见问题解决
- **清理项目**：Project → Clean → Clean all projects
- **更新Maven**：右键项目 → Maven → Update Project
- **重启Eclipse**：确保Lombok插件生效

### 5. 验证命令
```bash
cd backend
mvn clean compile  # 应该成功
mvn test           # 应该通过
```

如果仍有问题，请检查：
1. Eclipse 版本是否兼容 Lombok
2. Java 版本是否为 17
3. 项目是否正确识别为 Maven 项目