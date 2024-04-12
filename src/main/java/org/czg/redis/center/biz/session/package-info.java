/**
 * 会话管理
 * </>
 * Spring Session 是 Spring Framework 的一个项目，旨在简化分布式应用程序中的会话管理。
 * 在传统的基于 Servlet 的应用程序中，会话管理是通过 HttpSession 接口实现的，但在分布式环境中，每个节点上的 HttpSession 不能简单地共享，因此需要一种机制来管理会话并确保会话在集群中的一致性。
 * </>
 * Spring Session 提供了一种简单的方法来解决这个问题，它将会话数据从容器（如 Tomcat 或 Jetty）中分离出来，并存储在外部数据存储（如 Redis、MongoDB、JDBC 等）中。
 * 这样，不同节点上的应用程序实例可以共享相同的会话数据，实现分布式环境下的会话管理。
 */
package org.czg.redis.center.biz.session;