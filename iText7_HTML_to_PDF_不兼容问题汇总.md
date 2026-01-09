# iText7 / HTML 转 PDF 不兼容问题汇总文档

> 本文档基于项目实际开发与问题排查总结，用于指导 **HTML → iText7 PDF** 开发。
> 适用于：前端开发人员

---

## 布局相关不兼容

### ❌ 完全不支持
- display: grid
- position: fixed
- position: sticky
- flex-wrap
- gap
- calc()
- vw / vh
- justify-content: space-between 
- justify-content: space-around
- justify-content: space-evenly
- flex: 1;


### ⚠️ 部分支持（不稳定）
- display: flex（仅横向，不可换行）
- z-index


### ✅ 推荐
- display: block
- display: inline-block
- float + clear
- 固定宽高布局


---

## 字体 & 字重问题

### ❌ 问题现象
- 字体 OSS 链接部分环境失效

### ❌ 不推荐
- 依赖系统字体
- 浏览器字体回退

### ✅ 建议
- 后端注册字体
- HTML 明确指定 font-family

---

## 图片 & 圆角问题

### ❌ 问题
- border-radius 在 PDF 中失效
- object-fit 不生效
- z-index 层级错乱

### ✅ 建议
- 后端裁剪图片
- 使用背景图片方式
- 避免圆角头像
- 使用固定尺寸 img

---

## 总结

❗ iText7 = **排版引擎，不是浏览器**  
❗ 能在 Chrome 显示 ≠ 能生成 PDF  
❗ 页面结构 > CSS 技巧  
❗ 慎用flex布局

> 所有 PDF 页面请以 **“最原始 HTML + 最基础 CSS”** 为开发目标。
