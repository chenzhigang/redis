/**
 * 最新列表
 * Redis的List（列表）是一个基于双向链表实现的数据结构，允许我们在列表头部（左端）和尾部（右端）进行高效的插入和删除操作。
 * LPUSH命令：全称是LIST PUSH LEFT，用于将一个或多个值插入到列表的最左边（头部），在这里用于将最新生成的内容ID推送到列表顶部，保证列表中始终是最新的内容排在前面。
 * LTRIM命令用于修剪列表，保留指定范围内的元素，从而限制列表的长度。在这个场景中，每次添加新ID后都会执行LTRIM操作，只保留最近的N个ID，确保列表始终保持固定长度，即只包含最新的内容ID。
 */
package org.czg.redis.center.biz.list;