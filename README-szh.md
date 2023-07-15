# xen mc 模组加载器
- 这个是一个类似于FabricMc和forgeMC的模组加载器，支持多种语言联合开发
# function
- minecraftLibrary() 这个依赖项会自动下载到库
- @ModLoad() 作用于联动区块，xenmc会检查是否加载目标模组来判定是否注入这些主类，凡是带有联动意味的都要加上这个注解
- @Modid() 作用于主类 Only()作用于客户端服务端逻辑
# in the future
- 使用csv或更多作用于映射表
- 使用朋友的事件驱动方法大大提高了效率和性能
- 模组加载标识用markdown形式
- annotation processor ，用于生成json到注册物品，方块或其他