# Android客户端代码规范

## 一、源文件基础

### 1. 文件名

源文件以其最顶层的类名来命名，大小写敏感，文件扩展名为.java。

### 2. 文件编码

源文件编码格式为UTF-8。

### 3. 特殊字符

#### 空白字符

除了行结束符序列，ASCII水平空格字符(0x20，即空格)是源文件中唯一允许出现的空白字符，这意味着：

- 所有其它字符串中的空白字符都要进行转义。
- 制表符不用于缩进。

#### 特殊转义序列

对于具有特殊转义序列的任何字符(\b, \t, \n, \f, \r, \“, \‘及\)，我们使用它的转义序列，而不是相应的八进制(比如\012)或Unicode(比如\u000a)转义。

#### 非ASCII字符

对于剩余的非ASCII字符，是使用实际的Unicode字符(比如∞)，还是使用等价的Unicode转义符(比如\u221e)，取决于哪个能让代码更易于阅读和理解。
> Tip: 在使用Unicode转义符或是一些实际的Unicode字符时，建议做些注释给出解释， 这有助于别人阅读和理解。

## 二、源文件结构

### 1. 源文件结构

一个源文件包含(按顺序地)：

- 许可证或版权信息(如有需要)
- package语句
- import语句
- 一个顶级类(只有一个)

以上每个部分之间用一个空行隔开。

#### 许可证或版权信息

如果一个文件包含许可证或版权信息，那么它应当被放在文件最前面。

#### package语句

package语句不换行(即package语句写在一行里)

#### import语句

- **import不要使用通配符**，即，不要出现类似这样的import语句：import java.util.*;
- **import语句不换行**，列限制(4.4节)并不适用于import语句。(每个import语句独立成行)
- **顺序和间距import语句**可分为以下几组，按照这个顺序，每组由一个空行分隔，组内不空行，按字典序排列：
- 所有的静态导入独立成组
- 第三方的包。每个顶级包为一组，字典序。例如：android, com, junit, org, sun
- java imports
- javax imports

### 2. 类声明

#### 只有一个顶级类声明

每个顶级类都在一个与它同名的源文件中(当然，还包含.java后缀)。

#### 类成员顺序

类的成员顺序对易学性有很大的影响，但这也不存在唯一的通用法则。我们按照以下顺序排列：

- 常量
- 类成员变量
- 构造函数
- 覆写的方法
- 公共方法
- 私有方法
- 内部类或者接口

#### 重载：永不分离

当一个类有多个构造函数，或是多个同名方法，这些函数/方法应该按顺序出现在一起，中间不要放进其它函数/方法。

## 三、格式

### 1. 大括号

#### 使用大括号(即使是可选的)

大括号与if, else, for, do, while语句一起使用，即使只有一条语句(或是空)，也应该把大括号写上

#### 非空块

- 左大括号前不换行
- 左大括号后换行
- 右大括号前换行

如果右大括号是一个语句、函数体或类的终止，则右大括号后换行; 否则不换行。例如，如果右大括号后面是else或逗号，则不换行。

### 2. 空块

一个空的块状结构里什么也不包含，大括号可以简洁地写成{}，不需要换行。例外：如果它是一个多块语句的一部分(if/else 或?try/catch/finally)
，即使大括号内没内容，右大括号也要换行。

### 3. 块缩进：

4个空格

### 4. 一行一个语句

每个语句后要换行。

### 5. 列限制和换行

#### 列限制 ：100字符

一个项目可以选择一行100个字符的列限制，除了下述例外，任何一行如果超过这个字符数限制，必须自动换行。  
例外：

- **不可能满足列限制的行**，例如，Javadoc中的一个长URL，或是一个长的JSNI方法参考。
- **package和import语句**
- **注释中那些可能被剪切并粘贴到shell中的命令行**。

#### 换行的策略

当超过100字符，并且不属于上述描述的例外情况之外，可以采用以下方式换行

- 在运算符前换行

例如:

    int longName = anotherVeryLongVariable + anEvenLongerOne - thisRidiculousLongOne  
                   + theFinalOne;

