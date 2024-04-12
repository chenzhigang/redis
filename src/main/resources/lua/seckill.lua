-- 获取传入的参数
-- 购买的商品信息
local key = KEYS[1];
-- 购买的数量
local reqQuantity = ARGV[1];
-- 请求次序
local index = ARGV[2];
-- 请求返回采纳数组装
local resp = "请求:"..index..":数量:"..reqQuantity.."[%s]:剩余库存:%s";

-- 判断该商品信息是否存在，不存在直接返回
local existsFlag = redis.call('exists', key);
if existsFlag == 0 then
    return string.format(resp, "秒杀失败：该商品不存在", 0);
end

-- 判断库存是否充足，不足直接返回
local stock = tonumber(redis.call('get', key));
-- redis日志打印
-- redis内嵌的lua引擎提供了日志打印，需要在redis.conf中配置
-- loglevel =
-- logfile =
-- redis.log函数接收两个参数，第一个是日志级别（LOG_DEBUG, LOG_VERBOSE, LOG_NOTICE, LOG_WARNING），第二个是日志内容。
redis.log(redis.LOG_NOTICE, string.format(resp, "开始秒杀", stock));
if stock <= 0 or stock < tonumber(reqQuantity) then
    return string.format(resp, "秒杀失败，库存不足", stock);
end

-- 库存充足，扣减库存，返回剩余库存
local remainStock = redis.call('decrby', key, reqQuantity);
return string.format(resp, "秒杀成功", remainStock);


