/**
 * 获取当前层级的所有form组件
 */

const allComponents = require.context(
  '.',      // 在当前目录下查找
  false,    // 不遍历子文件夹
  /\.vue$/  // 正则匹配 以 .vue结尾的文件
)
let formComps = {}
allComponents.keys().forEach(fileName => {
  let comp = allComponents(fileName)
  formComps[fileName.replace(/^\.\/(.*)\.\w+$/, '$1')] = comp.default
})
export default formComps