- 当多个方法在一行中调用时，将每个方法放在一行

例如：

    Picasso.with(context)  
           .load("http://ribot.co.uk/images/sexyjoe.jpg")  
           .into(imageView);

- 当一个方法有很多参数时，或者参数很长时，每个参数放在一行

例如：

    loadPicture(context,  
                "http://ribot.co.uk/images/sexyjoe.jpg",  
                mImageViewProfilePicture,  
                clickListener,  
                "Title of the picture");  

### 6. 具体结构

#### 变量声明

- **每次只声明一个变量不要使用组合声明**，比如int a, b;。
- **需要时才声明，并尽快进行初始化**，不要在一个代码块的开头把局部变量一次性都声明了(这是c语言的做法)
  ，而是在第一次需要使用它时才声明。局部变量在声明时最好就进行初始化，或者声明后尽快进行初始化。

#### 数组

- **数组初始化：**：可写成块状结构，数组初始化可以写成块状结构
- **非C风格的数组声明：**
  中括号是类型的一部分：String[] args，而非String args[]。

#### switch语句

- **缩进：**
  与其它块状结构一致，switch块中的内容缩进为2个空格。 每个switch标签后新起一行，再缩进2个空格，写下一条或多条语句。
- **default的情况要写出来：**
  每个switch语句都包含一个default语句组，即使它什么代码也不包含

#### 注解(Annotations)

注解紧跟在文档块后面，应用于类、方法和构造函数，一个注解独占一行。这些换行不属于自动换行，因此缩进级别不变，例如：

    @Override
    
    public String getNameIfPresent() { ... }

注解用于变量，应该与变量放在一行，除非超过行的最大限制，例如：

    @Mock  DataManager mDataManager;

#### 注释

- **块注释风格：**
  块注释与其周围的代码在同一缩进级别.它们可以是`/* ... */`风格，也可以是`// ...`风格，对于多行的`/* ...*/`注释，后续行必须从`*开始， 并且与前一行的*`对齐

#### Modifiers修饰符

类和成员的modifiers如果存在，则按Java语言规范中推荐的顺序

    public protected private abstract static final transient volatile synchronized native strictfp

## 四、命名约定

### 1. 对所有标识符都通用的规则

标识符只能使用ASCII字母和数字，因此每个有效的标.符名称都能匹配正则表

### 2. 标识符类型的规则

#### 包名

包名全部小写，连续的单词只是简单地连接起来，不使用下划线。

#### 类名

类名都以`UpperCamelCase`风格编写。  
以大写开头，如果一个类的类名由多个单词组成，所有单词的首字母必须大写，单词尽量写全称，不要简写，除非约定俗成的名字，例如：URL，RTMP，RTSP
这些广泛使用的专有名词，可以全部大写，也可以首字母大写。例如

| 类型     | 规则 | 举例   |
| ------------------------ | ---- | ------ |
| activity类 | Activity为后缀标识    | 启动页面类：SplashActivity |
| Adapter类  | Adapter为后缀标识    | 定投列表适配器：FncListAdapter |
| 公共方法类 | Utils或Manager为后缀标识    | 线程池管理类：ThreadPoolManager 工具类：TimeUtils |
| 数据库类  | 以DBHelper后缀标识    | 新闻数据库：NewDBHelper |
| Service类      | 以Service为后缀标识   | 时间服务：TimeService |
| BroadcastReceiver类 | 以Receiver为后缀标识   | 时间通知：TimeReceiver |
| ContentProvider  | 以Provider为后缀标识    | 新闻数据库：NewDBHelper |
| 直接写的共享基础类     | 以Base开头   | BaseActivity，BaseFragment |

#### 方法名

方法名都以`lowerCamelCase`风格编写。  
方法名通常是动.或动.短语。例如

