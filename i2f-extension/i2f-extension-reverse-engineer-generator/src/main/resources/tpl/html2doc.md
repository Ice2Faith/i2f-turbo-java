# html2doc

## 方式一、使用word打开html文件
- 使用word打开html文件
- 另存为doc文件为a.doc
- 新建一个空的doc文件为b.doc
- 打开这两个doc文件a.doc,b.doc
- 将另存为的a.doc中内容全选
- 复制到空的b.doc中即可保持页面不变

## 方式二、使用浏览器调试宽度进行复制

- 使用浏览器打开html文件
- 全选内容，复制
- 粘贴到word中
- 如果宽度不合适
- 则使用开发者工具
- 修改 body > div.document 元素的样式
- 或者直接编辑html文件

```css
.document {
    max-width: calc(210mm * 1);
    overflow-wrap: break-word;
    margin: 0 auto;
}
```

- 修改max-width的值
- 默认 calc(210mm * 1)
- 表示，宽度为 210mm ，也就是 A4 纸张的默认宽度， * 1 表示缩放为 1 倍
- 也就是不进行缩放，所以，一般来说，只需要调整缩放倍率到合适的大小即可
- 比如：max-width: calc(210mm * 1);
- 需要注意的是，* 号前后需要有空格
- 或者直接修改为满意的大小
- 比如：max-width: 150mm;