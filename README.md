# Netty 介绍

Netty是由JBOSS提供的一个java开源框架，现为Github上的独立项目

Netty是一个异步的，基于事件驱动的网络应用框架，用以快速开发高性能、高可靠性的网络IO程序

Netty主要针对TCP协议下，面向Client端的高并发应用，或者Peer-to-Peer场景下的大量数据持续传输应用

Netty本质是一个NIO框架，适用于服务器通讯相关的多种应用场景

# I/O模型介绍

I/O模型就是用什么样的通道进行数据的发送与接收，很大程度上决定了程序通信的性能

Java共支持3种网络IO模型：BIO、NIO、AIO

**BIO**：同步阻塞模型，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器就需要启动一个线程进行处理，**如果这个连接不做任何事情（没有数据需要读取时）会进行阻塞造成不必要的线程开销**

**NIO**：同步非阻塞模型，服务器实现模式为一个线程处理多个请求，即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有IO请求就进行处理

**AIO**：异步非阻塞模型，AIO引入了异步通道的概念，采用了Proactor模式，简化了程序编写，有效的请求才启动线程。它的特点是先由操作系统完成后才通知服务器程序启动线程处理，一般适用于连接数较多且连接时间较长的应用

# NIO介绍

1. Java NIO全称Java Non-Blocking IO，是指jdk提供的新api。从jdk1.4开始，java提供了一系列改进的IO的新特性，被统称为NIO(New IO)，是**同步非阻塞**的
2. NIO相关的类都被放在java.nio包下，并且对原java.io包中的很多类都进行了改写
3. NIO有三大核心部分：**Channel（通道），Buffer（缓冲区），Selector（选择器）**
4. NIO是**面向缓存区**，或者**面向块**编程的。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动，这就增加了处理过程中的灵活性，使用它可以提供非阻塞性的高伸缩性网络
5. NIO的非阻塞模式是一个线程从某通道发送或读取数据时，仅得到当前可用的数据，如果没有可用的数据就什么都不会获取，**而不是保持线程阻塞**。所以直到数据可用之前，线程可以继续做其他的事情。
6. HTTP2.0使用了多路复用的技术，做到同一个连接并发处理多个请求，而且并发请求的数量比HTTP1.1大了好几个数量级

## 1. 缓冲区（Buffer）

缓冲区本质上是一个可以读写数据的内存块，可以理解成一个**容器对象（含数组）**，该容器提供了一组方法，可以更轻松的使用内存块。

缓冲区对象内置了一些机制，能够追踪和记录缓冲区的状态变化情况。Channel提供从文件、网络读取数据的渠道，但是读取或写入的数据都必须经由Buffer

### Buffer类极其子类

在NIO中，Buffer是一个顶层父类，它是一个抽象类，层级图如下

<img src="image/1.png" style="zoom:120%;" />

* ByteBuffer，存储字节数据到缓冲区
* ShortBuffer，存储字符串数据到缓冲区
* CharBuffer，存储字符数据到缓冲区
* ...

Buffer类定义了所有的缓冲区都具有的四个属性来提供关于其所包含的数据元素的信息，例如：缓冲区的容量，终点，当前位置，标记等

## 2. 通道（Channel）

NIO的通道类似于流，有以下区别

* 通道可以进行读写，而流只能读或写
* 通道可以实现异步读写数据
* 通道可以从缓冲读数据，也可以写数据到缓冲

Channel是一个接口，常用的实现类有：`FileChannel`, `DatagramChannel`, `ServerSocketChannel`和`SocketChannel`。其中`FIleChannel`用于文件的数据读写，DatagramChannel用于UDP的数据读写，ServerSocketChannel和SocketChannel用于TCP的数据读写

### FileChannel

> 注意：FileChannel只能工作在阻塞模式中

`FileChannel`不能直接打开，必须用过`FileInputStream`、`FileOutputStream`、`RandomAccessFile`来获取`FileChannel`，他们都有`getChannel`方法

* 通过`FileInputStream`获取的Channel只能读
* 通过`FileOutputStream`获取的Channel只能写
* 通过`RandomAccessFile`获取的channel根据构造`RandomAccessFile`时的读写模式决定是否可读可写

## 3. Thread, Selector, Channel, Buffer之间的关系

1. 一个Thread对应一个Selector
2. 一个Selector对应多个Channel
3. 一个Channel对应一个Buffer
4. 程序切换到哪个Channel是由事件(Event)决定的，Selector会根据不同的事件，在各个通道上切换
5. Buffer就是一个内存块，低层维护了一个数组
6. 数据的读写是通过Buffer，与BIO通过流不同，NIO的Buffer可以读也可以写，用Flip方法切换
7. Channel是双向的，可以返回低层操作系统的情况