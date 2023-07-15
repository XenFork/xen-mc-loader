# xen-mc-loader
This is a mod loader similar to FabricMC and ForgeMC, developed in multiple languages
# function
- minecraftLibrary() This dependency will be automatically downloaded to the lib library
- @ModLoad() When applied to linked blocks, xenmc checks whether the target module has been loaded to determine whether to inject these main classes. Any annotation with linkage meaning should be added with this annotation
- @Modid() acts on the main class Only() acts on the client-server logic
# in the future
- using csv -> Mapping Table or more
- Using friend's event driven approach greatly improves efficiency and performance
- Markdown for mod loading identification files
- annotation processor to generate json to registry item block or other