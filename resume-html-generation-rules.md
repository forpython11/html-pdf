# 简历 HTML 生成规则

## 目标

用于生成 Wisematch 简历模板 HTML。

生成出来的 HTML 需要同时满足以下要求：

- 字段定义严格以 `/Users/a1234/Downloads/html-pdf/resume-template-new.html` 为准
- 模板语法使用 `Thymeleaf`
- 适配 HTML 转 PDF 场景
- 尽量兼容本地直接打开 HTML 预览
- 可以根据不同设计稿变换视觉样式，但不能随意新增必填字段

---

## 基础约束

### 1. 字段来源

所有字段绑定必须以 `resume-template-new.html` 为准，不允许擅自引入新的强依赖字段。

允许使用的主要字段：

- `avatarUrl`
- `resume.brief.userName`
- `resume.tele`
- `resume.vxNo`
- `resume.base`
- `resume.brief.salary`
- `resume.email`
- `resume.brief.skills`
- `resume.brief.userBrief`
- `resume.workExperience`
- `resume.projectExperience`
- `resume.eduExperience`
- `resume.schoolExperience`
- `resume.extraInfo`

列表项字段：

- `work.name`
- `work.label`
- `work.startDate`
- `work.endDate`
- `work.content`
- `project.name`
- `project.label`
- `project.startDate`
- `project.endDate`
- `project.content`
- `edu.name`
- `edu.label`
- `edu.degree`
- `edu.startDate`
- `edu.endDate`
- `edu.content`
- `school.name`
- `school.label`
- `school.startDate`
- `school.endDate`
- `school.content`

### 2. 不允许新增的强依赖字段

以下内容如果设计稿里出现，默认不能直接新增字段依赖：

- 二维码图片字段
- 作品集图片字段
- MBTI 独立字段
- 独立学校字段
- 独立毕业时间字段
- 独立求职意向字段

如果确实需要展示：

- 优先从现有字段推导
- 或做静态文案/安全占位
- 不能因为设计稿新增后端必须返回的新字段

---

## 渲染规范

### 3. 模板引擎

HTML 必须使用 `Thymeleaf` 写法，例如：

- `th:text`
- `th:if`
- `th:each`
- `th:style`

### 4. 判空规则

字段判空风格尽量与 `resume-template-new.html` 保持一致。

示例：

```html
<span th:text="${resume.tele != null ? resume.tele : '未填写'}">13800138000</span>
```

列表判空：

```html
<div th:if="${not #lists.isEmpty(resume.workExperience)}">
```

城市字段如果沿用 `resume.base`：

```html
<span th:if="${resume.base == null || resume.base.isEmpty()}">未填写</span>
<span th:each="item : ${resume.base}" th:text="${item}"></span>
```

### 5. 默认值规则

没有数据时优先使用以下占位文案：

- `未填写`
- `未填写职位`
- `未填写项目名称`
- `未填写学校名称`
- `未填写工作内容`
- `未填写项目内容`

默认值风格应尽量复用 `resume-template-new.html`，不要混用一套 `--`、一套 `未填写`，除非是纯视觉占位。

---

## 结构规范

### 6. 页面结构

模板允许根据设计稿调整视觉布局，但建议保持以下信息覆盖完整：

- 头部信息
- 掌握技能
- 个人优势
- 工作经历
- 项目经历
- 教育经历
- 在校经历
- 附加信息

### 7. 推荐头部字段

头部优先展示这些字段：

- 头像
- 姓名
- 电话
- 微信号
- 城市
- 期望薪资
- 邮箱

以下字段如果设计需要展示，但原模板没有稳定绑定方式，要谨慎处理：

- 求职意向
- 学校
- 毕业时间

处理原则：

- 求职意向：原模板只有文案“求职意向:”，没有稳定绑定值，不要擅自绑定新字段
- 学校：如设计必须展示，可从 `resume.eduExperience[0].name` 推导，但要说明这是推导值
- 毕业时间：原模板当前未实际绑定字段，默认不要强依赖新字段

### 8. 模块顺序

如果没有特别要求，推荐按以下顺序输出：

1. 掌握技能
2. 个人优势
3. 工作经历
4. 项目经历
5. 教育经历
6. 在校经历
7. 附加信息

如果是多页模板，可以按设计稿拆分，但字段集合仍然要覆盖上面这些内容。

---

## PDF 兼容规范

### 9. 页面尺寸

必须考虑 PDF 输出场景。

建议保留：

```css
@page {
    size: A4;
    margin: 0;
}
```

### 9.1 必须叠加 iText7 兼容限制

生成模板时，除了视觉规则外，还必须同时遵守 `/Users/a1234/Downloads/html-pdf/iText7_HTML_to_PDF_不兼容问题汇总.md` 中的限制。

也就是说，设计稿可以参考，但最终 HTML 必须优先服从 iText7 可落地性，而不是浏览器效果。

### 9.2 明确禁止使用的高风险写法