| 方法     | 描述   |
| ------------------------ | ---------- |
| initXX() | 初始化相关方法,使用init为前缀标识，如初始化布局initView() |
| isXX()  | checkXX()方法返回值为boolean型的请使用is或check为前缀标识 |
| getXX()      | 返回某个值的方法，使用get为前缀标识 |
| handleXX() | 对数据进行处理的方法，尽量使用handle为前缀标识 |
| displayXX()  | 弹出提示框和提示信息，使用display为前缀标识 |
| saveXX()      | 与保存数据相关的，使用save为前缀标识 |
| resetXX() | 对数据重组的，使用reset前缀标识 |
| clearXX()  | 清除数据相关的 |
| removeXXX()     | 清除数据相关的 |
| drawXXX()     | 绘制数据或效果相关的，使用draw前缀标识 |

#### 常量名

常量名命名模式为`CONSTANT_CASE`，全部字母大写，用下划线分隔单.

- 组件常量名  
  在使用一些组件时传递值，定义的KEY的常量命名如下：

| 组件     | 命名   |
| ------------------------ | ---------- |
| SharedPreferences | PREF_ |
| Bundle  | BUNDLE_ |
| Fragment Arguments | ARGUMENT_ |
| Intent Extra | EXTRA_ |
| Intent Action  | ACTION_ |

- 标签常量名  
  在使用网络请求，权限请求的时候，会有一些请求码需要我们定义为常量

| 类型    | 命名   |
| ------------------------ | ---------- |
| 网络请求 | FLAG_REQUEST_ |
| Activity Result请求码  | INTENT_REQUEST_CODE_ |
| 权限请求码 | PERMISSION_REQUEST_CODE_ |

#### 参数名

参数名以`lowerCamelCase`风格编写。  
参数应.避免用单个字符命名

#### 局部变量名

局部变量名以`lowerCamelCase`风格编写,比起其它类型的名称，局部变量可以有更为.松的缩写。

#### 类变量名

类变量以前缀加上驼峰命名的方式

| 类型    | 前缀（默认以驼峰命名法命名）   |
| ------------------------ | ---------- |
| 成员变量 | m（member） |
| 布尔变量  | is，has等 |
| 静态变量 | s（static） |

##### 类中的控件变量名

类中控件命名满足上述定义前缀+功能描述+控间缩写（或全称)或者Layout（布局容器）

| 类名    | 变量名   |
| ------------------------ | ---------- |
| TextView | mDescriptionTxt |
| ProgressBar  | mDescriptionProgress |
| Button | mDescriptionBtn） |
| SeekBar | mDescriptionSeekBar |
| ImageButton  | mDescriptionImgBtn |
| VideoView | mDescriptionVideoView） |
| ImageView | mDescriptionImg |
| Spinner  | mDescriptionSpinner |
| RadioButton | mDescriptionRb） |
| WebView | mDescriptionWebView |
| EditText  | mDescriptionEdit |
| RecyclerView | mDescriptionRecyclerView） |
| ListView | mDescriptionListView） |
| ScrollView  | mDescriptionScrollView |
| GridView | mDescriptionGridView） |

#### 资源文件命名

##### xml 控件id 统一命名规范

xml中id命名统一使用后缀 _xx，对应java文件中变量命名`xxBtn`等  
命名：模块名_逻辑名_控件名 `login_name_edit`

##### Layout资源文件命名

Layout资源文件的命名(全部小写，下划线分隔)

| 类名    | 变量名   |
| ------------------------ | ---------- |
| Activity的资源文件 | activity_description1_description2.xml |
| Fragment的资源文件  | fragment_description1_description2.xml |
| Listview列表项的资源文件 | item_description1_description2.xml） |
| Dialog命名 | dialog_description1_description2.xml |
| PopupWindow命名  | pop_description1_description2.xml |
| Include的文件命名 | include_description1_description2.xml |

##### 动画文件命名

动画文件（anim文件夹下） ：`anim_fade_in , anim_push_down_in` 逻辑名称_方向_序号。

##### menu文件命名

menu资源文件的命名(全部小写，下划线分隔):`menu_description1.description2.xml`

##### drawable资源命名

drawable资源：`selector_description1_description2.xml` `shape_description1_description2.xml ` 业务功能描述_
控件描述_控件状态限定词。