以下写法默认不要出现在新模板里：

- `display: grid`
- `position: fixed`
- `position: sticky`
- `flex-wrap`
- `gap`
- `calc()`
- `vw`
- `vh`
- `justify-content: space-between`
- `justify-content: space-around`
- `justify-content: space-evenly`
- `flex: 1`

如果必须横向排布，优先考虑：

- `display: inline-block`
- `float`
- 固定宽度块布局

`display: flex` 只能在非常简单、单行、不换行的场景谨慎使用，不能依赖其复杂对齐能力。

### 9.3 图片与圆角限制

图片相关默认遵守以下策略：

- 不依赖 `object-fit`
- 不依赖头像圆角在 PDF 中精确还原
- 不依赖 `z-index` 做复杂覆盖
- 头像优先使用固定尺寸容器 + `background-image`
- 如设计稿是圆形头像，生成模板时应允许视觉近似，不要为了追求 100% 圆形效果引入高风险 CSS

### 9.4 布局优先级

在 HTML 转 PDF 场景里，优先级必须是：

1. 字段正确
2. 结构稳定
3. 分页可控
4. 视觉接近设计稿

不能为了更像设计稿，牺牲字段约束和 PDF 兼容性。

### 10. 中文排版

正文建议保留以下能力：

- 中文自动换行
- 长文本不断布局
- PDF 中尽量避免标点落行异常

推荐写法：

```css
.content-text {
    word-break: break-word;
    overflow-wrap: anywhere;
    line-break: loose;
    word-spacing: 0;
    white-space: pre-line;
}
```

### 11. 分页控制

经历类模块尽量避免单条经历被分页切断。

建议：

- section 标题后避免立即分页
- 单条经历 `page-break-inside: avoid`

---

## 静态预览兼容规范

### 12. 不要只依赖 `th:if` 处理可视占位

因为直接在浏览器打开 HTML 时，`th:if` 不会执行。

例如下面这种写法：

```html
<div class="avatar-box" th:if="${avatarUrl != null and avatarUrl != ''}"></div>
<div class="avatar-box" th:if="${avatarUrl == null or avatarUrl == ''}"></div>
```

在静态预览时，两个元素都会显示。

因此需要注意：

- 如果模板主要给 Thymeleaf 渲染，可接受这种写法
- 如果还需要本地静态预览，尽量避免写两个视觉完全一样的并列节点
- 更推荐用一个容器，内部做样式降级，或明确注明“静态预览会看到重复占位”

---

## 视觉改造规则

### 13. 可以改什么

允许根据设计稿调整：

- 字体大小
- 行高
- 间距
- 颜色
- 栏目标题样式
- 块状分栏布局
- 卡片或非卡片样式
- 头像外框样式
- 多页拆分方式

前提是这些调整不能违反上面的 iText7 兼容限制。

### 14. 不要改什么

不要轻易改动：

- 字段来源
- 判空逻辑的大方向
- 列表循环来源
- 关键默认值语义
- HTML 转 PDF 的基本兼容策略

---

## 生成模板时的执行步骤

### 15. 推荐流程

1. 先读取 `resume-template-new.html`
2. 抽取所有现有字段和判空方式
3. 对照设计稿，只改视觉布局，不先改字段结构
4. 如果设计稿里出现新信息位，先判断能不能从现有字段推导
5. 不能推导时，用静态占位或弱依赖方式处理
6. 写完后检查所有 `th:text`、`th:if`、`th:each`
7. 检查是否覆盖了原模板中的主要数据模块
8. 用 `xmllint --html --noout <file>` 做基础语法检查

---

## 生成时可直接使用的提示词

```md
请基于 /Users/a1234/Downloads/html-pdf/resume-template-new.html 生成一个新的简历 HTML 模板。

要求：
- 字段和数据来源必须严格以 resume-template-new.html 为准
- 使用 Thymeleaf 语法
- 允许调整视觉样式，但不要新增后端强依赖字段
- 要兼容 HTML 转 PDF 场景
- 尽量兼容本地静态预览
- 必须同时遵守 /Users/a1234/Downloads/html-pdf/iText7_HTML_to_PDF_不兼容问题汇总.md 中的兼容限制
- 不要使用 display:grid、gap、flex-wrap、calc()、vw/vh、justify-content: space-between/space-around/space-evenly、flex:1、position:fixed、position:sticky
- 图片和头像不要依赖 object-fit、圆角裁切、复杂 z-index
- 布局优先使用固定宽度、inline-block、float 等更稳的 PDF 写法
- 缺失数据时默认值风格尽量和原模板一致
- 工作经历、项目经历、教育经历、在校经历、附加信息等模块不能丢

如果设计稿里有原模板没有稳定字段的内容，例如求职意向具体值、学校独立字段、毕业时间、二维码、作品集、MBTI，不要直接新增必填字段，优先使用现有字段推导，或者使用静态占位/弱依赖方案。
```