例如：

    `button_bg_sendmessage_selector.xml`

##### color命名

公用颜色命名：`color_description` 以color为前缀，全部小写，下划线分隔。  
例如：

	<color name="color_333333">#333333</color>
	<color name="color_333333_60">#60333333</color>  

非公用颜色命名:`color_function_description`
例如

	<color name="color_desc_title_">#222222</color>

##### string 命名

公用字符串：通用_描述 命名  
例如：

    <string name="common_str_ok">确定</string>

非公用字符串：模块_描述 命名  
例如：

    <string name="mine_default_nickname">我的昵称</string>

不同模块之间需要使用间隔符分开

	<!-- 主页 -->
	<string name="main_tab_clean">清理</string>

代码里不能出现写死的文字

## Javadoc

### 1. 格式

#### 类说明（File Header）

在每个创建类的时候，需要给它定义类说明：

    设置文件模板：File => Settings => Editor => File and Code Templates

```
/**
 * Function description
 *
 * @author author
 * @version [1.0.0]
 * @date ${DATE}
 * @see [class/method]
 * @since [1.0.0]
 */
```

#### 一般形式

Javadoc块的基本格式如下所示：

    /**
    * Multiple lines of Javadoc text are written here,
    * wrapped normally...
    */
    public int method(String p1) { ... }

或者是以下单行形式：

    /** An especially short bit of Javadoc. */

基本格式总是OK的。当整个Javadoc块能容纳于一行时且没有Javadoc标.@XXX)，可以使用单行

#### 段落

空行(即，只包含最左侧星号的行)会出现在段落之间和Javadoc标记(@XXX)之前(如果有的话。 除了第一个段落，每个段落第一个单词前都有标.`<p>`，并且它和第一个单词间没有空格。

#### Javadoc标记

标准的Javadoc标.按以下顺序出现：@param, @return, @throws, @deprecated,前面这种标记如果出现，描述都不能为空。
当描述无法在一行中容纳，连续行需要至少再缩进4个空格。

```
/**
 * 处理手机号、验证码输入
 *
 * @param phone 手机号
 * @param code 验证码
 */
public void checkLoginCodeChange(String phone, String code)
```

### 2. 摘要片段

每个类或成员的Javadoc以一个简短的摘要片段开始。这个片段是非常重要的，在某些情况下，它是唯一出现的文本，比如在类和方法索引中。这只是一个小片段，可以是一个名词短语或动词短语，但不是一个完整的句子。它不会以A
{@code Foo} is a...或This method returns...开头, 它也不会是一个完整的祈使句，如Save the
record...。然而，由于开头大写及被加了标点，它看起来就像是个完整的句子。
> Tip：一个常见的错误是把简单的Javadoc写成`/** @return the customer ID */`，这是不正确的。它应该写成`/** Returns the customer ID. */`。

### 3. 哪里需要使用Javadoc

至少在每个public类及它的每个public和protected成员处使用Javadoc，以下是一些例外：

#### 例外：不言自明的方法

对于简单明显的方法如getFoo，Javadoc是可选的(即，是可以不写的)。这种情况下除了写“Returns thefoo”，确实也没有什么值得写了。
单元测试类中的测试方法可能是不言自明的最常见例子了，我们通常可以从这些方法的描述性命名中知道它是干什么的，因此不需要额外的文档说明。  
Tip：如果有一些相关信息是需要读者了解的，那么以上的例外不应作为忽视这些信息 的理由。例如，对于方法名getCanonicalName，
就不应该忽视文档说明，因为读者很可能不知道词语canonical name指的是什么。

#### 例外：重载

如果一个方法重载了超类中的方法，那么Javadoc并非必需的。

#### 可选的Javadoc

对于包外不可见的类和方法，如有需要，也是要使用Javadoc的。如果一个注释是用来定义一个类，方法，字段的整体目的或行为， 那么这个注释应该写成Javadoc，这样更统一更友好

## 插件推荐

#### Alibaba Java Coding Guidelines plugin support